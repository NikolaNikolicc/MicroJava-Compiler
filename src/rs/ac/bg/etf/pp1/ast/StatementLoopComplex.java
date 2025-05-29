// generated with ast extension for cup
// version 0.8
// 30/4/2025 1:47:6


package rs.ac.bg.etf.pp1.ast;

public class StatementLoopComplex extends Statement {

    private DoStatement DoStatement;
    private Statement Statement;
    private Condition Condition;
    private DesignatorStatementWhile DesignatorStatementWhile;

    public StatementLoopComplex (DoStatement DoStatement, Statement Statement, Condition Condition, DesignatorStatementWhile DesignatorStatementWhile) {
        this.DoStatement=DoStatement;
        if(DoStatement!=null) DoStatement.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.DesignatorStatementWhile=DesignatorStatementWhile;
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.setParent(this);
    }

    public DoStatement getDoStatement() {
        return DoStatement;
    }

    public void setDoStatement(DoStatement DoStatement) {
        this.DoStatement=DoStatement;
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
        if(DoStatement!=null) DoStatement.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoStatement!=null) DoStatement.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoStatement!=null) DoStatement.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementLoopComplex(\n");

        if(DoStatement!=null)
            buffer.append(DoStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

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
        buffer.append(") [StatementLoopComplex]");
        return buffer.toString();
    }
}
