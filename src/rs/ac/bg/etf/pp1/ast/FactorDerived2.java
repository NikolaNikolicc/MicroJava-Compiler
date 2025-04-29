// generated with ast extension for cup
// version 0.8
// 30/3/2025 0:29:55


package rs.ac.bg.etf.pp1.ast;

public class FactorDerived2 extends Factor {

    private Type Type;
    private FactorChoice FactorChoice;

    public FactorDerived2 (Type Type, FactorChoice FactorChoice) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.FactorChoice=FactorChoice;
        if(FactorChoice!=null) FactorChoice.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public FactorChoice getFactorChoice() {
        return FactorChoice;
    }

    public void setFactorChoice(FactorChoice FactorChoice) {
        this.FactorChoice=FactorChoice;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(FactorChoice!=null) FactorChoice.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(FactorChoice!=null) FactorChoice.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(FactorChoice!=null) FactorChoice.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorDerived2(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FactorChoice!=null)
            buffer.append(FactorChoice.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorDerived2]");
        return buffer.toString();
    }
}
