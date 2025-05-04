// generated with ast extension for cup
// version 0.8
// 4/4/2025 21:46:21


package rs.ac.bg.etf.pp1.ast;

public class ProgramDeclListDerived2 extends ProgramDeclList {

    public ProgramDeclListDerived2 () {
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
        buffer.append("ProgramDeclListDerived2(\n");

        buffer.append(tab);
        buffer.append(") [ProgramDeclListDerived2]");
        return buffer.toString();
    }
}
