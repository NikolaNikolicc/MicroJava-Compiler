// generated with ast extension for cup
// version 0.8
// 19/4/2025 19:3:44


package rs.ac.bg.etf.pp1.ast;

public class ClassYesExtendYesMethods extends ClassDecl {

    private ClassDeclName ClassDeclName;
    private Type Type;
    private VarDeclListOptional VarDeclListOptional;
    private MethodDeclList MethodDeclList;

    public ClassYesExtendYesMethods (ClassDeclName ClassDeclName, Type Type, VarDeclListOptional VarDeclListOptional, MethodDeclList MethodDeclList) {
        this.ClassDeclName=ClassDeclName;
        if(ClassDeclName!=null) ClassDeclName.setParent(this);
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarDeclListOptional=VarDeclListOptional;
        if(VarDeclListOptional!=null) VarDeclListOptional.setParent(this);
        this.MethodDeclList=MethodDeclList;
        if(MethodDeclList!=null) MethodDeclList.setParent(this);
    }

    public ClassDeclName getClassDeclName() {
        return ClassDeclName;
    }

    public void setClassDeclName(ClassDeclName ClassDeclName) {
        this.ClassDeclName=ClassDeclName;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public VarDeclListOptional getVarDeclListOptional() {
        return VarDeclListOptional;
    }

    public void setVarDeclListOptional(VarDeclListOptional VarDeclListOptional) {
        this.VarDeclListOptional=VarDeclListOptional;
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
        if(Type!=null) Type.accept(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.accept(visitor);
        if(MethodDeclList!=null) MethodDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassDeclName!=null) ClassDeclName.traverseTopDown(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseTopDown(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassDeclName!=null) ClassDeclName.traverseBottomUp(visitor);
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseBottomUp(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassYesExtendYesMethods(\n");

        if(ClassDeclName!=null)
            buffer.append(ClassDeclName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclListOptional!=null)
            buffer.append(VarDeclListOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclList!=null)
            buffer.append(MethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassYesExtendYesMethods]");
        return buffer.toString();
    }
}
