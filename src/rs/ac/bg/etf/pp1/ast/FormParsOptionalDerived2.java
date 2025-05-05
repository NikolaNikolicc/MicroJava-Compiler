// generated with ast extension for cup
// version 0.8
// 5/4/2025 22:57:32


package rs.ac.bg.etf.pp1.ast;

public class FormParsOptionalDerived2 extends FormParsOptional {

    private MethodSignatureEndFormPars MethodSignatureEndFormPars;

    public FormParsOptionalDerived2 (MethodSignatureEndFormPars MethodSignatureEndFormPars) {
        this.MethodSignatureEndFormPars=MethodSignatureEndFormPars;
        if(MethodSignatureEndFormPars!=null) MethodSignatureEndFormPars.setParent(this);
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
        if(MethodSignatureEndFormPars!=null) MethodSignatureEndFormPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodSignatureEndFormPars!=null) MethodSignatureEndFormPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodSignatureEndFormPars!=null) MethodSignatureEndFormPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsOptionalDerived2(\n");

        if(MethodSignatureEndFormPars!=null)
            buffer.append(MethodSignatureEndFormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsOptionalDerived2]");
        return buffer.toString();
    }
}
