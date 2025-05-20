// generated with ast extension for cup
// version 0.8
// 20/4/2025 10:45:31


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementFuncCallWhile extends DesignatorStatementWhile {

    private Designator Designator;
    private DesignatorFuncCall DesignatorFuncCall;

    public DesignatorStatementFuncCallWhile (Designator Designator, DesignatorFuncCall DesignatorFuncCall) {
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
        buffer.append("DesignatorStatementFuncCallWhile(\n");

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
        buffer.append(") [DesignatorStatementFuncCallWhile]");
        return buffer.toString();
    }
}
