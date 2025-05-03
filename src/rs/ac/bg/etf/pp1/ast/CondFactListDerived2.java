// generated with ast extension for cup
// version 0.8
// 3/4/2025 18:54:8


package rs.ac.bg.etf.pp1.ast;

public class CondFactListDerived2 extends CondFactList {

    public CondFactListDerived2 () {
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
        buffer.append("CondFactListDerived2(\n");

        buffer.append(tab);
        buffer.append(") [CondFactListDerived2]");
        return buffer.toString();
    }
}
