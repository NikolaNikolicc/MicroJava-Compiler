// generated with ast extension for cup
// version 0.8
// 13/0/2025 13:9:5


package rs.ac.bg.etf.pp1.ast;

public class FactorDerived4 extends Factor {

    private BoolConst boolConst;

    public FactorDerived4 (BoolConst boolConst) {
        this.boolConst=boolConst;
        if(boolConst!=null) boolConst.setParent(this);
    }

    public BoolConst getBoolConst() {
        return boolConst;
    }

    public void setBoolConst(BoolConst boolConst) {
        this.boolConst=boolConst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(boolConst!=null) boolConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(boolConst!=null) boolConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(boolConst!=null) boolConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorDerived4(\n");

        if(boolConst!=null)
            buffer.append(boolConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorDerived4]");
        return buffer.toString();
    }
}
