// generated with ast extension for cup
// version 0.8
// 4/4/2025 22:6:6


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclAssign implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String I1;
    private ConstDeclListValue ConstDeclListValue;

    public ConstDeclAssign (String I1, ConstDeclListValue ConstDeclListValue) {
        this.I1=I1;
        this.ConstDeclListValue=ConstDeclListValue;
        if(ConstDeclListValue!=null) ConstDeclListValue.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public ConstDeclListValue getConstDeclListValue() {
        return ConstDeclListValue;
    }

    public void setConstDeclListValue(ConstDeclListValue ConstDeclListValue) {
        this.ConstDeclListValue=ConstDeclListValue;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
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
        buffer.append("ConstDeclAssign(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(ConstDeclListValue!=null)
            buffer.append(ConstDeclListValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclAssign]");
        return buffer.toString();
    }
}
