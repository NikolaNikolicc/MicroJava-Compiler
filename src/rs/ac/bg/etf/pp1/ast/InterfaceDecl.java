// generated with ast extension for cup
// version 0.8
// 29/4/2025 19:26:57


package rs.ac.bg.etf.pp1.ast;

public class InterfaceDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private InterfaceDeclName InterfaceDeclName;
    private InterfaceDeclOptionList InterfaceDeclOptionList;

    public InterfaceDecl (InterfaceDeclName InterfaceDeclName, InterfaceDeclOptionList InterfaceDeclOptionList) {
        this.InterfaceDeclName=InterfaceDeclName;
        if(InterfaceDeclName!=null) InterfaceDeclName.setParent(this);
        this.InterfaceDeclOptionList=InterfaceDeclOptionList;
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.setParent(this);
    }

    public InterfaceDeclName getInterfaceDeclName() {
        return InterfaceDeclName;
    }

    public void setInterfaceDeclName(InterfaceDeclName InterfaceDeclName) {
        this.InterfaceDeclName=InterfaceDeclName;
    }

    public InterfaceDeclOptionList getInterfaceDeclOptionList() {
        return InterfaceDeclOptionList;
    }

    public void setInterfaceDeclOptionList(InterfaceDeclOptionList InterfaceDeclOptionList) {
        this.InterfaceDeclOptionList=InterfaceDeclOptionList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceDeclName!=null) InterfaceDeclName.accept(visitor);
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceDeclName!=null) InterfaceDeclName.traverseTopDown(visitor);
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceDeclName!=null) InterfaceDeclName.traverseBottomUp(visitor);
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InterfaceDecl(\n");

        if(InterfaceDeclName!=null)
            buffer.append(InterfaceDeclName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(InterfaceDeclOptionList!=null)
            buffer.append(InterfaceDeclOptionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceDecl]");
        return buffer.toString();
    }
}
