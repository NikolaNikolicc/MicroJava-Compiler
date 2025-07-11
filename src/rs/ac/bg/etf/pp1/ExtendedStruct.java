package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Struct;

public class ExtendedStruct {
    // Singleton instance
    private static ExtendedStruct instance;

    // Private constructor to prevent instantiation
    private ExtendedStruct() {}

    // Public method to get the singleton instance
    public static ExtendedStruct getInstance() {
        if (instance == null) {
            instance = new ExtendedStruct();
        }
        return instance;
    }

    public boolean isRefType(Struct node) {
        int kind = node.getKind();
        return kind == Struct.Class || kind == Struct.Interface || kind == Struct.Array || kind == Struct.Enum;
    }

    public boolean equals(Struct node, Struct other) {
        if (node.getKind() == Struct.Array) {
            return node.getKind() == Struct.Array && equals(node.getElemType(), other.getElemType());
        }else {
            return node == other;
        }
    }

    public boolean compatibleWith(Struct node, Struct other) {
        return equals(node, other) || node == Tab.nullType && isRefType(other) || other == Tab.nullType && isRefType(node);
    }


    private boolean isPolyAssignableToClass(Struct right, Struct dest) {
        Struct node = right;
        while (node != null){
            if (equals(node, dest)) {
                return true;
            }
            node = node.getElemType();
        }
        return false;
    }

    private boolean isPolyAssignableToInterface(Struct right, Struct dest) {
        for (Struct iface : right.getImplementedInterfaces()) {
            if (equals(iface, dest)) {
                return true;
            }
        }
        return false;
    }

    public boolean assignableTo(Struct node, Struct dest) {
        if (equals(node, dest) || node == Tab.nullType && isRefType(dest) || node.getKind() == Struct.Array && dest.getKind() == Struct.Array && dest.getElemType() == Tab.noType){
            return true;
        }
        return isPolyAssignableToClass(node.getElemType(), dest) || isPolyAssignableToInterface(node, dest);
    }

}
