// generated with ast extension for cup
// version 0.8
// 5/6/2025 0:36:49


package rs.ac.bg.etf.pp1.ast;

public class RelopGreaterThanOrEqual extends Relop {

    public RelopGreaterThanOrEqual () {
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
        buffer.append("RelopGreaterThanOrEqual(\n");

        buffer.append(tab);
        buffer.append(") [RelopGreaterThanOrEqual]");
        return buffer.toString();
    }
}
