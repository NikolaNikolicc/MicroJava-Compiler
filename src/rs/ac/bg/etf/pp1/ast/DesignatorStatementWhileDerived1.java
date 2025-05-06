// generated with ast extension for cup
// version 0.8
// 6/4/2025 7:9:34


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementWhileDerived1 extends DesignatorStatementWhile {

    private DesignatorAssignWhile DesignatorAssignWhile;

    public DesignatorStatementWhileDerived1 (DesignatorAssignWhile DesignatorAssignWhile) {
        this.DesignatorAssignWhile=DesignatorAssignWhile;
        if(DesignatorAssignWhile!=null) DesignatorAssignWhile.setParent(this);
    }

    public DesignatorAssignWhile getDesignatorAssignWhile() {
        return DesignatorAssignWhile;
    }

    public void setDesignatorAssignWhile(DesignatorAssignWhile DesignatorAssignWhile) {
        this.DesignatorAssignWhile=DesignatorAssignWhile;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorAssignWhile!=null) DesignatorAssignWhile.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorAssignWhile!=null) DesignatorAssignWhile.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorAssignWhile!=null) DesignatorAssignWhile.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementWhileDerived1(\n");

        if(DesignatorAssignWhile!=null)
            buffer.append(DesignatorAssignWhile.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementWhileDerived1]");
        return buffer.toString();
    }
}
