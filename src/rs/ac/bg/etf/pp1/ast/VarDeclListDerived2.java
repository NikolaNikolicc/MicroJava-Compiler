// generated with ast extension for cup
// version 0.8
// 28/3/2025 18:13:0


package rs.ac.bg.etf.pp1.ast;

public class VarDeclListDerived2 extends VarDeclList {

    public VarDeclListDerived2 () {
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
        buffer.append("VarDeclListDerived2(\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListDerived2]");
        return buffer.toString();
    }
}
