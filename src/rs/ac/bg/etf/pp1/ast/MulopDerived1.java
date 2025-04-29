// generated with ast extension for cup
// version 0.8
// 30/3/2025 0:10:35


package rs.ac.bg.etf.pp1.ast;

public class MulopDerived1 extends Mulop {

    public MulopDerived1 () {
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
        buffer.append("MulopDerived1(\n");

        buffer.append(tab);
        buffer.append(") [MulopDerived1]");
        return buffer.toString();
    }
}
