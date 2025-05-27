// generated with ast extension for cup
// version 0.8
// 27/4/2025 11:11:34


package rs.ac.bg.etf.pp1.ast;

public class InterfaceDeclOptionListDerived1 extends InterfaceDeclOptionList {

    private InterfaceDeclOptionList InterfaceDeclOptionList;
    private MethodDecl MethodDecl;

    public InterfaceDeclOptionListDerived1 (InterfaceDeclOptionList InterfaceDeclOptionList, MethodDecl MethodDecl) {
        this.InterfaceDeclOptionList=InterfaceDeclOptionList;
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.setParent(this);
        this.MethodDecl=MethodDecl;
        if(MethodDecl!=null) MethodDecl.setParent(this);
    }

    public InterfaceDeclOptionList getInterfaceDeclOptionList() {
        return InterfaceDeclOptionList;
    }

    public void setInterfaceDeclOptionList(InterfaceDeclOptionList InterfaceDeclOptionList) {
        this.InterfaceDeclOptionList=InterfaceDeclOptionList;
    }

    public MethodDecl getMethodDecl() {
        return MethodDecl;
    }

    public void setMethodDecl(MethodDecl MethodDecl) {
        this.MethodDecl=MethodDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.accept(visitor);
        if(MethodDecl!=null) MethodDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.traverseTopDown(visitor);
        if(MethodDecl!=null) MethodDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.traverseBottomUp(visitor);
        if(MethodDecl!=null) MethodDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InterfaceDeclOptionListDerived1(\n");

        if(InterfaceDeclOptionList!=null)
            buffer.append(InterfaceDeclOptionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDecl!=null)
            buffer.append(MethodDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceDeclOptionListDerived1]");
        return buffer.toString();
    }
}
