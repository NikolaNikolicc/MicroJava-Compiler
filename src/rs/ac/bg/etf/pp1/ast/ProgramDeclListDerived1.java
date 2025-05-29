// generated with ast extension for cup
// version 0.8
// 29/4/2025 14:30:56


package rs.ac.bg.etf.pp1.ast;

public class ProgramDeclListDerived1 extends ProgramDeclList {

    private ProgramDeclList ProgramDeclList;
    private ProgramDeclElem ProgramDeclElem;

    public ProgramDeclListDerived1 (ProgramDeclList ProgramDeclList, ProgramDeclElem ProgramDeclElem) {
        this.ProgramDeclList=ProgramDeclList;
        if(ProgramDeclList!=null) ProgramDeclList.setParent(this);
        this.ProgramDeclElem=ProgramDeclElem;
        if(ProgramDeclElem!=null) ProgramDeclElem.setParent(this);
    }

    public ProgramDeclList getProgramDeclList() {
        return ProgramDeclList;
    }

    public void setProgramDeclList(ProgramDeclList ProgramDeclList) {
        this.ProgramDeclList=ProgramDeclList;
    }

    public ProgramDeclElem getProgramDeclElem() {
        return ProgramDeclElem;
    }

    public void setProgramDeclElem(ProgramDeclElem ProgramDeclElem) {
        this.ProgramDeclElem=ProgramDeclElem;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ProgramDeclList!=null) ProgramDeclList.accept(visitor);
        if(ProgramDeclElem!=null) ProgramDeclElem.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ProgramDeclList!=null) ProgramDeclList.traverseTopDown(visitor);
        if(ProgramDeclElem!=null) ProgramDeclElem.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ProgramDeclList!=null) ProgramDeclList.traverseBottomUp(visitor);
        if(ProgramDeclElem!=null) ProgramDeclElem.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ProgramDeclListDerived1(\n");

        if(ProgramDeclList!=null)
            buffer.append(ProgramDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ProgramDeclElem!=null)
            buffer.append(ProgramDeclElem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgramDeclListDerived1]");
        return buffer.toString();
    }
}
