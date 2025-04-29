// generated with ast extension for cup
// version 0.8
// 30/3/2025 0:10:35


package rs.ac.bg.etf.pp1.ast;

public class FormParsDerived1 extends FormPars {

    private Type Type;
    private VarDeclFinal VarDeclFinal;
    private MethodSignatureEndFormPars MethodSignatureEndFormPars;

    public FormParsDerived1 (Type Type, VarDeclFinal VarDeclFinal, MethodSignatureEndFormPars MethodSignatureEndFormPars) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarDeclFinal=VarDeclFinal;
        if(VarDeclFinal!=null) VarDeclFinal.setParent(this);
        this.MethodSignatureEndFormPars=MethodSignatureEndFormPars;
        if(MethodSignatureEndFormPars!=null) MethodSignatureEndFormPars.setParent(this);
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

    public MethodSignatureEndFormPars getMethodSignatureEndFormPars() {
        return MethodSignatureEndFormPars;
    }

    public void setMethodSignatureEndFormPars(MethodSignatureEndFormPars MethodSignatureEndFormPars) {
        this.MethodSignatureEndFormPars=MethodSignatureEndFormPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.accept(visitor);
        if(MethodSignatureEndFormPars!=null) MethodSignatureEndFormPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.traverseTopDown(visitor);
        if(MethodSignatureEndFormPars!=null) MethodSignatureEndFormPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDeclFinal!=null) VarDeclFinal.traverseBottomUp(visitor);
        if(MethodSignatureEndFormPars!=null) MethodSignatureEndFormPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsDerived1(\n");

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

        if(MethodSignatureEndFormPars!=null)
            buffer.append(MethodSignatureEndFormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsDerived1]");
        return buffer.toString();
    }
}
