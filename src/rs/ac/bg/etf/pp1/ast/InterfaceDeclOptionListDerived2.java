// generated with ast extension for cup
// version 0.8
// 23/5/2025 22:42:39


package rs.ac.bg.etf.pp1.ast;

public class InterfaceDeclOptionListDerived2 extends InterfaceDeclOptionList {

    private InterfaceDeclOptionList InterfaceDeclOptionList;
    private MethodSignature MethodSignature;
    private CloseMethodScope CloseMethodScope;

    public InterfaceDeclOptionListDerived2 (InterfaceDeclOptionList InterfaceDeclOptionList, MethodSignature MethodSignature, CloseMethodScope CloseMethodScope) {
        this.InterfaceDeclOptionList=InterfaceDeclOptionList;
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.setParent(this);
        this.MethodSignature=MethodSignature;
        if(MethodSignature!=null) MethodSignature.setParent(this);
        this.CloseMethodScope=CloseMethodScope;
        if(CloseMethodScope!=null) CloseMethodScope.setParent(this);
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

    public CloseMethodScope getCloseMethodScope() {
        return CloseMethodScope;
    }

    public void setCloseMethodScope(CloseMethodScope CloseMethodScope) {
        this.CloseMethodScope=CloseMethodScope;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.accept(visitor);
        if(MethodSignature!=null) MethodSignature.accept(visitor);
        if(CloseMethodScope!=null) CloseMethodScope.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.traverseTopDown(visitor);
        if(MethodSignature!=null) MethodSignature.traverseTopDown(visitor);
        if(CloseMethodScope!=null) CloseMethodScope.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceDeclOptionList!=null) InterfaceDeclOptionList.traverseBottomUp(visitor);
        if(MethodSignature!=null) MethodSignature.traverseBottomUp(visitor);
        if(CloseMethodScope!=null) CloseMethodScope.traverseBottomUp(visitor);
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

        if(CloseMethodScope!=null)
            buffer.append(CloseMethodScope.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceDeclOptionListDerived2]");
        return buffer.toString();
    }
}
