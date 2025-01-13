// generated with ast extension for cup
// version 0.8
// 12/0/2025 20:50:53


package rs.ac.bg.etf.pp1.ast;

public class ConditionListDerived2 extends ConditionList {

    public ConditionListDerived2 () {
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
        buffer.append("ConditionListDerived2(\n");

        buffer.append(tab);
        buffer.append(") [ConditionListDerived2]");
        return buffer.toString();
    }
}
