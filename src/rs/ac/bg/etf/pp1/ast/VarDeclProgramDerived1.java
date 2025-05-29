// generated with ast extension for cup
// version 0.8
// 30/4/2025 1:47:6


package rs.ac.bg.etf.pp1.ast;

public class VarDeclProgramDerived1 extends VarDeclProgram {

    private VarDeclErrorSemi VarDeclErrorSemi;

    public VarDeclProgramDerived1 (VarDeclErrorSemi VarDeclErrorSemi) {
        this.VarDeclErrorSemi=VarDeclErrorSemi;
        if(VarDeclErrorSemi!=null) VarDeclErrorSemi.setParent(this);
    }

    public VarDeclErrorSemi getVarDeclErrorSemi() {
        return VarDeclErrorSemi;
    }

    public void setVarDeclErrorSemi(VarDeclErrorSemi VarDeclErrorSemi) {
        this.VarDeclErrorSemi=VarDeclErrorSemi;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclErrorSemi!=null) VarDeclErrorSemi.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclErrorSemi!=null) VarDeclErrorSemi.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclErrorSemi!=null) VarDeclErrorSemi.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclProgramDerived1(\n");

        if(VarDeclErrorSemi!=null)
            buffer.append(VarDeclErrorSemi.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclProgramDerived1]");
        return buffer.toString();
    }
}
