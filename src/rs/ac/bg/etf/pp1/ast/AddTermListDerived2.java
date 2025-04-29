// generated with ast extension for cup
// version 0.8
// 29/3/2025 15:35:18


package rs.ac.bg.etf.pp1.ast;

public class AddTermListDerived2 extends AddTermList {

    public AddTermListDerived2 () {
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
        buffer.append("AddTermListDerived2(\n");

        buffer.append(tab);
        buffer.append(") [AddTermListDerived2]");
        return buffer.toString();
    }
}
