// generated with ast extension for cup
// version 0.8
// 5/6/2025 14:44:19


package rs.ac.bg.etf.pp1.ast;

public class StatementLoopComplex extends Statement {

    private DoStatement DoStatement;
    private Statement Statement;
    private WhileStatement WhileStatement;
    private Condition Condition;
    private HandleWhileCondition HandleWhileCondition;
    private DesignatorStatementWhile DesignatorStatementWhile;
    private GoToStart GoToStart;

    public StatementLoopComplex (DoStatement DoStatement, Statement Statement, WhileStatement WhileStatement, Condition Condition, HandleWhileCondition HandleWhileCondition, DesignatorStatementWhile DesignatorStatementWhile, GoToStart GoToStart) {
        this.DoStatement=DoStatement;
        if(DoStatement!=null) DoStatement.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.WhileStatement=WhileStatement;
        if(WhileStatement!=null) WhileStatement.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.HandleWhileCondition=HandleWhileCondition;
        if(HandleWhileCondition!=null) HandleWhileCondition.setParent(this);
        this.DesignatorStatementWhile=DesignatorStatementWhile;
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.setParent(this);
        this.GoToStart=GoToStart;
        if(GoToStart!=null) GoToStart.setParent(this);
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

    public WhileStatement getWhileStatement() {
        return WhileStatement;
    }

    public void setWhileStatement(WhileStatement WhileStatement) {
        this.WhileStatement=WhileStatement;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public HandleWhileCondition getHandleWhileCondition() {
        return HandleWhileCondition;
    }

    public void setHandleWhileCondition(HandleWhileCondition HandleWhileCondition) {
        this.HandleWhileCondition=HandleWhileCondition;
    }

    public DesignatorStatementWhile getDesignatorStatementWhile() {
        return DesignatorStatementWhile;
    }

    public void setDesignatorStatementWhile(DesignatorStatementWhile DesignatorStatementWhile) {
        this.DesignatorStatementWhile=DesignatorStatementWhile;
    }

    public GoToStart getGoToStart() {
        return GoToStart;
    }

    public void setGoToStart(GoToStart GoToStart) {
        this.GoToStart=GoToStart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoStatement!=null) DoStatement.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(WhileStatement!=null) WhileStatement.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
        if(HandleWhileCondition!=null) HandleWhileCondition.accept(visitor);
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.accept(visitor);
        if(GoToStart!=null) GoToStart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoStatement!=null) DoStatement.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(WhileStatement!=null) WhileStatement.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(HandleWhileCondition!=null) HandleWhileCondition.traverseTopDown(visitor);
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.traverseTopDown(visitor);
        if(GoToStart!=null) GoToStart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoStatement!=null) DoStatement.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(WhileStatement!=null) WhileStatement.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(HandleWhileCondition!=null) HandleWhileCondition.traverseBottomUp(visitor);
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.traverseBottomUp(visitor);
        if(GoToStart!=null) GoToStart.traverseBottomUp(visitor);
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

        if(WhileStatement!=null)
            buffer.append(WhileStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(HandleWhileCondition!=null)
            buffer.append(HandleWhileCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementWhile!=null)
            buffer.append(DesignatorStatementWhile.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GoToStart!=null)
            buffer.append(GoToStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementLoopComplex]");
        return buffer.toString();
    }
}
