// generated with ast extension for cup
// version 0.8
// 5/4/2025 20:48:10


package rs.ac.bg.etf.pp1.ast;

public class ProgramDeclElemDerived2 extends ProgramDeclElem {

    private VarDeclProgram VarDeclProgram;

    public ProgramDeclElemDerived2 (VarDeclProgram VarDeclProgram) {
        this.VarDeclProgram=VarDeclProgram;
        if(VarDeclProgram!=null) VarDeclProgram.setParent(this);
    }

    public VarDeclProgram getVarDeclProgram() {
        return VarDeclProgram;
    }

    public void setVarDeclProgram(VarDeclProgram VarDeclProgram) {
        this.VarDeclProgram=VarDeclProgram;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclProgram!=null) VarDeclProgram.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclProgram!=null) VarDeclProgram.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclProgram!=null) VarDeclProgram.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ProgramDeclElemDerived2(\n");

        if(VarDeclProgram!=null)
            buffer.append(VarDeclProgram.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgramDeclElemDerived2]");
        return buffer.toString();
    }
}
