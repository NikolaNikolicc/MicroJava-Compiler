// generated with ast extension for cup
// version 0.8
// 6/4/2025 7:9:34


package rs.ac.bg.etf.pp1.ast;

public class DesignatorAssignDerived2 extends DesignatorAssign {

    public DesignatorAssignDerived2 () {
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
        buffer.append("DesignatorAssignDerived2(\n");

        buffer.append(tab);
        buffer.append(") [DesignatorAssignDerived2]");
        return buffer.toString();
    }
}
