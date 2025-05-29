// generated with ast extension for cup
// version 0.8
// 29/4/2025 14:30:56


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclDerived1 extends ClassDecl {

    private ClassDeclName ClassDeclName;
    private ClassDeclErrorLBrace ClassDeclErrorLBrace;
    private VarDeclListOptional VarDeclListOptional;

    public ClassDeclDerived1 (ClassDeclName ClassDeclName, ClassDeclErrorLBrace ClassDeclErrorLBrace, VarDeclListOptional VarDeclListOptional) {
        this.ClassDeclName=ClassDeclName;
        if(ClassDeclName!=null) ClassDeclName.setParent(this);
        this.ClassDeclErrorLBrace=ClassDeclErrorLBrace;
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.setParent(this);
        this.VarDeclListOptional=VarDeclListOptional;
        if(VarDeclListOptional!=null) VarDeclListOptional.setParent(this);
    }

    public ClassDeclName getClassDeclName() {
        return ClassDeclName;
    }

    public void setClassDeclName(ClassDeclName ClassDeclName) {
        this.ClassDeclName=ClassDeclName;
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
        if(ClassDeclName!=null) ClassDeclName.accept(visitor);
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.accept(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassDeclName!=null) ClassDeclName.traverseTopDown(visitor);
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.traverseTopDown(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassDeclName!=null) ClassDeclName.traverseBottomUp(visitor);
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.traverseBottomUp(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclDerived1(\n");

        if(ClassDeclName!=null)
            buffer.append(ClassDeclName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
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
        buffer.append(") [ClassDeclDerived1]");
        return buffer.toString();
    }
}
