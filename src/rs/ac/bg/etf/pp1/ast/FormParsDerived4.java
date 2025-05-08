// generated with ast extension for cup
// version 0.8
// 8/4/2025 9:47:48


package rs.ac.bg.etf.pp1.ast;

public class FormParsDerived4 extends FormPars {

    private VarDeclErrorComma VarDeclErrorComma;
    private FormPars FormPars;

    public FormParsDerived4 (VarDeclErrorComma VarDeclErrorComma, FormPars FormPars) {
        this.VarDeclErrorComma=VarDeclErrorComma;
        if(VarDeclErrorComma!=null) VarDeclErrorComma.setParent(this);
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
    }

    public VarDeclErrorComma getVarDeclErrorComma() {
        return VarDeclErrorComma;
    }

    public void setVarDeclErrorComma(VarDeclErrorComma VarDeclErrorComma) {
        this.VarDeclErrorComma=VarDeclErrorComma;
    }

    public FormPars getFormPars() {
        return FormPars;
    }

    public void setFormPars(FormPars FormPars) {
        this.FormPars=FormPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclErrorComma!=null) VarDeclErrorComma.accept(visitor);
        if(FormPars!=null) FormPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclErrorComma!=null) VarDeclErrorComma.traverseTopDown(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclErrorComma!=null) VarDeclErrorComma.traverseBottomUp(visitor);
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsDerived4(\n");

        if(VarDeclErrorComma!=null)
            buffer.append(VarDeclErrorComma.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormPars!=null)
            buffer.append(FormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsDerived4]");
        return buffer.toString();
    }
}
