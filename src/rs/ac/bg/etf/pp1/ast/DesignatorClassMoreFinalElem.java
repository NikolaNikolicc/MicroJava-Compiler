// generated with ast extension for cup
// version 0.8
// 27/5/2025 0:55:50


package rs.ac.bg.etf.pp1.ast;

public class DesignatorClassMoreFinalElem extends DesignatorClassMore {

    private DesignatorClassArrayName DesignatorClassArrayName;
    private Expr Expr;

    public DesignatorClassMoreFinalElem (DesignatorClassArrayName DesignatorClassArrayName, Expr Expr) {
        this.DesignatorClassArrayName=DesignatorClassArrayName;
        if(DesignatorClassArrayName!=null) DesignatorClassArrayName.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
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
        if(DesignatorClassArrayName!=null) DesignatorClassArrayName.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorClassArrayName!=null) DesignatorClassArrayName.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorClassArrayName!=null) DesignatorClassArrayName.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorClassMoreFinalElem(\n");

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
        buffer.append(") [DesignatorClassMoreFinalElem]");
        return buffer.toString();
    }
}
