// generated with ast extension for cup
// version 0.8
// 27/3/2025 13:14:53


package rs.ac.bg.etf.pp1.ast;

public class FactorListDerived2 extends FactorList {

    public FactorListDerived2 () {
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
        buffer.append("FactorListDerived2(\n");

        buffer.append(tab);
        buffer.append(") [FactorListDerived2]");
        return buffer.toString();
    }
}
