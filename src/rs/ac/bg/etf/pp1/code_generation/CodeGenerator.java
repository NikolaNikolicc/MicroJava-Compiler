package rs.ac.bg.etf.pp1.code_generation;

import rs.ac.bg.etf.pp1.syntax_analysis.output.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.syntax_analysis.output.ast.*;
import rs.ac.bg.etf.pp1.semantic_analysis.SemanticAnalyzer;
import rs.ac.bg.etf.pp1.util.StructExtended;
import rs.ac.bg.etf.pp1.util.TabExtended;
import rs.etf.pp1.symboltable.ModuleHandler;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

import java.util.*;

public class CodeGenerator extends VisitorAdaptor {

    private String name;

    private Struct currClass = null;
    private Struct currInterface = null;

    private final Struct setType = Tab.find("set").getType(); // Set type from the symbol table

    private boolean chainingMethodCall = false;
    private static boolean embeddedMethodsInitialized = false;
    private static Obj noReturnFromNoVoidMethodTrapPointer;

    private int mainPC;
    private final static int fieldSize = 4;
    private final Stack<Integer> elseJumps = new Stack<>(); // Stack to hold the addresses of conditional jumps
    private final Stack<Integer> thenJumps = new Stack<>(); // Stack to hold the addresses of skip jumps

    private final Stack<Integer> skipThen = new Stack<>();
    private final Stack<Integer> skipElse = new Stack<>();

    private final Stack<Integer> doJumps = new Stack<>(); // for do-while statements (true condition)
    private final Stack<Collection<Integer>> whileJumps = new Stack<>(); // for continue statements
    private final Stack<Collection<Integer>> skipWhile = new Stack<>(); // for break statements

    private static final StructExtended es = StructExtended.getInstance();
    private static final ModuleHandler moduleHandler = ModuleHandler.getInstance();
    private final TVFHandler tvfHandler = new TVFHandler();
    private SetHandler setHandler;

    public int getMainPC(){return this.mainPC;}

    // <editor-fold desc="Initialization">

    private void generateOrdChrLenMethods(){
        Obj ordMeth = Tab.find("ord");
        Obj chrMeth = Tab.find("chr");

        ordMeth.setAdr(Code.pc);
        chrMeth.setAdr(Code.pc);

        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);
        Code.put(Code.load_n);
        Code.put(Code.exit);
        Code.put(Code.return_);

        Obj lenMeth = Tab.find("len");
        lenMeth.setAdr(Code.pc);

        Code.put(Code.enter);
        Code.put(1); // 1 formal parameter
        Code.put(1); // 1 local variable
        Code.put(Code.load_n);
        Code.put(Code.arraylength);
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    static void printChar(char ch, Obj width){
        Code.loadConst(ch);
        if (width != null) {
            Code.load(width);
        } else {
            Code.loadConst(0); // Default width if not provided
        }
        Code.put(Code.bprint);
    }

    private void generateTrap(String message, Obj trapPointer){
        trapPointer.setAdr(Code.pc);
        for(char ch: message.toCharArray()){
            printChar(ch, null);
        }
        Code.put(Code.trap);
        Code.put(1);
    }

    public void initializeMethods(){
        if (embeddedMethodsInitialized) return;
        generateOrdChrLenMethods();
        String noReturnFromNoVoidMethodMessage = "No return from non-void method";
        noReturnFromNoVoidMethodTrapPointer = new Obj(Obj.Meth, "$noReturnFromNoVoidMethodTrap", Tab.noType, 0, 0);
        generateTrap(noReturnFromNoVoidMethodMessage, noReturnFromNoVoidMethodTrapPointer);
        setHandler = SetHandler.getInstance();
        embeddedMethodsInitialized = true;
    }

    public CodeGenerator(String name){
        initializeMethods();
        this.name = name;
    }

    // </editor-fold>

    // <editor-fold desc="Const and Var declarations">

    @Override
    public void visit(VarDeclFinalVar node){
        if (node.obj.getLevel() == SemanticAnalyzer.LEVEL_GLOBAL_VAR){
            node.obj.setAdr(Code.dataSize++);
        }
    }

    @Override
    public void visit(VarDeclFinalArray node){
        if (node.obj.getLevel() == SemanticAnalyzer.LEVEL_GLOBAL_VAR){
            node.obj.setAdr(Code.dataSize++);
        }
    }

    // </editor-fold>

    // <editor-fold desc="program start and end">

    @Override
    public void visit(ProgName node){
        moduleHandler.openModule(this.name);
    }

