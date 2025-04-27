// generated with ast extension for cup
// version 0.8
// 27/3/2025 19:22:29


package rs.ac.bg.etf.pp1.ast;

public class DesignatorListDerived1 extends DesignatorList {

    private DesignatorList DesignatorList;
    private DesignatorListElem DesignatorListElem;

    public DesignatorListDerived1 (DesignatorList DesignatorList, DesignatorListElem DesignatorListElem) {
        this.DesignatorList=DesignatorList;
        if(DesignatorList!=null) DesignatorList.setParent(this);
        this.DesignatorListElem=DesignatorListElem;
        if(DesignatorListElem!=null) DesignatorListElem.setParent(this);
    }

    public DesignatorList getDesignatorList() {
        return DesignatorList;
    }

    public void setDesignatorList(DesignatorList DesignatorList) {
        this.DesignatorList=DesignatorList;
    }

    public DesignatorListElem getDesignatorListElem() {
        return DesignatorListElem;
    }

    public void setDesignatorListElem(DesignatorListElem DesignatorListElem) {
        this.DesignatorListElem=DesignatorListElem;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorList!=null) DesignatorList.accept(visitor);
        if(DesignatorListElem!=null) DesignatorListElem.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorList!=null) DesignatorList.traverseTopDown(visitor);
        if(DesignatorListElem!=null) DesignatorListElem.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorList!=null) DesignatorList.traverseBottomUp(visitor);
        if(DesignatorListElem!=null) DesignatorListElem.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorListDerived1(\n");

        if(DesignatorList!=null)
            buffer.append(DesignatorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorListElem!=null)
            buffer.append(DesignatorListElem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorListDerived1]");
        return buffer.toString();
    }
}
