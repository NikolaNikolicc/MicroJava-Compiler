// generated with ast extension for cup
// version 0.8
// 28/3/2025 18:13:0


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementDerived1 extends DesignatorStatement {

    private Designator Designator;
    private DesignatorStatementChoice DesignatorStatementChoice;

    public DesignatorStatementDerived1 (Designator Designator, DesignatorStatementChoice DesignatorStatementChoice) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignatorStatementChoice=DesignatorStatementChoice;
        if(DesignatorStatementChoice!=null) DesignatorStatementChoice.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignatorStatementChoice getDesignatorStatementChoice() {
        return DesignatorStatementChoice;
    }

    public void setDesignatorStatementChoice(DesignatorStatementChoice DesignatorStatementChoice) {
        this.DesignatorStatementChoice=DesignatorStatementChoice;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignatorStatementChoice!=null) DesignatorStatementChoice.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignatorStatementChoice!=null) DesignatorStatementChoice.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignatorStatementChoice!=null) DesignatorStatementChoice.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementDerived1(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementChoice!=null)
            buffer.append(DesignatorStatementChoice.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementDerived1]");
        return buffer.toString();
    }
}