    @Override
    public void visit(Program node){
        moduleHandler.closeModule();
    }

    // </editor-fold>

    // <editor-fold desc="Method Declarations and Returns">

    @Override
    public void visit(RegularMethod node){
        node.obj.setAdr(Code.pc); // Set the address of the method in the symbol table;
        Code.put(Code.enter);
        Code.put(node.obj.getLevel()); // b1 - number of formal parameters
        Code.put(node.obj.getLocalSymbols().size()); // b2 - number of local variables (formal + local)
    }

    @Override
    public void visit(MainMethod node){
        this.mainPC = Code.pc; // save the position of the main method
        node.obj.setAdr(Code.pc); // Set the address of the method in the symbol table;

        Code.put(Code.enter);
        Code.put(node.obj.getLevel()); // b1 - number of formal parameters
        Code.put(node.obj.getLocalSymbols().size()); // b2 - number of local variables (formal + local)

        tvfHandler.putAllTVFsInMemory();
    }

    private void exitReturnFromMethod(){
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    @Override
    public void visit(MethodDecl node){
        if (!es.equals(node.obj.getType(), Tab.noType)){
            Code.putJump(noReturnFromNoVoidMethodTrapPointer.getAdr());
        }
        exitReturnFromMethod();
    }

    @Override
    public void visit(StatementReturn node){
        exitReturnFromMethod();
    }

    @Override
    public void visit(StatementReturnExpr node){
        exitReturnFromMethod();
    }

    // </editor-fold>

    // <editor-fold desc="[Read, Print] Standard Input/Output">

    @Override
    public void visit(StatementRead node){
        if (es.equals(node.getDesignator().obj.getType(), Tab.charType)) {
            Code.put(Code.bread);
        } else {
            Code.put(Code.read);
        }
        Code.store(node.getDesignator().obj); // Store the read value in the designator
    }

    @Override
    public void visit(StatementPrint node){
        if (es.equals(node.getExpr().struct, setType)) {
            Code.loadConst(0);
            int offset = setHandler.printSetMeth.getAdr() - Code.pc; // Calculate the offset to the printSet method
            Code.put(Code.call);
            Code.put2(offset); // Call the printSet method
            return;
        }
        Code.loadConst(0);
        if (es.equals(node.getExpr().struct, Tab.charType)) Code.put(Code.bprint);
        else Code.put(Code.print);
    }

    @Override
    public void visit(StatementPrintNumber node){
        if (es.equals(node.getExpr().struct, setType)) {
            Code.loadConst(node.getN2());
            int offset = setHandler.printSetMeth.getAdr() - Code.pc; // Calculate the offset to the printSet method
            Code.put(Code.call);
            Code.put2(offset); // Call the printSet method
            return;
        }
        Code.loadConst(node.getN2());
        if (es.equals(node.getExpr().struct, Tab.charType)) Code.put(Code.bprint);
        else Code.put(Code.print);
    }

    // </editor-fold>

    // <editor-fold desc="[Expr, Term] Arithmetic Operations (Minus, Addop, Mulop, Map Designator)">

    @Override
    public void visit(ExprMinusTerm node){
        Code.put(Code.neg);
    }

    @Override
    public void visit(ExprAddopTerm node){
        if (node.getAddop() instanceof AddopPlus) {
            Code.put(Code.add);
        } else if (node.getAddop() instanceof AddopMinus) {
            Code.put(Code.sub);
        }
    }

    @Override
    public void visit(TermTermMulopFactor node){
        if (node.getMulop() instanceof MulopMul) {
            Code.put(Code.mul);
        } else if (node.getMulop() instanceof MulopDiv) {
            Code.put(Code.div);
        } else if (node.getMulop() instanceof MulopMod) {
            Code.put(Code.rem);
        }
    }

    @Override
    public void visit(ExprDesignatorMap node){
        int methAdr = node.getDesignator().obj.getAdr();
        Obj arr = node.getMapDesignator().getDesignator().obj;

        Obj i = new Obj(Obj.Var, "$i", Tab.intType, 0, 1);
        Obj sum = new Obj(Obj.Var, "$sum", Tab.intType, 1, 1);
        Obj arrLocal = new Obj(Obj.Var, "$arr", arr.getType(), 2, 1);

        // Pretpostavljamo:
        // lokal 0: i
        // lokal 1: sum
        // lokal 2: arr

        Code.put(Code.enter);
        Code.put(0); // 0 formalnih parametara
        Code.put(3); // 3 lokalne promenljive

        // sum = 0
        Code.loadConst(0);
        Code.store(sum);

        // i = 0
        Code.loadConst(0);
        Code.store(i);

        // arr = ... (učitaj referencu na niz sa steka)
//        Code.load(node.getMapDesignator().getDesignator().obj);
        Code.store(arrLocal);

        int loopStart = Code.pc;

        // uslov: i < arr.length
        Code.load(i); // i
        Code.load(arrLocal); // arr
        Code.put(Code.arraylength);
        Code.putFalseJump(Code.lt, 0);
        int loopEnd = Code.pc - 2;

        // sum += poziv_metode(arr[i])
        Code.load(arrLocal); // arr
        Code.load(i); // i
        Code.put(Code.aload); // arr[i]
        int offset = methAdr - Code.pc;
        Code.put(Code.call);
        Code.put2(offset);
        Code.load(sum); // sum
        Code.put(Code.add);
        Code.store(sum); // sum

        // i++
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add);
        Code.store(i);

        Code.putJump(loopStart);
        Code.fixup(loopEnd);

        Code.load(sum); // vraća sum

        Code.put(Code.exit); // removing local variables from the stack
//        Code.put(Code.return_); // return instruction is not needed here, as the value is already on the stack

        // Fixing the method address
        node.getDesignator().obj.setAdr(methAdr);
    }

