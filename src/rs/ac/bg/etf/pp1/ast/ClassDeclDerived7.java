// generated with ast extension for cup
// version 0.8
// 6/4/2025 7:9:34


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclDerived7 extends ClassDecl {

    private String I1;
    private VarDeclListOptional VarDeclListOptional;
    private ClassDeclErrorLBrace ClassDeclErrorLBrace;
    private MethodDeclList MethodDeclList;

    public ClassDeclDerived7 (String I1, VarDeclListOptional VarDeclListOptional, ClassDeclErrorLBrace ClassDeclErrorLBrace, MethodDeclList MethodDeclList) {
        this.I1=I1;
        this.VarDeclListOptional=VarDeclListOptional;
        if(VarDeclListOptional!=null) VarDeclListOptional.setParent(this);
        this.ClassDeclErrorLBrace=ClassDeclErrorLBrace;
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.setParent(this);
        this.MethodDeclList=MethodDeclList;
        if(MethodDeclList!=null) MethodDeclList.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public VarDeclListOptional getVarDeclListOptional() {
        return VarDeclListOptional;
    }

    public void setVarDeclListOptional(VarDeclListOptional VarDeclListOptional) {
        this.VarDeclListOptional=VarDeclListOptional;
    }

    public ClassDeclErrorLBrace getClassDeclErrorLBrace() {
        return ClassDeclErrorLBrace;
    }

    public void setClassDeclErrorLBrace(ClassDeclErrorLBrace ClassDeclErrorLBrace) {
        this.ClassDeclErrorLBrace=ClassDeclErrorLBrace;
    }

    public MethodDeclList getMethodDeclList() {
        return MethodDeclList;
    }

    public void setMethodDeclList(MethodDeclList MethodDeclList) {
        this.MethodDeclList=MethodDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclListOptional!=null) VarDeclListOptional.accept(visitor);
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.accept(visitor);
        if(MethodDeclList!=null) MethodDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseTopDown(visitor);
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.traverseTopDown(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseBottomUp(visitor);
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.traverseBottomUp(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclDerived7(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(VarDeclListOptional!=null)
            buffer.append(VarDeclListOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassDeclErrorLBrace!=null)
            buffer.append(ClassDeclErrorLBrace.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclList!=null)
            buffer.append(MethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclDerived7]");
        return buffer.toString();
    }
}
