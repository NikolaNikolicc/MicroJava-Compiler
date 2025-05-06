// generated with ast extension for cup
// version 0.8
// 6/4/2025 16:0:25


package rs.ac.bg.etf.pp1.ast;

public class DesignatorFuncCallDerived1 extends DesignatorFuncCall {

    public DesignatorFuncCallDerived1 () {
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
        buffer.append("DesignatorFuncCallDerived1(\n");

        buffer.append(tab);
        buffer.append(") [DesignatorFuncCallDerived1]");
        return buffer.toString();
    }
}
