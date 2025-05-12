// generated with ast extension for cup
// version 0.8
// 12/4/2025 14:2:9


package rs.ac.bg.etf.pp1.ast;

public class DesignatorFuncCallDerived2 extends DesignatorFuncCall {

    private StackInitialize StackInitialize;
    private ActPars ActPars;

    public DesignatorFuncCallDerived2 (StackInitialize StackInitialize, ActPars ActPars) {
        this.StackInitialize=StackInitialize;
        if(StackInitialize!=null) StackInitialize.setParent(this);
        this.ActPars=ActPars;
        if(ActPars!=null) ActPars.setParent(this);
    }

    public StackInitialize getStackInitialize() {
        return StackInitialize;
    }

    public void setStackInitialize(StackInitialize StackInitialize) {
        this.StackInitialize=StackInitialize;
    }

    public ActPars getActPars() {
        return ActPars;
    }

    public void setActPars(ActPars ActPars) {
        this.ActPars=ActPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StackInitialize!=null) StackInitialize.accept(visitor);
        if(ActPars!=null) ActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StackInitialize!=null) StackInitialize.traverseTopDown(visitor);
        if(ActPars!=null) ActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StackInitialize!=null) StackInitialize.traverseBottomUp(visitor);
        if(ActPars!=null) ActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorFuncCallDerived2(\n");

        if(StackInitialize!=null)
            buffer.append(StackInitialize.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActPars!=null)
            buffer.append(ActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorFuncCallDerived2]");
        return buffer.toString();
    }
}
