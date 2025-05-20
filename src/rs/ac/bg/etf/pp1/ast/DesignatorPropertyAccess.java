// generated with ast extension for cup
// version 0.8
// 20/4/2025 10:45:31


package rs.ac.bg.etf.pp1.ast;

public class DesignatorPropertyAccess extends Designator {

    private DesignatorClassName DesignatorClassName;
    private DesignatorClassMore DesignatorClassMore;

    public DesignatorPropertyAccess (DesignatorClassName DesignatorClassName, DesignatorClassMore DesignatorClassMore) {
        this.DesignatorClassName=DesignatorClassName;
        if(DesignatorClassName!=null) DesignatorClassName.setParent(this);
        this.DesignatorClassMore=DesignatorClassMore;
        if(DesignatorClassMore!=null) DesignatorClassMore.setParent(this);
    }

    public DesignatorClassName getDesignatorClassName() {
        return DesignatorClassName;
    }

    public void setDesignatorClassName(DesignatorClassName DesignatorClassName) {
        this.DesignatorClassName=DesignatorClassName;
    }

    public DesignatorClassMore getDesignatorClassMore() {
        return DesignatorClassMore;
    }

    public void setDesignatorClassMore(DesignatorClassMore DesignatorClassMore) {
        this.DesignatorClassMore=DesignatorClassMore;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorClassName!=null) DesignatorClassName.accept(visitor);
        if(DesignatorClassMore!=null) DesignatorClassMore.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorClassName!=null) DesignatorClassName.traverseTopDown(visitor);
        if(DesignatorClassMore!=null) DesignatorClassMore.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorClassName!=null) DesignatorClassName.traverseBottomUp(visitor);
        if(DesignatorClassMore!=null) DesignatorClassMore.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorPropertyAccess(\n");

        if(DesignatorClassName!=null)
            buffer.append(DesignatorClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorClassMore!=null)
            buffer.append(DesignatorClassMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorPropertyAccess]");
        return buffer.toString();
    }
}
