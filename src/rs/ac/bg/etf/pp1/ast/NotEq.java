// generated with ast extension for cup
// version 0.8
// 8/4/2025 9:47:48


package rs.ac.bg.etf.pp1.ast;

public class NotEq extends Relop {

    public NotEq () {
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
        buffer.append("NotEq(\n");

        buffer.append(tab);
        buffer.append(") [NotEq]");
        return buffer.toString();
    }
}
