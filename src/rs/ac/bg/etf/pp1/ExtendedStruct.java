package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

import java.util.HashSet;

public class ExtendedStruct extends Struct {

    private HashSet<ExtendedStruct> visited = new HashSet<>();

    public ExtendedStruct(int kind) {
        super(kind);
    }

    public ExtendedStruct(int kind, ExtendedStruct elemType) {
        super(kind, elemType);
    }

    public ExtendedStruct(int kind, SymbolDataStructure members) {
        super(kind, members);
    }


    public boolean isRefType() {
        int kind = this.getKind();
        return kind == Class || kind == Interface || kind == Array || kind == Enum;
    }

    public boolean equals(ExtendedStruct other) {
        if (this.getKind() == Array) {
            return other.getKind() == Array && this.getElemType().equals(other.getElemType());
        }else {
            return this == other;
        }
    }

    public boolean compatibleWith(ExtendedStruct other) {
        return this.equals(other) || this == Tab.nullType && other.isRefType() || other == Tab.nullType && this.isRefType();
    }


    private static boolean isPolymorphicallyAssignableTo(Struct right, Struct dest) {
        if (right.getImplementedInterfaces().isEmpty()){
            return false;
        }

        Struct node = right.getImplementedInterfaces().toArray(new Struct[0])[0];
        if (node.equals(dest)){
            return true;
        }

        return isPolymorphicallyAssignableTo(node, dest);
    }

    public boolean assignableTo(ExtendedStruct dest) {
        if (this.equals(dest) || this == Tab.nullType && dest.isRefType() || this.getKind() == Array && dest.getKind() == Array && dest.getElemType() == Tab.noType){
            return true;
        }

        return isPolymorphicallyAssignableTo(this, dest);
    }

}
