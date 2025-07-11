package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class CodeGenerator extends VisitorAdaptor {

    private Obj addMeth;
    private Obj addAllMeth;
    private Obj printSetMeth;
    private Obj unionSetsMeth;
    private Struct setType = Tab.find("set").getType(); // Set type from the symbol table

    private boolean chainingMethodCall = false;

    private int mainPC;
    private final static int fieldSize = 4;
    private Stack<Integer> elseJumps = new Stack<>(); // Stack to hold the addresses of conditional jumps
    private Stack<Integer> thenJumps = new Stack<>(); // Stack to hold the addresses of skip jumps

    private Stack<Integer> skipThen = new Stack<>();
    private Stack<Integer> skipElse = new Stack<>();

    private Stack<Integer> doJumps = new Stack<>(); // for do-while statements (true condition)
    private Stack<Collection<Integer>> whileJumps = new Stack<>(); // for continue statements
    private Stack<Collection<Integer>> skipWhile = new Stack<>(); // for break statements

    private final ExtendedStruct es = ExtendedStruct.getInstance();
    private final static TVFHandler tvfHandler = TVFHandler.getInstance();

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

    // <editor-fold desc="Set Embedded Methods">

    private void generateAdd(){
//        add
//
//        if (set_size >= set_capacity)return;
//        set: [set_size, set_elem_0, ...], set_size <= set_capacity
//        set_capacity = arraylength
//
//        for (int i = 0; i < set_size; i++){
//            if (set[i + 1] == b)return;
//        }
//        set[set_size] = b;
//        set_size++;

        addMeth = Tab.find("add");
        Obj a = null; // Local variable for set
        Obj b = null; // Local variable for element to add
        Obj i = null;
        for(Obj member: addMeth.getLocalSymbols()){
            if (member.getName().equals("a")) {
                a = member;
            } else if (member.getName().equals("b")) {
                b = member;
            } else if (member.getName().equals("i")) {
                i = member;
            }
        }
        addMeth.setAdr(Code.pc);

        // add(set, b):
        // set[0] = set_size
        // set[1..set_size] = elementi
        // Ako je set_size >= set.length - 1, return
        // Ako je b već u skupu, return
        // Ako nije, dodaj na kraj i uvećaj set[0]
        Code.put(Code.enter);
        Code.put(2); // 2 formalna parametra: a, b
        Code.put(3); // 2 parametra + 1 lokalna promenljiva (i)

        Code.load(a);
        Code.loadConst(0);
        Code.put(Code.aload); // set[0]

        Code.load(a);
        Code.put(Code.arraylength); // set.length
        Code.loadConst(1);
        Code.put(Code.sub);

        Code.putFalseJump(Code.lt, 0); // if set[0] >= set.length - 1 then return
        int fullReturnJump = Code.pc - 2;

        Code.loadConst(0);
        Code.store(i); // i = 0

        // if (i >= set[0])break; // while (i < set[0])
        int loopStart = Code.pc;
        Code.load(i); // i
        Code.load(a);
        Code.loadConst(0);
        Code.put(Code.aload); // set[0] - get the size of the set
        Code.putFalseJump(Code.lt, 0); // if i >= set[0] then break
        int breakJump = Code.pc - 2;

        // if (set[i +1] == b) return;
        Code.load(a);
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.put(Code.aload); // set[i + 1]
        Code.load(b);
        Code.putFalseJump(Code.ne, 0); // if set[i + 1] == b then return
        int returnJump = Code.pc - 2;

        // i++
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.store(i);

        Code.putJump(loopStart); // jump to the start of the loop

        Code.fixup(breakJump);
        Code.load(a);
        Code.load(a);
        Code.loadConst(0);
        Code.put(Code.aload); // set[0] - get the size of the set
        Code.loadConst(1);
        Code.put(Code.add); // set[0] + 1
        Code.load(b);
        Code.put(Code.astore); // set[set[0] + 1] = b

        Code.load(a);
        Code.loadConst(0);
        Code.load(a);
        Code.loadConst(0);
        Code.put(Code.aload); // set[0] - get the size of the set
        Code.loadConst(1);
        Code.put(Code.add); // set[0] + 1
        Code.put(Code.astore); // set[0] = set[0] + 1

        Code.fixup(returnJump);
        Code.fixup(fullReturnJump);

        Code.put(Code.exit);
        Code.put(Code.return_);

    }

    private void generateAddAll(){
//        addAll(a, b)
//
//        for (int i = 0; i < b_size; i++){
//            add(a, b[i])
//        }

        addAllMeth = Tab.find("addAll");

        Obj a = null; // Local variable for set
        Obj b = null; // Local variable for element to add
        Obj i = null;
        for(Obj member: addMeth.getLocalSymbols()){
            if (member.getName().equals("a")) {
                a = member;
            } else if (member.getName().equals("b")) {
                b = member;
            } else if (member.getName().equals("i")) {
                i = member;
            }
        }
        addAllMeth.setAdr(Code.pc);

        Code.put(Code.enter);
        Code.put(2); // 2 formalna parametra: a, b
        Code.put(3); // 2 parametra + 1 lokalna promenljiva (i)

        Code.loadConst(0);
        Code.store(i); // i = 0

        int loopStart = Code.pc;

        // if (i >= b.length)break; // while (i < b.length)
        Code.load(i);
        Code.load(b);
        Code.put(Code.arraylength);
        Code.putFalseJump(Code.lt, 0);
        int loopEnd = Code.pc - 2;

        // add(a, b[i]);
        Code.load(a);
        Code.load(b);
        Code.load(i);
        Code.put(Code.aload); // b[i]
        int addOffset = addMeth.getAdr() - Code.pc; // Calculate the offset to the add method
        Code.put(Code.call);
        Code.put2(addOffset); // Call the add method

        // i++
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.store(i);

        Code.putJump(loopStart); // jump to the start of the loop

        Code.fixup(loopEnd);

        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    private void generatePrintSet(){
        printSetMeth = Tab.find("$printSet");

        Obj a = null; // Local variable for set
        Obj offset = null;
        Obj i = null;
        for(Obj member: printSetMeth.getLocalSymbols()){
            if (member.getName().equals("a")) {
                a = member;
            } else if (member.getName().equals("offset")) {
                offset = member;
            } else if (member.getName().equals("i")) {
                i = member;
            }
        }
        printSetMeth.setAdr(Code.pc);

        Code.put(Code.enter);
        Code.put(2); // 2 formalna parametra: a, offset
        Code.put(3); // 2 parametra + 1 lokalna promenljiva (i)

        int loopStart = Code.pc;
        Code.load(i);
        Code.load(a);
        Code.loadConst(0);
        Code.put(Code.aload); // set[0] - get the size of the set
        Code.putFalseJump(Code.lt, 0); // if i >= set[0] then break
        int loopEnd = Code.pc - 2;

        // print set[i + 1]
        Code.load(a);
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.put(Code.aload); // set[i + 1]
        Code.load(offset);
        Code.put(Code.print);

        // i++
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.store(i);

        Code.putJump(loopStart);

        Code.fixup(loopEnd);

        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    // c = a union b
    private void generateUnionSets(){
        unionSetsMeth = Tab.find("$union");

        Obj a = null;
        Obj b = null;
        Obj c = null;
        Obj i = null;
        for(Obj member: unionSetsMeth.getLocalSymbols()){
            if (member.getName().equals("a")) {
                a = member;
            } else if (member.getName().equals("b")) {
                b = member;
            } else if (member.getName().equals("c")) {
                c = member;
            } else if (member.getName().equals("i")) {
                i = member;
            }
        }
        unionSetsMeth.setAdr(Code.pc);

        Code.put(Code.enter);
        Code.put(3); // 3 formalna parametra: a, b, c
        Code.put(4); // 3 parametra + 1 lokalna promenljiva (i)

        // c = a;
        Code.loadConst(0);
        Code.store(i); // i = 0

        int loopStart = Code.pc;

        Code.load(i);
        Code.load(a);
        Code.loadConst(0);
        Code.put(Code.aload);

        Code.putFalseJump(Code.lt, 0);
        int loopEnd = Code.pc - 2;

        // add(c, a[i + 1])
        Code.load(c);
        Code.load(a);
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.put(Code.aload); // a[i + 1]
        int addOffset = addMeth.getAdr() - Code.pc; // Calculate the offset to the add method
        Code.put(Code.call);
        Code.put2(addOffset); // Call the add method

        // i++
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.store(i);

        Code.putJump(loopStart);

        Code.fixup(loopEnd);

        // c = c union b
        Code.loadConst(0);
        Code.store(i); // i = 0

        int loopStart1 = Code.pc;

        Code.load(i);
        Code.load(b);
        Code.loadConst(0);
        Code.put(Code.aload);

        Code.putFalseJump(Code.lt, 0);
        int loopEnd1 = Code.pc - 2;

        // add(c, b[i + 1])
        Code.load(c);
        Code.load(b);
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add);
        Code.put(Code.aload); // b[i + 1]
        int addOffset1 = addMeth.getAdr() - Code.pc; // Calculate the offset to the add method
        Code.put(Code.call);
        Code.put2(addOffset1); // Call the add method

        // i++
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.store(i);

        Code.putJump(loopStart1);

        Code.fixup(loopEnd1);

        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    // </editor-fold>

    private void initializeMethods(){
        generateOrdChrLenMethods();
        generateAdd();
        generateAddAll();
        generatePrintSet();
        generateUnionSets();
    }

    CodeGenerator(){
//        initializeMethods();
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
        node.obj.setAdr(Code.pc); // Set the address of the method in the symbol table;
        this.mainPC = Code.pc; // save the position of the main method
        Code.put(Code.enter);
        Code.put(node.obj.getLevel()); // b1 - number of formal parameters
        Code.put(node.obj.getLocalSymbols().size()); // b2 - number of local variables (formal + local)
    }

    @Override
    public void visit(MethodDecl node){
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    @Override
    public void visit(StatementReturn node){
        Code.put(Code.exit);
        Code.put(Code.return_);
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
            int offset = printSetMeth.getAdr() - Code.pc; // Calculate the offset to the printSet method
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
            int offset = printSetMeth.getAdr() - Code.pc; // Calculate the offset to the printSet method
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
            Code.put(Code.load_n);
            Code.put(Code.getfield);
            Code.put2(Code.const_n);
            // remove 0th parameter (the class instance) from the stack
            Code.put(Code.exit);
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
        Code.load(node.getDesignator().obj);
        int offset = unionSetsMeth.getAdr() - Code.pc; // Calculate the offset to the add method
        Code.put(Code.call);
        Code.put2(offset); // Call the unionSets method
    }

    @Override
    public void visit(DesignatorAssignSetopWhile node){
        Code.load(node.getDesignator1().obj);
        Code.load(node.getDesignator2().obj);
        Code.load(node.getDesignator().obj);
        int offset = unionSetsMeth.getAdr() - Code.pc; // Calculate the offset to the add method
        Code.put(Code.call);
        Code.put2(offset); // Call the unionSets method
    }

    // </editor-fold>

    // <editor-fold desc="[DesignatorClass] access Class properties">

    @Override
    public void visit(DesignatorClassName node){
        // we don't need to explicitly call getfield  when accessing a class field because we have added load instruction to the factorDesignator method
        Code.load(node.obj); // Load the address of the class instance
    }

    @Override
    public void visit(DesignatorClassElem node){
        Code.load(node.obj);
    }

    @Override
    public void visit(DesignatorClassMoreFinal node){
        SyntaxNode parent = node.getParent();
        if (parent instanceof DesignatorClassMoreNotFinal || parent instanceof DesignatorClassMoreNotFinalElem){
            // If the parent is a DesignatorClassMoreFinal or DesignatorClassMoreFinalElem, we need to load the class instance
            Code.load(node.obj); // Load the address of the class instance
        }
    }

    @Override
    public void visit(DesignatorClassMoreFinalElem node){
        if (node.obj.getKind() == Obj.Meth){
            Code.put(Code.enter);
            Code.put(Code.const_1); // 1 formal parameter (the class instance)
            Code.put(Code.const_1); // 1 + 0 local variable (the class instance)
            // load this
            Code.put(Code.load_n);
            // set flag
            chainingMethodCall = true;
            return;
        }

        SyntaxNode parent = node.getParent();
        if (parent instanceof DesignatorClassMoreNotFinal || parent instanceof DesignatorClassMoreNotFinalElem){
            // If the parent is a DesignatorClassMoreFinal or DesignatorClassMoreFinalElem, we need to load the class instance
            Code.load(node.obj); // Load the address of the class instance
        }
    }

    @Override
    public void visit(DesignatorClassMoreNotFinal node){
        if (node.obj.getKind() == Obj.Meth){
            Code.put(Code.enter);
            Code.put(Code.const_1); // 1 formal parameter (the class instance)
            Code.put(Code.const_1); // 1 + 0 local variable (the class instance)
            // load this
            Code.put(Code.load_n);
            // set flag
            chainingMethodCall = true;
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
        Code.fixup(skipThen.pop()); // for invalid condition
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
        // here we do not need to fix the else jumps (as above) because we just finished the whole condition
    }

    @Override
    public void visit(StatementConditionCondition node){
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
    }

    @Override
    public void visit(ExtendsClass node){
        tvfHandler.inheritMethods(node.struct);
    }

    @Override
    public void visit(ClassNoExtend node) {
        tvfHandler.putTVFInMemory(node.struct);
    }

    @Override
    public void visit(ClassYesExtend node){
        tvfHandler.putTVFInMemory(node.struct);
    }

    @Override
    public void visit(ClassNoExtendYesMethods node){
        tvfHandler.addClassMethods(node.struct);
        tvfHandler.putTVFInMemory(node.struct);
    }

    @Override
    public void visit(ClassYesExtendYesMethods node){
        tvfHandler.putTVFInMemory(node.struct);
    }

    // </editor-fold>

}
