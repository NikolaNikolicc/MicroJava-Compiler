// generated with ast extension for cup
// version 0.8
// 4/6/2025 20:54:52


package rs.ac.bg.etf.pp1.ast;

public class SetopDerived1 extends Setop {

    public SetopDerived1 () {
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
        buffer.append("SetopDerived1(\n");

        buffer.append(tab);
        buffer.append(") [SetopDerived1]");
        return buffer.toString();
    }
}