    @Override
    public void visit(MapDesignator node){
        Code.load(node.getDesignator().obj); // Load the address of the array
    }

    // </editor-fold>

    // <editor-fold desc="[Designator Statements, regular and while] function call, assignment, unary operations (inc, dec), set operations (union), left operands">

    @Override
    public void visit(UnaryInc node){
        Obj obj;
        if (node.getParent() instanceof  DesignatorStatementUnarySemi)
            obj = ((DesignatorStatementUnarySemi)node.getParent()).getDesignator().obj;
        else
            obj = ((DesignatorStatementUnarySemiWhile)node.getParent()).getDesignator().obj;

        if (obj.getKind() == Obj.Elem){
            Code.put(Code.dup2);
        } else if (obj.getKind() == Obj.Fld){
            Code.put(Code.dup);
        }
        Code.load(obj);
        Code.loadConst(1);
        Code.put(Code.add);
        Code.store(obj);
    }

    @Override
    public void visit(UnaryDec node){
        Obj obj;
        if (node.getParent() instanceof  DesignatorStatementUnarySemi)
            obj = ((DesignatorStatementUnarySemi)node.getParent()).getDesignator().obj;
        else
            obj = ((DesignatorStatementUnarySemiWhile)node.getParent()).getDesignator().obj;

        if (obj.getKind() == Obj.Elem){
            // If the designator is an array element, we need to duplicate both the index and the value
            Code.put(Code.dup2);
        } else if (obj.getKind() == Obj.Fld){
            // If the designator is a class field, we only need to duplicate the value
            Code.put(Code.dup);
        }
        Code.load(obj);
        Code.loadConst(1);
        Code.put(Code.sub);
        Code.store(obj);
    }

    private void functionCall(Obj node){
        if (chainingMethodCall){
            chainingMethodCall = false;
            // load TVF
            Code.put(Code.getstatic);
            Code.put2(0);
            Code.put(Code.getfield);
            Code.put2(0);
            // invokevirtual
            Code.put(Code.invokevirtual);
            for (char ch: node.getName().toCharArray()){
                Code.put4(ch);
            }
            Code.put4(-1);
        } else {
            int offset = node.getAdr() - Code.pc; // Calculate the offset to the function address
            Code.put(Code.call);
            Code.put2(offset);
        }
    }

    @Override
    public void visit(DesignatorStatementFuncCall node){
        functionCall(node.getDesignator().obj);

        if (node.getDesignator().obj.getType() != Tab.noType) {
            Code.put(Code.pop); // If the function returns a value, we pop it from the stack
        }
    }

    @Override
    public void visit(DesignatorStatementFuncCallWhile node){
        functionCall(node.getDesignator().obj);

        if (node.getDesignator().obj.getType() != Tab.noType) {
            Code.put(Code.pop); // If the function returns a value, we pop it from the stack
        }
    }

    @Override
    public void visit(DesignatorAssignExpr node){
        Code.store(node.getDesignator().obj); // Store the value of the expression in the designator
    }

    @Override
    public void visit(DesignatorAssignExprWhile node){
        Code.store(node.getDesignator().obj); // Store the value of the expression in the designator
    }

