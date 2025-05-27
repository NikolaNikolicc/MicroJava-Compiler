// generated with ast extension for cup
// version 0.8
// 27/4/2025 11:8:20


package rs.ac.bg.etf.pp1.ast;

public class DesignatorClassMoreNotFinal extends DesignatorClassMore {

    private DesignatorClassMore DesignatorClassMore;
    private String I2;

    public DesignatorClassMoreNotFinal (DesignatorClassMore DesignatorClassMore, String I2) {
        this.DesignatorClassMore=DesignatorClassMore;
        if(DesignatorClassMore!=null) DesignatorClassMore.setParent(this);
        this.I2=I2;
    }

    public DesignatorClassMore getDesignatorClassMore() {
        return DesignatorClassMore;
    }

    public void setDesignatorClassMore(DesignatorClassMore DesignatorClassMore) {
        this.DesignatorClassMore=DesignatorClassMore;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorClassMore!=null) DesignatorClassMore.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorClassMore!=null) DesignatorClassMore.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorClassMore!=null) DesignatorClassMore.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorClassMoreNotFinal(\n");

        if(DesignatorClassMore!=null)
            buffer.append(DesignatorClassMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorClassMoreNotFinal]");
        return buffer.toString();
    }
}
