// generated with ast extension for cup
// version 0.8
// 27/4/2025 11:8:20


package rs.ac.bg.etf.pp1.ast;

public class MethodSignatureDerived1 extends MethodSignature {

    private TypeChoice TypeChoice;
    private MethodName MethodName;
    private MethodSignatureStartFormPars MethodSignatureStartFormPars;
    private FormParsOptional FormParsOptional;

    public MethodSignatureDerived1 (TypeChoice TypeChoice, MethodName MethodName, MethodSignatureStartFormPars MethodSignatureStartFormPars, FormParsOptional FormParsOptional) {
        this.TypeChoice=TypeChoice;
        if(TypeChoice!=null) TypeChoice.setParent(this);
        this.MethodName=MethodName;
        if(MethodName!=null) MethodName.setParent(this);
        this.MethodSignatureStartFormPars=MethodSignatureStartFormPars;
        if(MethodSignatureStartFormPars!=null) MethodSignatureStartFormPars.setParent(this);
        this.FormParsOptional=FormParsOptional;
        if(FormParsOptional!=null) FormParsOptional.setParent(this);
    }

    public TypeChoice getTypeChoice() {
        return TypeChoice;
    }

    public void setTypeChoice(TypeChoice TypeChoice) {
        this.TypeChoice=TypeChoice;
    }

    public MethodName getMethodName() {
        return MethodName;
    }

    public void setMethodName(MethodName MethodName) {
        this.MethodName=MethodName;
    }

    public MethodSignatureStartFormPars getMethodSignatureStartFormPars() {
        return MethodSignatureStartFormPars;
    }

    public void setMethodSignatureStartFormPars(MethodSignatureStartFormPars MethodSignatureStartFormPars) {
        this.MethodSignatureStartFormPars=MethodSignatureStartFormPars;
    }

    public FormParsOptional getFormParsOptional() {
        return FormParsOptional;
    }

    public void setFormParsOptional(FormParsOptional FormParsOptional) {
        this.FormParsOptional=FormParsOptional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(TypeChoice!=null) TypeChoice.accept(visitor);
        if(MethodName!=null) MethodName.accept(visitor);
        if(MethodSignatureStartFormPars!=null) MethodSignatureStartFormPars.accept(visitor);
        if(FormParsOptional!=null) FormParsOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(TypeChoice!=null) TypeChoice.traverseTopDown(visitor);
        if(MethodName!=null) MethodName.traverseTopDown(visitor);
        if(MethodSignatureStartFormPars!=null) MethodSignatureStartFormPars.traverseTopDown(visitor);
        if(FormParsOptional!=null) FormParsOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(TypeChoice!=null) TypeChoice.traverseBottomUp(visitor);
        if(MethodName!=null) MethodName.traverseBottomUp(visitor);
        if(MethodSignatureStartFormPars!=null) MethodSignatureStartFormPars.traverseBottomUp(visitor);
        if(FormParsOptional!=null) FormParsOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodSignatureDerived1(\n");

        if(TypeChoice!=null)
            buffer.append(TypeChoice.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodName!=null)
            buffer.append(MethodName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodSignatureStartFormPars!=null)
            buffer.append(MethodSignatureStartFormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsOptional!=null)
            buffer.append(FormParsOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodSignatureDerived1]");
        return buffer.toString();
    }
}
