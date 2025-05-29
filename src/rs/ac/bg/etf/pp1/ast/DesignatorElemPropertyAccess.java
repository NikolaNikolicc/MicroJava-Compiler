// generated with ast extension for cup
// version 0.8
// 29/4/2025 14:30:57


package rs.ac.bg.etf.pp1.ast;

public class DesignatorElemPropertyAccess extends Designator {

    private DesignatorClassElem DesignatorClassElem;
    private DesignatorClassMore DesignatorClassMore;

    public DesignatorElemPropertyAccess (DesignatorClassElem DesignatorClassElem, DesignatorClassMore DesignatorClassMore) {
        this.DesignatorClassElem=DesignatorClassElem;
        if(DesignatorClassElem!=null) DesignatorClassElem.setParent(this);
        this.DesignatorClassMore=DesignatorClassMore;
        if(DesignatorClassMore!=null) DesignatorClassMore.setParent(this);
    }

    public DesignatorClassElem getDesignatorClassElem() {
        return DesignatorClassElem;
    }

    public void setDesignatorClassElem(DesignatorClassElem DesignatorClassElem) {
        this.DesignatorClassElem=DesignatorClassElem;
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
        if(DesignatorClassElem!=null) DesignatorClassElem.accept(visitor);
        if(DesignatorClassMore!=null) DesignatorClassMore.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorClassElem!=null) DesignatorClassElem.traverseTopDown(visitor);
        if(DesignatorClassMore!=null) DesignatorClassMore.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorClassElem!=null) DesignatorClassElem.traverseBottomUp(visitor);
        if(DesignatorClassMore!=null) DesignatorClassMore.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorElemPropertyAccess(\n");

        if(DesignatorClassElem!=null)
            buffer.append(DesignatorClassElem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorClassMore!=null)
            buffer.append(DesignatorClassMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorElemPropertyAccess]");
        return buffer.toString();
    }
}
