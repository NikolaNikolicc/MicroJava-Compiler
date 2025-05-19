// generated with ast extension for cup
// version 0.8
// 19/4/2025 19:3:44


package rs.ac.bg.etf.pp1.ast;

public class FactorConstDeclListValue extends Factor {

    private ConstDeclListValue ConstDeclListValue;

    public FactorConstDeclListValue (ConstDeclListValue ConstDeclListValue) {
        this.ConstDeclListValue=ConstDeclListValue;
        if(ConstDeclListValue!=null) ConstDeclListValue.setParent(this);
    }

    public ConstDeclListValue getConstDeclListValue() {
        return ConstDeclListValue;
    }

    public void setConstDeclListValue(ConstDeclListValue ConstDeclListValue) {
        this.ConstDeclListValue=ConstDeclListValue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclListValue!=null) ConstDeclListValue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclListValue!=null) ConstDeclListValue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclListValue!=null) ConstDeclListValue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorConstDeclListValue(\n");

        if(ConstDeclListValue!=null)
            buffer.append(ConstDeclListValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorConstDeclListValue]");
        return buffer.toString();
    }
}
