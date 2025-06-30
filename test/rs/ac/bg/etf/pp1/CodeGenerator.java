package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class CodeGenerator extends VisitorAdaptor {

    private int mainPC;

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
    public void visit(DesignatorAssignExpr node){
        Code.store(node.getDesignator().obj); // Store the value of the expression in the designator
    }

    @Override
    public void visit(UnaryIncrement node){
        Obj obj = ((DesignatorStatementUnarySemi)node.getParent()).getDesignator().obj;
        if (obj.getKind() == Obj.Elem){
            Code.put(Code.dup2);
        }
        Code.load(obj);
        Code.loadConst(1);
        Code.put(Code.add);
        Code.store(obj);
    }

    @Override
    public void visit(UnaryDecrement node){
        Obj obj = ((DesignatorStatementUnarySemi)node.getParent()).getDesignator().obj;
        if (obj.getKind() == Obj.Elem){
            Code.put(Code.dup2);
        }
        Code.load(obj);
        Code.loadConst(1);
        Code.put(Code.sub);
        Code.store(obj);
    }

    @Override
    public void visit(DesignatorArrayName node){
        Code.load(node.obj); // Load the address of the array
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
    

}
