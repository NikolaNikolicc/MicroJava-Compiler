package rs.ac.bg.etf.pp1.symboltable.concepts;

import java.util.Collection;
import java.util.Collections;

import rs.ac.bg.etf.pp1.symboltable.concepts.Obj;
import rs.ac.bg.etf.pp1.symboltable.factory.SymbolTableFactory;
import rs.ac.bg.etf.pp1.symboltable.structure.SymbolDataStructure;
import rs.ac.bg.etf.pp1.symboltable.visitors.SymbolTableVisitor;


/**
 * Opseg u MikroJava tabeli simbola
 *
 * @author ETF
 *
 */
public class Scope {
    // referenca na okruzujuci opseg
    private Scope outer;

    // stablo (tabela simbola) za ovaj opseg
    private SymbolDataStructure locals;

    // broj simbola deklarisanih u opsegu
    private int nVars = 0;


    public Scope(Scope outer) {
        this.outer = outer;
    }

    public boolean addToLocals(Obj o) {
        if (locals == null)
            locals = SymbolTableFactory.instance().createSymbolTableDataStructure();

        boolean isOK = locals.insertKey(o);
        if (isOK && (o.getKind() == Obj.Var || o.getKind() == Obj.Fld)) {
            o.setAdr(nVars++);
        }
        return isOK;
    }

    public Obj findSymbol(String objName) {
        return (locals != null) ? locals.searchKey(objName) : null;
    }

    public Scope getOuter() {
        return outer;
    }

    public int getnVars() {
        return nVars;
    }

    public SymbolDataStructure getLocals() {
        return locals;
    }

    public Collection<Obj> values() {
        return (locals != null) ? locals.symbols() : Collections.<Obj>emptyList();
    }

    public void accept(SymbolTableVisitor stv) {
        stv.visitScopeNode(this);
    }
}
