// generated with ast extension for cup
// version 0.8
// 4/4/2025 21:38:23


package rs.ac.bg.etf.pp1.ast;

public class FactorChoiceDerived3 extends FactorChoice {

    public FactorChoiceDerived3 () {
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
        buffer.append("FactorChoiceDerived3(\n");

        buffer.append(tab);
        buffer.append(") [FactorChoiceDerived3]");
        return buffer.toString();
    }
}