    @Override
    public void visit(DesignatorAssignSetop node){
        Code.load(node.getDesignator1().obj);
        Code.load(node.getDesignator2().obj);
//        Code.load(node.getDesignator().obj);

        // create new result array for the union result (same size as the assign left operand)
        Code.load(node.getDesignator().obj);
        Code.put(Code.arraylength);
        Code.put(Code.newarray);
        Code.put(1);

        int offset = 0;
        if (node.getSetop() instanceof SetopUnion){
            offset = setHandler.unionSetsMeth.getAdr() - Code.pc; // Calculate the offset to the add method
        } else if (node.getSetop() instanceof  SetopIntersection){
            offset = setHandler.intersectSetsMeth.getAdr() - Code.pc;
        } else if (node.getSetop() instanceof SetopDifference){
            offset = setHandler.differenceSetsMeth.getAdr() - Code.pc;
        }

        Code.put(Code.call);
        Code.put2(offset); // Call the unionSets method

        Code.store(node.getDesignator().obj); // store the result of the union in the assign left operand
    }

    @Override
    public void visit(DesignatorAssignSetopWhile node){
        Code.load(node.getDesignator1().obj);
        Code.load(node.getDesignator2().obj);

        // create new result array for the union result (same size as the assign left operand)
        Code.load(node.getDesignator().obj);
        Code.put(Code.arraylength);
        Code.put(Code.newarray);
        Code.put(1);

        int offset = 0;
        if (node.getSetop() instanceof SetopUnion){
            offset = setHandler.unionSetsMeth.getAdr() - Code.pc; // Calculate the offset to the add method
        } else if (node.getSetop() instanceof  SetopIntersection){
            offset = setHandler.intersectSetsMeth.getAdr() - Code.pc;
        } else if (node.getSetop() instanceof SetopDifference){
            offset = setHandler.differenceSetsMeth.getAdr() - Code.pc;
        }

        Code.put(Code.call);
        Code.put2(offset); // Call the unionSets method

        Code.store(node.getDesignator().obj); // store the result of the union in the assign left operand
    }

    // </editor-fold>

    // <editor-fold desc="[DesignatorClass] access Class properties">

    private void prepareForInvokeVirtual(){
        Code.put(Code.putstatic);
        Code.put2(0); // 0 is the offset for the TVF in the class instance
        // load this
        Code.put(Code.getstatic);
        Code.put2(0);
        // set flag
        chainingMethodCall = true;
    }

    @Override
    public void visit(DesignatorVar node){
        if (node.obj.getKind() == Obj.Fld){
            Code.put(Code.load_n);
        } else if (node.obj.getKind() == Obj.Meth && (currClass != null || currInterface != null) && !TabExtended.isMethodEmbedded(node.obj.getName())) {
            // If the designator is a method and we are in a class or interface context, prepare for virtual invocation
            Code.put(Code.load_n);
            prepareForInvokeVirtual();
            return;
        }
    }

    @Override
    public void visit(DesignatorClassName node){
        if (node.obj.getKind() == Obj.Fld && !node.obj.getName().equals("this")) {
            Code.put(Code.load_n);
        }
        Code.load(node.obj);
    }

    @Override
    public void visit(DesignatorClassElem node){
        if (node.obj.getKind() == Obj.Fld && !node.obj.getName().equals("this")){
            Code.put(Code.load_n);
        }
        Code.load(node.obj);
    }

    @Override
    public void visit(DesignatorClassMoreFinal node){
        if (node.obj.getKind() == Obj.Meth){
            prepareForInvokeVirtual();
            return;
        }
        SyntaxNode parent = node.getParent();
        if (parent instanceof DesignatorClassMoreNotFinal || parent instanceof DesignatorClassMoreNotFinalElem){
            // If the parent is a DesignatorClassMoreFinal or DesignatorClassMoreFinalElem, we need to load the class instance
            Code.load(node.obj); // Load the address of the class instance
        }
    }

    @Override
    public void visit(DesignatorClassMoreFinalElem node){
        SyntaxNode parent = node.getParent();
        if (parent instanceof DesignatorClassMoreNotFinal || parent instanceof DesignatorClassMoreNotFinalElem){
            // If the parent is a DesignatorClassMoreFinal or DesignatorClassMoreFinalElem, we need to load the class instance
            Code.load(node.obj); // Load the address of the class instance
        }
    }

