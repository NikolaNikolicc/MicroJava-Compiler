// generated with ast extension for cup
// version 0.8
// 5/4/2025 20:48:10


package rs.ac.bg.etf.pp1.ast;

public class VarDeclProgramDerived2 extends VarDeclProgram {

    private VarDeclErrorComma VarDeclErrorComma;
    private VarDeclFinal VarDeclFinal;
    private VarDeclOptionalList VarDeclOptionalList;

    public VarDeclProgramDerived2 (VarDeclErrorComma VarDeclErrorComma, VarDeclFinal VarDeclFinal, VarDeclOptionalList VarDeclOptionalList) {
        this.VarDeclErrorComma=VarDeclErrorComma;
        if(VarDeclErrorComma!=null) VarDeclErrorComma.setParent(this);
        this.VarDeclFinal=VarDeclFinal;
        if(VarDeclFinal!=null) VarDeclFinal.setParent(this);
        this.VarDeclOptionalList=VarDeclOptionalList;
        if(VarDeclOptionalList!=null) VarDeclOptionalList.setParent(this);
    }

    public VarDeclErrorComma getVarDeclErrorComma() {
        return VarDeclErrorComma;
    }

    public void setVarDeclErrorComma(VarDeclErrorComma VarDeclErrorComma) {
        this.VarDeclErrorComma=VarDeclErrorComma;
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
        if(VarDeclErrorComma!=null) VarDeclErrorComma.accept(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.accept(visitor);
        if(VarDeclOptionalList!=null) VarDeclOptionalList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclErrorComma!=null) VarDeclErrorComma.traverseTopDown(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.traverseTopDown(visitor);
        if(VarDeclOptionalList!=null) VarDeclOptionalList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclErrorComma!=null) VarDeclErrorComma.traverseBottomUp(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.traverseBottomUp(visitor);
        if(VarDeclOptionalList!=null) VarDeclOptionalList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclProgramDerived2(\n");

        if(VarDeclErrorComma!=null)
            buffer.append(VarDeclErrorComma.toString("  "+tab));
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
        buffer.append(") [VarDeclProgramDerived2]");
        return buffer.toString();
    }
}
