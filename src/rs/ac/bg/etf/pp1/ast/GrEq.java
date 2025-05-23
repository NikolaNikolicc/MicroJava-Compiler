// generated with ast extension for cup
// version 0.8
// 23/4/2025 11:45:36


package rs.ac.bg.etf.pp1.ast;

public class GrEq extends Relop {

    public GrEq () {
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
        buffer.append("GrEq(\n");

        buffer.append(tab);
        buffer.append(") [GrEq]");
        return buffer.toString();
    }
}
