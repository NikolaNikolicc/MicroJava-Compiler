// generated with ast extension for cup
// version 0.8
// 12/4/2025 14:2:9


package rs.ac.bg.etf.pp1.ast;

public class StatementDerived3 extends Statement {

    private StatementCondition StatementCondition;
    private Statement Statement;

    public StatementDerived3 (StatementCondition StatementCondition, Statement Statement) {
        this.StatementCondition=StatementCondition;
        if(StatementCondition!=null) StatementCondition.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public StatementCondition getStatementCondition() {
        return StatementCondition;
    }

    public void setStatementCondition(StatementCondition StatementCondition) {
        this.StatementCondition=StatementCondition;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementCondition!=null) StatementCondition.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementCondition!=null) StatementCondition.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementCondition!=null) StatementCondition.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementDerived3(\n");

        if(StatementCondition!=null)
            buffer.append(StatementCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementDerived3]");
        return buffer.toString();
    }
}
