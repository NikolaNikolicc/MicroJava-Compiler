// generated with ast extension for cup
// version 0.8
// 20/4/2025 10:45:31


package rs.ac.bg.etf.pp1.ast;

public class VarDeclDerived1 extends VarDecl {

    private Type Type;
    private VarDeclFinal VarDeclFinal;
    private VarDeclOptionalList VarDeclOptionalList;

    public VarDeclDerived1 (Type Type, VarDeclFinal VarDeclFinal, VarDeclOptionalList VarDeclOptionalList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarDeclFinal=VarDeclFinal;
        if(VarDeclFinal!=null) VarDeclFinal.setParent(this);
        this.VarDeclOptionalList=VarDeclOptionalList;
        if(VarDeclOptionalList!=null) VarDeclOptionalList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public VarDeclFinal getVarDeclFinal() {
        return VarDeclFinal;
    }

    public void setVarDeclFinal(VarDeclFinal VarDeclFinal) {
        this.VarDeclFinal=VarDeclFinal;
    }

    public VarDeclOptionalList getVarDeclOptionalList() {
        return VarDeclOptionalList;
    }

    public void setVarDeclOptionalList(VarDeclOptionalList VarDeclOptionalList) {
        this.VarDeclOptionalList=VarDeclOptionalList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.accept(visitor);
        if(VarDeclOptionalList!=null) VarDeclOptionalList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.traverseTopDown(visitor);
        if(VarDeclOptionalList!=null) VarDeclOptionalList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.traverseBottomUp(visitor);
        if(VarDeclOptionalList!=null) VarDeclOptionalList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclDerived1(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclFinal!=null)
            buffer.append(VarDeclFinal.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclOptionalList!=null)
            buffer.append(VarDeclOptionalList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclDerived1]");
        return buffer.toString();
    }
}
