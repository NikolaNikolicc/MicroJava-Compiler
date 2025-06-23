// generated with ast extension for cup
// version 0.8
// 23/5/2025 22:42:39


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementFuncCall extends DesignatorStatement {

    private Designator Designator;
    private DesignatorFuncCall DesignatorFuncCall;

    public DesignatorStatementFuncCall (Designator Designator, DesignatorFuncCall DesignatorFuncCall) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignatorFuncCall=DesignatorFuncCall;
        if(DesignatorFuncCall!=null) DesignatorFuncCall.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignatorFuncCall getDesignatorFuncCall() {
        return DesignatorFuncCall;
    }

    public void setDesignatorFuncCall(DesignatorFuncCall DesignatorFuncCall) {
        this.DesignatorFuncCall=DesignatorFuncCall;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignatorFuncCall!=null) DesignatorFuncCall.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignatorFuncCall!=null) DesignatorFuncCall.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignatorFuncCall!=null) DesignatorFuncCall.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementFuncCall(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorFuncCall!=null)
            buffer.append(DesignatorFuncCall.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementFuncCall]");
        return buffer.toString();
    }
}
