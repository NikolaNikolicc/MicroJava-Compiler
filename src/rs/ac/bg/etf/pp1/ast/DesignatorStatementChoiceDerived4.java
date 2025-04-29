// generated with ast extension for cup
// version 0.8
// 30/3/2025 0:10:35


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementChoiceDerived4 extends DesignatorStatementChoice {

    public DesignatorStatementChoiceDerived4 () {
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
        buffer.append("DesignatorStatementChoiceDerived4(\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementChoiceDerived4]");
        return buffer.toString();
    }
}
