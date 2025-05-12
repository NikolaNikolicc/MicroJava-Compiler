// generated with ast extension for cup
// version 0.8
// 12/4/2025 14:2:9


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementUnaryDerived2 extends DesignatorStatementUnary {

    public DesignatorStatementUnaryDerived2 () {
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
        buffer.append("DesignatorStatementUnaryDerived2(\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementUnaryDerived2]");
        return buffer.toString();
    }
}
