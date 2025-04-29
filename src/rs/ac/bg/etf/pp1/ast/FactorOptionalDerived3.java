// generated with ast extension for cup
// version 0.8
// 29/3/2025 15:15:59


package rs.ac.bg.etf.pp1.ast;

public class FactorOptionalDerived3 extends FactorOptional {

    public FactorOptionalDerived3 () {
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
        buffer.append("FactorOptionalDerived3(\n");

        buffer.append(tab);
        buffer.append(") [FactorOptionalDerived3]");
        return buffer.toString();
    }
}
