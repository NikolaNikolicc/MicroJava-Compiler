// generated with ast extension for cup
// version 0.8
// 6/4/2025 7:9:34


package rs.ac.bg.etf.pp1.ast;

public class InterfaceDeclDerived1 extends InterfaceDecl {

    private String I1;
    private InterfaceDeclOptionList InterfaceDeclOptionList;

    public InterfaceDeclDerived1 (String I1, InterfaceDeclOptionList InterfaceDeclOptionList) {
        this.I1=I1;
        this.InterfaceDeclOptionList=InterfaceDeclOptionList;
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public InterfaceDeclOptionList getInterfaceDeclOptionList() {
        return InterfaceDeclOptionList;
    }

    public void setInterfaceDeclOptionList(InterfaceDeclOptionList InterfaceDeclOptionList) {
        this.InterfaceDeclOptionList=InterfaceDeclOptionList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InterfaceDeclDerived1(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(InterfaceDeclOptionList!=null)
            buffer.append(InterfaceDeclOptionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceDeclDerived1]");
        return buffer.toString();
    }
}
