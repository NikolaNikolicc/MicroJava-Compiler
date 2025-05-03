// generated with ast extension for cup
// version 0.8
// 3/4/2025 18:29:53


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementChoiceDerived3 extends DesignatorStatementChoice {

    public DesignatorStatementChoiceDerived3 () {
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
        buffer.append("DesignatorStatementChoiceDerived3(\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementChoiceDerived3]");
        return buffer.toString();
    }
}
