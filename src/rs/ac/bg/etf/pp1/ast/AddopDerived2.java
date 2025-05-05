// generated with ast extension for cup
// version 0.8
// 5/4/2025 20:13:10


package rs.ac.bg.etf.pp1.ast;

public class AddopDerived2 extends Addop {

    public AddopDerived2 () {
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
        buffer.append("AddopDerived2(\n");

        buffer.append(tab);
        buffer.append(") [AddopDerived2]");
        return buffer.toString();
    }
}
