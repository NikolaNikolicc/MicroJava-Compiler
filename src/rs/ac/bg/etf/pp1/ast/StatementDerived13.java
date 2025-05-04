// generated with ast extension for cup
// version 0.8
// 4/4/2025 22:6:6


package rs.ac.bg.etf.pp1.ast;

public class StatementDerived13 extends Statement {

    private Statement Statement;
    private Condition Condition;
    private DesignatorStatementWhile DesignatorStatementWhile;

    public StatementDerived13 (Statement Statement, Condition Condition, DesignatorStatementWhile DesignatorStatementWhile) {
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.DesignatorStatementWhile=DesignatorStatementWhile;
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.setParent(this);
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public DesignatorStatementWhile getDesignatorStatementWhile() {
        return DesignatorStatementWhile;
    }

    public void setDesignatorStatementWhile(DesignatorStatementWhile DesignatorStatementWhile) {
        this.DesignatorStatementWhile=DesignatorStatementWhile;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Statement!=null) Statement.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementDerived13(\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementWhile!=null)
            buffer.append(DesignatorStatementWhile.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementDerived13]");
        return buffer.toString();
    }
}
