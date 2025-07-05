// generated with ast extension for cup
// version 0.8
// 5/6/2025 0:36:48


package rs.ac.bg.etf.pp1.ast;

public class FormParsDerived3 extends FormPars {

    private Type Type;
    private VarDeclFinal VarDeclFinal;
    private FormPars FormPars;

    public FormParsDerived3 (Type Type, VarDeclFinal VarDeclFinal, FormPars FormPars) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarDeclFinal=VarDeclFinal;
        if(VarDeclFinal!=null) VarDeclFinal.setParent(this);
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
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
        if(Type!=null) Type.accept(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.accept(visitor);
        if(FormPars!=null) FormPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.traverseTopDown(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.traverseBottomUp(visitor);
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsDerived3(\n");

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

        if(FormPars!=null)
            buffer.append(FormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsDerived3]");
        return buffer.toString();
    }
}
