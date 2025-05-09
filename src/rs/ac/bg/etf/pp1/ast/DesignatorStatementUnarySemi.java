// generated with ast extension for cup
// version 0.8
// 9/4/2025 10:42:42


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementUnarySemi extends DesignatorStatement {

    private Designator Designator;
    private DesignatorStatementUnary DesignatorStatementUnary;

    public DesignatorStatementUnarySemi (Designator Designator, DesignatorStatementUnary DesignatorStatementUnary) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignatorStatementUnary=DesignatorStatementUnary;
        if(DesignatorStatementUnary!=null) DesignatorStatementUnary.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignatorStatementUnary getDesignatorStatementUnary() {
        return DesignatorStatementUnary;
    }

    public void setDesignatorStatementUnary(DesignatorStatementUnary DesignatorStatementUnary) {
        this.DesignatorStatementUnary=DesignatorStatementUnary;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignatorStatementUnary!=null) DesignatorStatementUnary.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignatorStatementUnary!=null) DesignatorStatementUnary.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignatorStatementUnary!=null) DesignatorStatementUnary.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementUnarySemi(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementUnary!=null)
            buffer.append(DesignatorStatementUnary.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementUnarySemi]");
        return buffer.toString();
    }
}
