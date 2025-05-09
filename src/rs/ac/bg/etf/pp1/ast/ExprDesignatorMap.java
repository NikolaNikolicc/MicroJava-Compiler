// generated with ast extension for cup
// version 0.8
// 9/4/2025 10:33:16


package rs.ac.bg.etf.pp1.ast;

public class ExprDesignatorMap extends Expr {

    private Designator Designator;
    private MapDesignator MapDesignator;

    public ExprDesignatorMap (Designator Designator, MapDesignator MapDesignator) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.MapDesignator=MapDesignator;
        if(MapDesignator!=null) MapDesignator.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public MapDesignator getMapDesignator() {
        return MapDesignator;
    }

    public void setMapDesignator(MapDesignator MapDesignator) {
        this.MapDesignator=MapDesignator;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(MapDesignator!=null) MapDesignator.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(MapDesignator!=null) MapDesignator.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(MapDesignator!=null) MapDesignator.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprDesignatorMap(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MapDesignator!=null)
            buffer.append(MapDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprDesignatorMap]");
        return buffer.toString();
    }
}
