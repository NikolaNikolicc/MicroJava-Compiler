// generated with ast extension for cup
// version 0.8
// 5/4/2025 23:46:0


package rs.ac.bg.etf.pp1.ast;

public class VarDeclListOptionalDerived1 extends VarDeclListOptional {

    private VarDeclListOptional VarDeclListOptional;
    private VarDeclClass VarDeclClass;

    public VarDeclListOptionalDerived1 (VarDeclListOptional VarDeclListOptional, VarDeclClass VarDeclClass) {
        this.VarDeclListOptional=VarDeclListOptional;
        if(VarDeclListOptional!=null) VarDeclListOptional.setParent(this);
        this.VarDeclClass=VarDeclClass;
        if(VarDeclClass!=null) VarDeclClass.setParent(this);
    }

    public VarDeclListOptional getVarDeclListOptional() {
        return VarDeclListOptional;
    }

    public void setVarDeclListOptional(VarDeclListOptional VarDeclListOptional) {
        this.VarDeclListOptional=VarDeclListOptional;
    }

    public VarDeclClass getVarDeclClass() {
        return VarDeclClass;
    }

    public void setVarDeclClass(VarDeclClass VarDeclClass) {
        this.VarDeclClass=VarDeclClass;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclListOptional!=null) VarDeclListOptional.accept(visitor);
        if(VarDeclClass!=null) VarDeclClass.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseTopDown(visitor);
        if(VarDeclClass!=null) VarDeclClass.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseBottomUp(visitor);
        if(VarDeclClass!=null) VarDeclClass.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclListOptionalDerived1(\n");

        if(VarDeclListOptional!=null)
            buffer.append(VarDeclListOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclClass!=null)
            buffer.append(VarDeclClass.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListOptionalDerived1]");
        return buffer.toString();
    }
}
