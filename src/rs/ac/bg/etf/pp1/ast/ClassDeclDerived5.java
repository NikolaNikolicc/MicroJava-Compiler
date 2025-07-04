// generated with ast extension for cup
// version 0.8
// 4/6/2025 20:54:52


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclDerived5 extends ClassDecl {

    private ClassDeclName ClassDeclName;
    private ClassDeclErrorLBrace ClassDeclErrorLBrace;
    private VarDeclListOptional VarDeclListOptional;
    private ClassDeclErrorLBrace ClassDeclErrorLBrace1;
    private ClassMethodDeclListStart ClassMethodDeclListStart;
    private MethodDeclList MethodDeclList;

    public ClassDeclDerived5 (ClassDeclName ClassDeclName, ClassDeclErrorLBrace ClassDeclErrorLBrace, VarDeclListOptional VarDeclListOptional, ClassDeclErrorLBrace ClassDeclErrorLBrace1, ClassMethodDeclListStart ClassMethodDeclListStart, MethodDeclList MethodDeclList) {
        this.ClassDeclName=ClassDeclName;
        if(ClassDeclName!=null) ClassDeclName.setParent(this);
        this.ClassDeclErrorLBrace=ClassDeclErrorLBrace;
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.setParent(this);
        this.VarDeclListOptional=VarDeclListOptional;
        if(VarDeclListOptional!=null) VarDeclListOptional.setParent(this);
        this.ClassDeclErrorLBrace1=ClassDeclErrorLBrace1;
        if(ClassDeclErrorLBrace1!=null) ClassDeclErrorLBrace1.setParent(this);
        this.ClassMethodDeclListStart=ClassMethodDeclListStart;
        if(ClassMethodDeclListStart!=null) ClassMethodDeclListStart.setParent(this);
        this.MethodDeclList=MethodDeclList;
        if(MethodDeclList!=null) MethodDeclList.setParent(this);
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

    public ClassDeclErrorLBrace getClassDeclErrorLBrace1() {
        return ClassDeclErrorLBrace1;
    }

    public void setClassDeclErrorLBrace1(ClassDeclErrorLBrace ClassDeclErrorLBrace1) {
        this.ClassDeclErrorLBrace1=ClassDeclErrorLBrace1;
    }

    public ClassMethodDeclListStart getClassMethodDeclListStart() {
        return ClassMethodDeclListStart;
    }

    public void setClassMethodDeclListStart(ClassMethodDeclListStart ClassMethodDeclListStart) {
        this.ClassMethodDeclListStart=ClassMethodDeclListStart;
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
        if(ClassDeclName!=null) ClassDeclName.accept(visitor);
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.accept(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.accept(visitor);
        if(ClassDeclErrorLBrace1!=null) ClassDeclErrorLBrace1.accept(visitor);
        if(ClassMethodDeclListStart!=null) ClassMethodDeclListStart.accept(visitor);
        if(MethodDeclList!=null) MethodDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassDeclName!=null) ClassDeclName.traverseTopDown(visitor);
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.traverseTopDown(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseTopDown(visitor);
        if(ClassDeclErrorLBrace1!=null) ClassDeclErrorLBrace1.traverseTopDown(visitor);
        if(ClassMethodDeclListStart!=null) ClassMethodDeclListStart.traverseTopDown(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassDeclName!=null) ClassDeclName.traverseBottomUp(visitor);
        if(ClassDeclErrorLBrace!=null) ClassDeclErrorLBrace.traverseBottomUp(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseBottomUp(visitor);
        if(ClassDeclErrorLBrace1!=null) ClassDeclErrorLBrace1.traverseBottomUp(visitor);
        if(ClassMethodDeclListStart!=null) ClassMethodDeclListStart.traverseBottomUp(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclDerived5(\n");

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

        if(ClassDeclErrorLBrace1!=null)
            buffer.append(ClassDeclErrorLBrace1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassMethodDeclListStart!=null)
            buffer.append(ClassMethodDeclListStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclList!=null)
            buffer.append(MethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclDerived5]");
        return buffer.toString();
    }
}
