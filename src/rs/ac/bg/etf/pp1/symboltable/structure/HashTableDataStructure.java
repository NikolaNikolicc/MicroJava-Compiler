package rs.ac.bg.etf.pp1.symboltable.structure;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import rs.ac.bg.etf.pp1.symboltable.concepts.Obj;
import rs.ac.bg.etf.pp1.symboltable.structure.SymbolDataStructure;

public class HashTableDataStructure extends SymbolDataStructure {

    protected Map<String, Obj> hashTable = new LinkedHashMap<String, Obj>();

    @Override
    public Obj searchKey(String key) {
        return hashTable.get(key);
    }

    @Override
    public boolean deleteKey(String key) {
        Obj o = null;
        if (hashTable.containsKey(key)) {
            o = hashTable.remove(key);
        }
        return !hashTable.containsKey(key) && (o != null);
    }


    @Override
    public boolean insertKey(Obj node) {
        if (hashTable.containsKey(node.getName()))
            return false;
        else{
            hashTable.put(node.getName(), node);
            return true;
        }
    }

    @Override
    public Collection<Obj> symbols() {
        return hashTable.values();
    }

    @Override
    public int numSymbols() {
        return hashTable.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Obj symbol : hashTable.values()) {
            sb.append(symbol.toString()).append("\n");
        }
        return sb.toString();
    }


}
