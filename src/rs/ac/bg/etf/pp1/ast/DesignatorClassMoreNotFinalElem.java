// generated with ast extension for cup
// version 0.8
// 24/5/2025 0:36:5


package rs.ac.bg.etf.pp1.ast;

public class DesignatorClassMoreNotFinalElem extends DesignatorClassMore {

    private DesignatorClassMore DesignatorClassMore;
    private DesignatorClassArrayName DesignatorClassArrayName;
    private Expr Expr;

    public DesignatorClassMoreNotFinalElem (DesignatorClassMore DesignatorClassMore, DesignatorClassArrayName DesignatorClassArrayName, Expr Expr) {
        this.DesignatorClassMore=DesignatorClassMore;
        if(DesignatorClassMore!=null) DesignatorClassMore.setParent(this);
        this.DesignatorClassArrayName=DesignatorClassArrayName;
        if(DesignatorClassArrayName!=null) DesignatorClassArrayName.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public DesignatorClassMore getDesignatorClassMore() {
        return DesignatorClassMore;
    }

    public void setDesignatorClassMore(DesignatorClassMore DesignatorClassMore) {
        this.DesignatorClassMore=DesignatorClassMore;
    }

    public DesignatorClassArrayName getDesignatorClassArrayName() {
        return DesignatorClassArrayName;
    }

    public void setDesignatorClassArrayName(DesignatorClassArrayName DesignatorClassArrayName) {
        this.DesignatorClassArrayName=DesignatorClassArrayName;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorClassMore!=null) DesignatorClassMore.accept(visitor);
        if(DesignatorClassArrayName!=null) DesignatorClassArrayName.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorClassMore!=null) DesignatorClassMore.traverseTopDown(visitor);
        if(DesignatorClassArrayName!=null) DesignatorClassArrayName.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorClassMore!=null) DesignatorClassMore.traverseBottomUp(visitor);
        if(DesignatorClassArrayName!=null) DesignatorClassArrayName.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorClassMoreNotFinalElem(\n");

        if(DesignatorClassMore!=null)
            buffer.append(DesignatorClassMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorClassArrayName!=null)
            buffer.append(DesignatorClassArrayName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorClassMoreNotFinalElem]");
        return buffer.toString();
    }
}
