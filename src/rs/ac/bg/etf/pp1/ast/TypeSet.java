// generated with ast extension for cup
// version 0.8
// 9/4/2025 21:24:20


package rs.ac.bg.etf.pp1.ast;

public class TypeSet extends Type {

    public TypeSet () {
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
        buffer.append("TypeSet(\n");

        buffer.append(tab);
        buffer.append(") [TypeSet]");
        return buffer.toString();
    }
}
