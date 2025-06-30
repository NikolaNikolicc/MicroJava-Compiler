package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class CodeGenerator extends VisitorAdaptor {

    private int mainPC;

    public int getMainPC(){return this.mainPC;}

    @Override
    public void visit(RegularMethod node){
        Code.put(Code.enter);
        Code.put(node.obj.getLevel()); // b1 - number of formal parameters
        Code.put(node.obj.getLocalSymbols().size()); // b2 - number of local variables (formal + local)
    }

    @Override
    public void visit(MainMethod node){
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
