// generated with ast extension for cup
// version 0.8
// 5/4/2025 20:59:6


package rs.ac.bg.etf.pp1.ast;

public class VarDeclErrorCommaDerived1 extends VarDeclErrorComma {

    public VarDeclErrorCommaDerived1 () {
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
        buffer.append("VarDeclErrorCommaDerived1(\n");

        buffer.append(tab);
        buffer.append(") [VarDeclErrorCommaDerived1]");
        return buffer.toString();
    }
}
