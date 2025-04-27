// generated with ast extension for cup
// version 0.8
// 27/3/2025 13:14:53


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclListValueDerived1 extends ConstDeclListValue {

    private NumConst numConst;

    public ConstDeclListValueDerived1 (NumConst numConst) {
        this.numConst=numConst;
        if(numConst!=null) numConst.setParent(this);
    }

    public NumConst getNumConst() {
        return numConst;
    }

    public void setNumConst(NumConst numConst) {
        this.numConst=numConst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(numConst!=null) numConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(numConst!=null) numConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(numConst!=null) numConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclListValueDerived1(\n");

        if(numConst!=null)
            buffer.append(numConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclListValueDerived1]");
        return buffer.toString();
    }
}
