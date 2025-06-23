// generated with ast extension for cup
// version 0.8
// 24/5/2025 0:36:5


package rs.ac.bg.etf.pp1.ast;

public class DesignatorFuncCallDerived1 extends DesignatorFuncCall {

    private StackInitialize StackInitialize;

    public DesignatorFuncCallDerived1 (StackInitialize StackInitialize) {
        this.StackInitialize=StackInitialize;
        if(StackInitialize!=null) StackInitialize.setParent(this);
    }

    public StackInitialize getStackInitialize() {
        return StackInitialize;
    }

    public void setStackInitialize(StackInitialize StackInitialize) {
        this.StackInitialize=StackInitialize;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StackInitialize!=null) StackInitialize.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StackInitialize!=null) StackInitialize.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StackInitialize!=null) StackInitialize.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorFuncCallDerived1(\n");

        if(StackInitialize!=null)
            buffer.append(StackInitialize.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorFuncCallDerived1]");
        return buffer.toString();
    }
}
