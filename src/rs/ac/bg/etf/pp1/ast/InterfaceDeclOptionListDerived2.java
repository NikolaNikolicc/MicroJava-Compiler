// generated with ast extension for cup
// version 0.8
// 4/4/2025 21:46:21


package rs.ac.bg.etf.pp1.ast;

public class InterfaceDeclOptionListDerived2 extends InterfaceDeclOptionList {

    private InterfaceDeclOptionList InterfaceDeclOptionList;
    private MethodSignature MethodSignature;

    public InterfaceDeclOptionListDerived2 (InterfaceDeclOptionList InterfaceDeclOptionList, MethodSignature MethodSignature) {
        this.InterfaceDeclOptionList=InterfaceDeclOptionList;
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.setParent(this);
        this.MethodSignature=MethodSignature;
        if(MethodSignature!=null) MethodSignature.setParent(this);
    }

    public InterfaceDeclOptionList getInterfaceDeclOptionList() {
        return InterfaceDeclOptionList;
    }

    public void setInterfaceDeclOptionList(InterfaceDeclOptionList InterfaceDeclOptionList) {
        this.InterfaceDeclOptionList=InterfaceDeclOptionList;
    }

    public MethodSignature getMethodSignature() {
        return MethodSignature;
    }

    public void setMethodSignature(MethodSignature MethodSignature) {
        this.MethodSignature=MethodSignature;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.accept(visitor);
        if(MethodSignature!=null) MethodSignature.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.traverseTopDown(visitor);
        if(MethodSignature!=null) MethodSignature.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.traverseBottomUp(visitor);
        if(MethodSignature!=null) MethodSignature.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InterfaceDeclOptionListDerived2(\n");

        if(InterfaceDeclOptionList!=null)
            buffer.append(InterfaceDeclOptionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodSignature!=null)
            buffer.append(MethodSignature.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceDeclOptionListDerived2]");
        return buffer.toString();
    }
}
