// generated with ast extension for cup
// version 0.8
// 6/4/2025 7:41:15


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclDerived3 extends ClassDecl {

    private String I1;
    private ClassDeclErrorLBrace ClassDeclErrorLBrace;
    private VarDeclListOptional VarDeclListOptional;

    public ClassDeclDerived3 (String I1, ClassDeclErrorLBrace ClassDeclErrorLBrace, VarDeclListOptional VarDeclListOptional) {
        this.I1=I1;
        this.ClassDeclErrorLBrace=ClassDeclErrorLBrace;
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.setParent(this);
        this.VarDeclListOptional=VarDeclListOptional;
        if(VarDeclListOptional!=null) VarDeclListOptional.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public ClassDeclErrorLBrace getClassDeclErrorLBrace() {
        return ClassDeclErrorLBrace;
    }

    public void setClassDeclErrorLBrace(ClassDeclErrorLBrace ClassDeclErrorLBrace) {
        this.ClassDeclErrorLBrace=ClassDeclErrorLBrace;
    }

    public VarDeclListOptional getVarDeclListOptional() {
        return VarDeclListOptional;
    }

    public void setVarDeclListOptional(VarDeclListOptional VarDeclListOptional) {
        this.VarDeclListOptional=VarDeclListOptional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.accept(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.traverseTopDown(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.traverseBottomUp(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclDerived3(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(ClassDeclErrorLBrace!=null)
            buffer.append(ClassDeclErrorLBrace.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclListOptional!=null)
            buffer.append(VarDeclListOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclDerived3]");
        return buffer.toString();
    }
}
