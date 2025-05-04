// generated with ast extension for cup
// version 0.8
// 4/4/2025 21:38:23


package rs.ac.bg.etf.pp1.ast;

public class RelopDerived1 extends Relop {

    public RelopDerived1 () {
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
        buffer.append("RelopDerived1(\n");

        buffer.append(tab);
        buffer.append(") [RelopDerived1]");
        return buffer.toString();
    }
}
