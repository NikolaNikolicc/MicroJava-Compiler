// generated with ast extension for cup
// version 0.8
// 28/3/2025 18:13:0


package rs.ac.bg.etf.pp1.ast;

public class charConst extends ConstDeclListValue {

    private Character char;

    public charConst (Character char) {
        this.char=char;
    }

    public Character getChar() {
        return char;
    }

    public void setChar(Character char) {
        this.char=char;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("charConst(\n");

        buffer.append(" "+tab+char);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [charConst]");
        return buffer.toString();
    }
}
