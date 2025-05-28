// generated with ast extension for cup
// version 0.8
// 28/4/2025 15:24:7


package rs.ac.bg.etf.pp1.ast;

public class ClassYesExtend extends ClassDecl {

    private ClassDeclName ClassDeclName;
    private ExtendsClass ExtendsClass;
    private VarDeclListOptional VarDeclListOptional;
    private CopyParentMethods CopyParentMethods;

    public ClassYesExtend (ClassDeclName ClassDeclName, ExtendsClass ExtendsClass, VarDeclListOptional VarDeclListOptional, CopyParentMethods CopyParentMethods) {
        this.ClassDeclName=ClassDeclName;
        if(ClassDeclName!=null) ClassDeclName.setParent(this);
        this.ExtendsClass=ExtendsClass;
        if(ExtendsClass!=null) ExtendsClass.setParent(this);
        this.VarDeclListOptional=VarDeclListOptional;
        if(VarDeclListOptional!=null) VarDeclListOptional.setParent(this);
        this.CopyParentMethods=CopyParentMethods;
        if(CopyParentMethods!=null) CopyParentMethods.setParent(this);
    }

    public ClassDeclName getClassDeclName() {
        return ClassDeclName;
    }

    public void setClassDeclName(ClassDeclName ClassDeclName) {
        this.ClassDeclName=ClassDeclName;
    }

    public ExtendsClass getExtendsClass() {
        return ExtendsClass;
    }

    public void setExtendsClass(ExtendsClass ExtendsClass) {
        this.ExtendsClass=ExtendsClass;
    }

    public VarDeclListOptional getVarDeclListOptional() {
        return VarDeclListOptional;
    }

    public void setVarDeclListOptional(VarDeclListOptional VarDeclListOptional) {
        this.VarDeclListOptional=VarDeclListOptional;
    }

    public CopyParentMethods getCopyParentMethods() {
        return CopyParentMethods;
    }

    public void setCopyParentMethods(CopyParentMethods CopyParentMethods) {
        this.CopyParentMethods=CopyParentMethods;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassDeclName!=null) ClassDeclName.accept(visitor);
        if(ExtendsClass!=null) ExtendsClass.accept(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.accept(visitor);
        if(CopyParentMethods!=null) CopyParentMethods.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassDeclName!=null) ClassDeclName.traverseTopDown(visitor);
        if(ExtendsClass!=null) ExtendsClass.traverseTopDown(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseTopDown(visitor);
        if(CopyParentMethods!=null) CopyParentMethods.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassDeclName!=null) ClassDeclName.traverseBottomUp(visitor);
        if(ExtendsClass!=null) ExtendsClass.traverseBottomUp(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseBottomUp(visitor);
        if(CopyParentMethods!=null) CopyParentMethods.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassYesExtend(\n");

        if(ClassDeclName!=null)
            buffer.append(ClassDeclName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExtendsClass!=null)
            buffer.append(ExtendsClass.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclListOptional!=null)
            buffer.append(VarDeclListOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CopyParentMethods!=null)
            buffer.append(CopyParentMethods.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassYesExtend]");
        return buffer.toString();
    }
}
