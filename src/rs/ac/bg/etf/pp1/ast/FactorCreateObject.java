// generated with ast extension for cup
// version 0.8
// 27/5/2025 10:42:15


package rs.ac.bg.etf.pp1.ast;

public class FactorCreateObject extends Factor {

    private Type Type;
    private DesignatorFuncCall DesignatorFuncCall;

    public FactorCreateObject (Type Type, DesignatorFuncCall DesignatorFuncCall) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.DesignatorFuncCall=DesignatorFuncCall;
        if(DesignatorFuncCall!=null) DesignatorFuncCall.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public DesignatorFuncCall getDesignatorFuncCall() {
        return DesignatorFuncCall;
    }

    public void setDesignatorFuncCall(DesignatorFuncCall DesignatorFuncCall) {
        this.DesignatorFuncCall=DesignatorFuncCall;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(DesignatorFuncCall!=null) DesignatorFuncCall.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(DesignatorFuncCall!=null) DesignatorFuncCall.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(DesignatorFuncCall!=null) DesignatorFuncCall.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorCreateObject(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorFuncCall!=null)
            buffer.append(DesignatorFuncCall.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorCreateObject]");
        return buffer.toString();
    }
}
