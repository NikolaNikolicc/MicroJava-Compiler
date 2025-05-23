// generated with ast extension for cup
// version 0.8
// 23/4/2025 11:45:36


package rs.ac.bg.etf.pp1.ast;

public class ClassNoExtendYesMethods extends ClassDecl {

    private ClassDeclName ClassDeclName;
    private VarDeclListOptional VarDeclListOptional;
    private ClassMethodDeclListStart ClassMethodDeclListStart;
    private MethodDeclList MethodDeclList;

    public ClassNoExtendYesMethods (ClassDeclName ClassDeclName, VarDeclListOptional VarDeclListOptional, ClassMethodDeclListStart ClassMethodDeclListStart, MethodDeclList MethodDeclList) {
        this.ClassDeclName=ClassDeclName;
        if(ClassDeclName!=null) ClassDeclName.setParent(this);
        this.VarDeclListOptional=VarDeclListOptional;
        if(VarDeclListOptional!=null) VarDeclListOptional.setParent(this);
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

    public VarDeclListOptional getVarDeclListOptional() {
        return VarDeclListOptional;
    }

    public void setVarDeclListOptional(VarDeclListOptional VarDeclListOptional) {
        this.VarDeclListOptional=VarDeclListOptional;
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
        if(VarDeclListOptional!=null) VarDeclListOptional.accept(visitor);
        if(ClassMethodDeclListStart!=null) ClassMethodDeclListStart.accept(visitor);
        if(MethodDeclList!=null) MethodDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassDeclName!=null) ClassDeclName.traverseTopDown(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseTopDown(visitor);
        if(ClassMethodDeclListStart!=null) ClassMethodDeclListStart.traverseTopDown(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassDeclName!=null) ClassDeclName.traverseBottomUp(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseBottomUp(visitor);
        if(ClassMethodDeclListStart!=null) ClassMethodDeclListStart.traverseBottomUp(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassNoExtendYesMethods(\n");

        if(ClassDeclName!=null)
            buffer.append(ClassDeclName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclListOptional!=null)
            buffer.append(VarDeclListOptional.toString("  "+tab));
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
        buffer.append(") [ClassNoExtendYesMethods]");
        return buffer.toString();
    }
}
