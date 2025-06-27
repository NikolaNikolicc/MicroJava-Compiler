// generated with ast extension for cup
// version 0.8
// 27/5/2025 10:42:15


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclListDerived2 extends ConstDeclList {

    private ConstDeclAssign ConstDeclAssign;

    public ConstDeclListDerived2 (ConstDeclAssign ConstDeclAssign) {
        this.ConstDeclAssign=ConstDeclAssign;
        if(ConstDeclAssign!=null) ConstDeclAssign.setParent(this);
    }

    public ConstDeclAssign getConstDeclAssign() {
        return ConstDeclAssign;
    }

    public void setConstDeclAssign(ConstDeclAssign ConstDeclAssign) {
        this.ConstDeclAssign=ConstDeclAssign;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclAssign!=null) ConstDeclAssign.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclAssign!=null) ConstDeclAssign.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclAssign!=null) ConstDeclAssign.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclListDerived2(\n");

        if(ConstDeclAssign!=null)
            buffer.append(ConstDeclAssign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclListDerived2]");
        return buffer.toString();
    }
}
