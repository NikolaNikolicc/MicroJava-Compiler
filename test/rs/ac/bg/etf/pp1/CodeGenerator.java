package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

import java.util.Stack;

public class CodeGenerator extends VisitorAdaptor {

    private int mainPC;
    private final static int fieldSize = 4;
    private Stack<Integer> elseJumps = new Stack<>(); // Stack to hold the addresses of conditional jumps
    private Stack<Integer> thenJumps = new Stack<>(); // Stack to hold the addresses of skip jumps

    private Stack<Integer> skipThen = new Stack<>();
    private Stack<Integer> skipElse = new Stack<>();


    public int getMainPC(){return this.mainPC;}

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
    public void visit(StatementRead node){
        if (node.getDesignator().obj.getType().equals(Tab.charType)) {
            Code.put(Code.bread);
        } else {
            Code.put(Code.read);
        }
        Code.store(node.getDesignator().obj); // Store the read value in the designator
    }

    @Override
    public void visit(StatementPrint node){
        Code.loadConst(0);
        if (node.getExpr().struct.equals(Tab.charType)) Code.put(Code.bprint);
        else Code.put(Code.print);
    }

    @Override
    public void visit(StatementPrintNumber node){
        Code.loadConst(node.getN2());
        if (node.getExpr().struct.equals(Tab.charType)) Code.put(Code.bprint);
        else Code.put(Code.print);
    }

    @Override
    public void visit(FactorDesignator node){
        if (node.getDesignator().obj.getKind() == Obj.Con) {
            Code.loadConst(node.getDesignator().obj.getAdr());
        } else {
            Code.load(node.getDesignator().obj);
        }
    }

    @Override
    public void visit(FactorCreateArray node){
        Code.put(Code.newarray);
        if (node.getType().struct.equals(Tab.charType)){
            Code.put(0);
        } else{
            Code.put(1);
        }
    }

    @Override
    public void visit(FactorCreateObject node){
        Code.put(Code.new_);
        Code.put2(node.getType().struct.getNumberOfFields() * fieldSize); // Load the address of the class constructor
    }

    @Override
    public void visit(DesignatorAssignExpr node){
        Code.store(node.getDesignator().obj); // Store the value of the expression in the designator
    }

    @Override
    public void visit(UnaryInc node){
        Obj obj = ((DesignatorStatementUnarySemi)node.getParent()).getDesignator().obj;
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
        Obj obj = ((DesignatorStatementUnarySemi)node.getParent()).getDesignator().obj;
        if (obj.getKind() == Obj.Elem){
            Code.put(Code.dup2);
        } else if (obj.getKind() == Obj.Fld){
            Code.put(Code.dup);
        }
        Code.load(obj);
        Code.loadConst(1);
        Code.put(Code.sub);
        Code.store(obj);
    }

    @Override
    public void visit(DesignatorStatementFuncCall node){
        int offset = node.getDesignator().obj.getAdr() - Code.pc; // Calculate the offset to the function address
        Code.put(Code.call);
        Code.put2(offset);

        if (node.getDesignator().obj.getType() != Tab.noType) {
            Code.put(Code.pop); // If the function returns a value, we pop it from the stack
        }
    }

    @Override
    public void visit(FactorFuncCall node){
        int offset = node.getDesignator().obj.getAdr() - Code.pc; // Calculate the offset to the function address
        Code.put(Code.call);
        Code.put2(offset);
    }

    // Designator

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
        SyntaxNode parent = node.getParent();
        if (parent instanceof DesignatorClassMoreNotFinal || parent instanceof DesignatorClassMoreNotFinalElem){
            // If the parent is a DesignatorClassMoreFinal or DesignatorClassMoreFinalElem, we need to load the class instance
            Code.load(node.obj); // Load the address of the class instance
        }
    }

    @Override
    public void visit(DesignatorClassMoreNotFinal node){
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

    @Override
    public void visit(DesignatorArrayName node){
        Code.load(node.obj); // Load the address of the array
    }

    @Override
    public void visit(DesignatorClassArrayName node){ Code.load(node.obj); // Load the address of the class instance}
    }

    @Override
    public void visit(DesignatorClassElem node){
        Code.load(node.obj);
    }

    @Override
    public void visit(StatementReturn node){
        Code.put(Code.exit);
        Code.put(Code.return_);
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

    // Class

    @Override
    public void visit(DesignatorClassName node){
        // we don't need to explicitly call getfield  when accessing a class field because we have added load instruction to the factorDesignator method
        Code.load(node.obj); // Load the address of the class instance
    }

    // <editor-fold desc="Conditional Statements">

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

}