    @Override
    public void visit(DesignatorClassMoreNotFinal node){
        if (node.obj.getKind() == Obj.Meth){
            prepareForInvokeVirtual();
            return;
        }
        SyntaxNode parent = node.getParent();
        if (parent instanceof DesignatorClassMoreNotFinal || parent instanceof DesignatorClassMoreNotFinalElem){
            // If the parent is a DesignatorClassMoreFinal or DesignatorClassMoreFinalElem, we need to load the class instance
            Code.load(node.obj); // Load the address of the class instance
        }
    }

    @Override
    public void visit(DesignatorClassMoreNotFinalElem node){
        SyntaxNode parent = node.getParent();
        if (parent instanceof DesignatorClassMoreNotFinal || parent instanceof DesignatorClassMoreNotFinalElem){
            // If the parent is a DesignatorClassMoreFinal or DesignatorClassMoreFinalElem, we need to load the class instance
            Code.load(node.obj); // Load the address of the class instance
        }
    }

    // </editor-fold>

    // <editor-fold desc="[DesignatorArray] access Arrays">

    @Override
    public void visit(DesignatorArrayName node){
        if (node.obj.getKind() == Obj.Fld){
            Code.put(Code.load_n);
        }
        Code.load(node.obj); // Load the address of the array
    }

    @Override
    public void visit(DesignatorClassArrayName node){ Code.load(node.obj); // Load the address of the class instance}
    }

    // </editor-fold>

    // <editor-fold desc="[Factor] Heap Allocation (arrays, objects and sets), Loading Constants, Function Calls, right operands">

    @Override
    public void visit(FactorDesignator node){
        if (node.getDesignator().obj.getKind() == Obj.Con) {
            Code.loadConst(node.getDesignator().obj.getAdr());
        } else {
            Code.load(node.getDesignator().obj);
        }
    }

    @Override
    public void visit(FactorFuncCall node){
        functionCall(node.getDesignator().obj);
    }

    @Override
    public void visit(NumConst node){
        Code.loadConst(node.getN1());
    }

    @Override
    public void visit(CharConst node){
        Code.loadConst(node.getC1());
    }

    @Override
    public void visit(BoolConst node){
        Code.loadConst(node.getB1());
    }

    @Override
    public void visit(FactorCreateArray node){
        // we allocationg one extra element for the array if it is an set type
        if (node.struct.getKind() == Struct.Enum){
            Code.loadConst(1);
            Code.put(Code.add);
        }

        Code.put(Code.newarray);
        if (es.equals(node.getType().struct, Tab.charType)){
            Code.put(0);
        } else{
            Code.put(1);
        }

        // Newly allocated array is initialized to zero by default so we don't need to do anything here for set init
    }

    @Override
    public void visit(FactorCreateObject node){
        Struct classType = node.getType().struct;

        Code.put(Code.new_);
        Code.put2((classType.getNumberOfFields() + 1) * fieldSize); // Load the address of the class constructor
        Code.put(Code.dup);
        Code.loadConst(tvfHandler.getMyTVFaddressInMemory(classType)); // Load the address of the TVF for the class
        Code.put(Code.putfield);
        Code.put2(0);
    }

    // </editor-fold>

    // <editor-fold desc="Conditional Statements While">

    @Override
    public void visit(DoStatement node){
        doJumps.push(Code.pc);
        whileJumps.push(new ArrayList<Integer>());
        skipWhile.push(new ArrayList<Integer>());
    }

    @Override
    public void visit(WhileStatement node){
        ArrayList<Integer> continueAddresses = (ArrayList<Integer>) whileJumps.pop();
        while(!continueAddresses.isEmpty()){
            Code.fixup(continueAddresses.remove(0));
        }
    }

    @Override
    public void visit(HandleWhileCondition node){
        while(!elseJumps.isEmpty()){
            int condJumpAddress = elseJumps.pop();
            Code.fixup(condJumpAddress);
        }
        Code.putJump(0); // jump to ELSE region
        skipThen.push(Code.pc - 2);
        while (!thenJumps.isEmpty()) {
            int skipJumpAddress = thenJumps.pop();
            Code.fixup(skipJumpAddress);
        }
    }

    @Override
    public void visit(GoToStart node){
        Code.putJump(doJumps.peek());
        // if we don't have condition, we don't put anything in skipThen stack
        if (!skipThen.isEmpty()) Code.fixup(skipThen.pop()); // for invalid condition
        ArrayList<Integer> breakAddresses = (ArrayList<Integer>)(skipWhile.pop());
        while (!breakAddresses.isEmpty()){
            Code.fixup(breakAddresses.remove(0));
        }
    }

