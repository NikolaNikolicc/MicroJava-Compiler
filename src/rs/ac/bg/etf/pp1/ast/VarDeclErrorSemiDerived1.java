// generated with ast extension for cup
// version 0.8
// 6/4/2025 13:21:24


package rs.ac.bg.etf.pp1.ast;

public class VarDeclErrorSemiDerived1 extends VarDeclErrorSemi {

    public VarDeclErrorSemiDerived1 () {
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
        buffer.append("VarDeclErrorSemiDerived1(\n");

        buffer.append(tab);
        buffer.append(") [VarDeclErrorSemiDerived1]");
        return buffer.toString();
    }
}
