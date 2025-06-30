// generated with ast extension for cup
// version 0.8
// 30/5/2025 11:27:22


package rs.ac.bg.etf.pp1.ast;

public class ClassNoExtend extends ClassDecl {

    private ClassDeclName ClassDeclName;
    private VarDeclListOptional VarDeclListOptional;

    public ClassNoExtend (ClassDeclName ClassDeclName, VarDeclListOptional VarDeclListOptional) {
        this.ClassDeclName=ClassDeclName;
        if(ClassDeclName!=null) ClassDeclName.setParent(this);
        this.VarDeclListOptional=VarDeclListOptional;
        if(VarDeclListOptional!=null) VarDeclListOptional.setParent(this);
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassDeclName!=null) ClassDeclName.accept(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassDeclName!=null) ClassDeclName.traverseTopDown(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassDeclName!=null) ClassDeclName.traverseBottomUp(visitor);
        if(VarDeclListOptional!=null) VarDeclListOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassNoExtend(\n");

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

        buffer.append(tab);
        buffer.append(") [ClassNoExtend]");
        return buffer.toString();
    }
}
