package rs.ac.bg.etf.pp1.util;


import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class MySymbolTableVisitor extends SymbolTableVisitor {
    protected StringBuilder output = new StringBuilder();
    protected final String indent = "   ";
    protected StringBuilder currentIndent = new StringBuilder();

    protected void nextIndentationLevel() {
        this.currentIndent.append("   ");
    }

    protected void previousIndentationLevel() {
        if (this.currentIndent.length() > 0) {
            this.currentIndent.setLength(this.currentIndent.length() - "   ".length());
        }

    }

    public void visitObjNode(Obj objToVisit) {
        switch (objToVisit.getKind()) {
            case 0:
                this.output.append("Con ");
                break;
            case 1:
                this.output.append("Var ");
                break;
            case 2:
                this.output.append("Type ");
                break;
            case 3:
                this.output.append("Meth ");
                break;
            case 4:
                this.output.append("Fld ");
            case 5:
            default:
                break;
            case 6:
                this.output.append("Prog ");
        }

        this.output.append(objToVisit.getName());
        this.output.append(": ");
        if (1 == objToVisit.getKind() && "this".equalsIgnoreCase(objToVisit.getName())) {
            this.output.append("");
        } else {
            objToVisit.getType().accept(this);
        }

        this.output.append(", ");
        this.output.append(objToVisit.getAdr());
        this.output.append(", ");
        this.output.append(objToVisit.getLevel() + " ");
        if (objToVisit.getKind() == 6 || objToVisit.getKind() == 3) {
            this.output.append("\n");
            this.nextIndentationLevel();
        }

        for(Obj o : objToVisit.getLocalSymbols()) {
            this.output.append(this.currentIndent.toString());
            o.accept(this);
            this.output.append("\n");
        }

        if (objToVisit.getKind() == 6 || objToVisit.getKind() == 3) {
            this.previousIndentationLevel();
        }

    }

    public void visitScopeNode(Scope scope) {
        for(Obj o : scope.values()) {
            o.accept(this);
            this.output.append("\n");
        }

    }

    public void visitStructNode(Struct structToVisit) {
        switch (structToVisit.getKind()) {
            case 0:
                this.output.append("notype");
                break;
            case 1:
                this.output.append("int");
                break;
            case 2:
                this.output.append("char");
                break;
            case 3:
                this.output.append("Arr of ");
                switch (structToVisit.getElemType().getKind()) {
                    case 0:
                        this.output.append("notype");
                        return;
                    case 1:
                        this.output.append("int");
                        return;
                    case 2:
                        this.output.append("char");
                        return;
                    case 3:
                    default:
                        return;
                    case 4:
                        this.output.append("Class");
                        return;
                }
            case 4:
                this.output.append("Class [");

                for(Obj obj : structToVisit.getMembers()) {
                    obj.accept(this);
                }

                this.output.append("]");
            case 5:
                this.output.append("bool");
                break;
            case 6:
                this.output.append("Set of ");
                switch (structToVisit.getElemType().getKind()) {
                    case 1:
                        this.output.append("int");
                        return;
                    case 2:
                        this.output.append("char");
                        return;
                    case 3:
                    default:
                        return;
                    case 4:
                        this.output.append("Class");
                        return;
                }
        }

    }

    public String getOutput() {
        return this.output.toString();
    }
}
