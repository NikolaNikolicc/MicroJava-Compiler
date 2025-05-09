// generated with ast extension for cup
// version 0.8
// 9/4/2025 22:14:10


package rs.ac.bg.etf.pp1.ast;

public class StatementConditionDerived1 extends StatementCondition {

    public StatementConditionDerived1 () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementConditionDerived1(\n");

        buffer.append(tab);
        buffer.append(") [StatementConditionDerived1]");
        return buffer.toString();
    }
}
