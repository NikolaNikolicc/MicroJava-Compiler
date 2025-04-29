// generated with ast extension for cup
// version 0.8
// 29/3/2025 15:15:59


package rs.ac.bg.etf.pp1.ast;

public class FormParsDerived2 extends FormPars {

    private MethodSignatureEndFormPars MethodSignatureEndFormPars;

    public FormParsDerived2 (MethodSignatureEndFormPars MethodSignatureEndFormPars) {
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
        buffer.append("FormParsDerived2(\n");

        if(MethodSignatureEndFormPars!=null)
            buffer.append(MethodSignatureEndFormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsDerived2]");
        return buffer.toString();
    }
}
