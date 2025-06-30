// generated with ast extension for cup
// version 0.8
// 30/5/2025 11:27:22


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclErrorLBraceDerived1 extends ClassDeclErrorLBrace {

    public ClassDeclErrorLBraceDerived1 () {
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
        buffer.append("ClassDeclErrorLBraceDerived1(\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclErrorLBraceDerived1]");
        return buffer.toString();
    }
}
