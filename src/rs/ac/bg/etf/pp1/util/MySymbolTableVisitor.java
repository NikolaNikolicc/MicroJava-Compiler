package rs.ac.bg.etf.pp1.util;


import rs.etf.pp1.symboltable.concepts.Module;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class MySymbolTableVisitor extends DumpSymbolTableVisitor {

    protected StringBuilder output = new StringBuilder();
    protected final String indent = "   ";
    protected StringBuilder currentIndent = new StringBuilder();

    protected void nextIndentationLevel() {
        currentIndent.append(indent);
    }

    protected void previousIndentationLevel() {
        if (currentIndent.length() > 0)
            currentIndent.setLength(currentIndent.length()-indent.length());
    }


    /* (non-Javadoc)
     * @see rs.etf.pp1.symboltable.test.SymbolTableVisitor#visitObjNode(symboltable.Obj)
     */
    @Override
    public void visitObjNode(Obj objToVisit) {
        //output.append("[");
        switch (objToVisit.getKind()) {
            case Obj.Con:  output.append("Con "); break;
            case Obj.Var:  output.append("Var "); break;
            case Obj.Type: output.append("Type "); break;
            case Obj.Meth: output.append("Meth "); break;
            case Obj.Fld:  output.append("Fld "); break;
            case Obj.Prog: output.append("Prog "); break;
        }

        output.append(objToVisit.getName());
        output.append(": ");

        if ((Obj.Var == objToVisit.getKind()) && "this".equalsIgnoreCase(objToVisit.getName()))
            output.append("");
        else
            objToVisit.getType().accept(this);

        output.append(", ");
        output.append(objToVisit.getAdr());
        output.append(", ");
        output.append(objToVisit.getLevel() + " ");

        if (objToVisit.getKind() == Obj.Prog || objToVisit.getKind() == Obj.Meth) {
            output.append("\n");
            nextIndentationLevel();
        }


        for (Obj o : objToVisit.getLocalSymbols()) {
            output.append(currentIndent.toString());
            o.accept(this);
            output.append("\n");
        }

        if (objToVisit.getKind() == Obj.Prog || objToVisit.getKind() == Obj.Meth)
            previousIndentationLevel();

        //output.append("]");

    }

    /* (non-Javadoc)
     * @see rs.etf.pp1.symboltable.test.SymbolTableVisitor#visitScopeNode(symboltable.Scope)
     */
    @Override
    public void visitScopeNode(Scope scope) {
        for (Obj o : scope.values()) {
            o.accept(this);
            output.append("\n");
        }
    }

    @Override
    public void visitModuleNode(Module moduleToVisit) {
        output.append("\n----------- Module -----------\n");
        nextIndentationLevel();
        // print imported modules (only names)
        output.append(currentIndent.toString() + "Name: " + moduleToVisit.getName() + "\n");
        output.append(currentIndent.toString() + "----------------------------------\n");
        if (moduleToVisit.getImportedModules().size() == 0) {
            output.append(currentIndent.toString() + "Imports: <none>\n");
        }
        for (Module importedModule: moduleToVisit.getImportedModules()) {
            output.append(currentIndent.toString() + "Imports: " + importedModule.getName() + "\n");
        }
        output.append(currentIndent.toString() + "----------------------------------\n");
        // print imported aliases
        if (moduleToVisit.getImportedAliases().size() == 0) {
            output.append(currentIndent.toString() + "Imported aliases: <none>\n");
        }
        for (Module.ListNode aliasNode: moduleToVisit.getImportedAliases()) {
            output.append(currentIndent.toString() + "Imported alias: ");
            aliasNode.obj.accept(this);
            output.append("\n");
        }
        output.append(currentIndent.toString() + "----------------------------------\n");
        // print local symbols
        if (moduleToVisit.getLocals().size() == 0) {
            output.append(currentIndent.toString() + "Local symbols: <none>\n");
        }
        for (Obj localSymbol: moduleToVisit.getLocals()) {
            output.append(currentIndent.toString() + "Local symbol: ");
            localSymbol.accept(this);
            output.append("\n");
        }
        output.append(currentIndent.toString() + "----------------------------------\n");
        // print exported symbols
        if (moduleToVisit.getExportedSymbols().size() == 0) {
            output.append(currentIndent.toString() + "Exports: <none>\n");
        }
        for (Module.ListNode exportNode: moduleToVisit.getExportedSymbols()) {
            output.append(currentIndent.toString() + "Exports: ");
            exportNode.obj.accept(this);
            output.append("\n");
        }
        previousIndentationLevel();
        output.append("------- End of module --------");
    }

    /* (non-Javadoc)
     * @see rs.etf.pp1.symboltable.test.SymbolTableVisitor#visitStructNode(symboltable.Struct)
     */
    @Override
    public void visitStructNode(Struct structToVisit) {
        switch (structToVisit.getKind()) {
            case Struct.None:
                output.append("notype");
                break;
            case Struct.Int:
                output.append("int");
                break;
            case Struct.Char:
                output.append("char");
                break;
            case Struct.Bool:
                output.append("bool");
                break;
            case Struct.Enum:
                output.append("set");
                break;
            case Struct.Array:
                output.append("Arr of ");

                switch (structToVisit.getElemType().getKind()) {
                    case Struct.None:
                        output.append("notype");
                        break;
                    case Struct.Int:
                        output.append("int");
                        break;
                    case Struct.Bool:
                        output.append("bool");
                        break;
                    case Struct.Char:
                        output.append("char");
                        break;
                    case Struct.Enum:
                        output.append("set");
                        break;
                    case Struct.Class:
                        output.append("Class");
                        break;
                    case Struct.Interface:
                        output.append("Interface");
                        break;
                }
                break;
            case Struct.Class:
                output.append("Class [\n");
                for (Obj obj : structToVisit.getMembers()) {
                    obj.accept(this);
                }
                output.append("]");
                break;
            case Struct.Interface:
                output.append("Interface [\n");
                for (Obj obj : structToVisit.getMembers()) {
                    obj.accept(this);
                }
                output.append("]");
                break;
        }

    }

    public String getOutput() {
        return output.toString();
    }


}
