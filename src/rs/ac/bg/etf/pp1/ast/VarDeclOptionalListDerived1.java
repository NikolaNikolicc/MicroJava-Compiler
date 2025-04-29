// generated with ast extension for cup
// version 0.8
// 30/3/2025 0:29:55


package rs.ac.bg.etf.pp1.ast;

public class VarDeclOptionalListDerived1 extends VarDeclOptionalList {

    private VarDeclOptionalList VarDeclOptionalList;
    private VarDeclFinal VarDeclFinal;

    public VarDeclOptionalListDerived1 (VarDeclOptionalList VarDeclOptionalList, VarDeclFinal VarDeclFinal) {
        this.VarDeclOptionalList=VarDeclOptionalList;
        if(VarDeclOptionalList!=null) VarDeclOptionalList.setParent(this);
        this.VarDeclFinal=VarDeclFinal;
        if(VarDeclFinal!=null) VarDeclFinal.setParent(this);
    }

    public VarDeclOptionalList getVarDeclOptionalList() {
        return VarDeclOptionalList;
    }

    public void setVarDeclOptionalList(VarDeclOptionalList VarDeclOptionalList) {
        this.VarDeclOptionalList=VarDeclOptionalList;
    }

    public VarDeclFinal getVarDeclFinal() {
        return VarDeclFinal;
    }

    public void setVarDeclFinal(VarDeclFinal VarDeclFinal) {
        this.VarDeclFinal=VarDeclFinal;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclOptionalList!=null) VarDeclOptionalList.accept(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclOptionalList!=null) VarDeclOptionalList.traverseTopDown(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclOptionalList!=null) VarDeclOptionalList.traverseBottomUp(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclOptionalListDerived1(\n");

        if(VarDeclOptionalList!=null)
            buffer.append(VarDeclOptionalList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclFinal!=null)
            buffer.append(VarDeclFinal.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclOptionalListDerived1]");
        return buffer.toString();
    }
}