    private void popWhileStacks(){
        doJumps.pop();
//        whileJumps.pop();
//        skipWhile.pop();
    }

    @Override
    public void visit(StatementLoopSimple node){
        popWhileStacks();
    }

    @Override
    public void visit(StatementLoopCondition node){
        popWhileStacks();
    }

    @Override
    public void visit(StatementLoopComplex node){
        popWhileStacks();
    }

    @Override
    public void visit(StatementBreak node){
        Code.putJump(0);
        skipWhile.peek().add(Code.pc - 2);
    }

    @Override
    public void visit(StatementContinue node){
        Code.putJump(0);
        whileJumps.peek().add(Code.pc - 2);
    }

    // </editor-fold>

    // <editor-fold desc="Conditional Statements If-Else">

    private int returnRelop(Relop node){
        if (node instanceof RelopEqual) {
            return Code.eq;
        } else if (node instanceof RelopNotEqual) {
            return Code.ne;
        } else if (node instanceof RelopLessThan) {
            return Code.lt;
        } else if (node instanceof RelopGreaterThan) {
            return Code.gt;
        } else if (node instanceof RelopLessThanOrEqual) {
            return Code.le;
        } else if (node instanceof RelopGreaterThanOrEqual) {
            return Code.ge;
        }
        return -1; // Invalid relop
    }

    /*
    int a = 2;
    if (a){region1}
    else {region2}

    if (a == 0) then jmp to region2 else continue to region1
    */
    @Override
    public void visit(CondFactExpr node){
        Code.loadConst(0);
        Code.putFalseJump(Code.ne, 0);
        elseJumps.push(Code.pc - 2); // Save the address of the jump instruction
    }

    @Override
    public void visit(CondFactRelop node){
        int relop = returnRelop(node.getRelop());
        Code.putFalseJump(relop, 0);
        elseJumps.push(Code.pc - 2); // Save the address of the jump instruction
    }

    @Override
    public void visit(ConditionCondTerm node){
        Code.putJump(0); // jump to THEN region
        int jumpAddress = Code.pc - 2;
        thenJumps.push(jumpAddress);
        while (!elseJumps.isEmpty()) {
            int condJumpAddress = elseJumps.pop();
            Code.fixup(condJumpAddress);
        }
    }

    @Override
    public void visit(ConditionOr node){
        Code.putJump(0); // jump to THEN region
        int jumpAddress = Code.pc - 2;
        thenJumps.push(jumpAddress);
        while(!elseJumps.isEmpty()){
            int condJumpAddress = elseJumps.pop();
            Code.fixup(condJumpAddress);
        }
    }

    @Override
    public void visit(StatementConditionCondition node){
        Code.putJump(0); // jump to ELSE region
        skipThen.push(Code.pc - 2);
        while (!thenJumps.isEmpty()) {
            int skipJumpAddress = thenJumps.pop();
            Code.fixup(skipJumpAddress);
        }
    }

    @Override
    public void visit(StatementNoElse node){
        Code.fixup(skipThen.pop());
    }

    @Override
    public void visit(Else node){
        Code.putJump(0);
        skipElse.push(Code.pc - 2);
        Code.fixup(skipThen.pop());
    }

    @Override
    public void visit(StatementElse node){
        Code.fixup(skipElse.pop());
    }

    // </editor-fold>

    // <editor-fold desc="Class Type Virtual Function Table (TVF)">

    @Override
    public void visit(ClassDeclName node){
        tvfHandler.createTVF(node.struct);
        currClass = node.struct;
    }

    @Override
    public void visit(InterfaceDeclName node){
        tvfHandler.createTVF(node.struct);
        currInterface = node.struct;
    }

    @Override
    public void visit(InterfaceDecl node){
        tvfHandler.addClassMethods(node.struct);
        currInterface = null;
    }

    @Override
    public void visit(ExtendsClass node){
        tvfHandler.inheritMethods(node.struct);
    }

    @Override
    public void visit(ClassNoExtend node) {
        currClass = null;
    }

    @Override
    public void visit(ClassYesExtend node){
        currClass = null;
    }

    @Override
    public void visit(ClassNoExtendYesMethods node){
        tvfHandler.addClassMethods(node.struct);
        currClass = null;
    }

    @Override
    public void visit(ClassYesExtendYesMethods node){
        tvfHandler.addClassMethods(node.struct);
        currClass = null;
    }

    // </editor-fold>

}
