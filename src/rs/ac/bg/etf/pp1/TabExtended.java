package rs.ac.bg.etf.pp1;

import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.Arrays;
import java.util.List;

public class TabExtended {

    Obj setObj;

    // <editor-fold desc="Singleton Instance">

    private static TabExtended instance;

    private TabExtended() {
        init();
    }

    public static TabExtended getInstance() {
        if (instance == null) {
            instance = new TabExtended();
        }
        return instance;
    }

    // </editor-fold>

    // <editor-fold desc="embedded types">

    private void createBooleanType() {
        Struct boolStruct = new Struct(Struct.Bool);
        Obj boolObj = Tab.insert(Obj.Type, "bool", boolStruct);
        boolObj.setAdr(-1);
        boolObj.setLevel(-1);
    }

    private void createSetType() {
        Struct setStruct = new Struct(Struct.Enum, Tab.intType);
        setObj = Tab.insert(Obj.Type, "set", setStruct);
        setObj.setAdr(-1);
        setObj.setLevel(-1);
    }

    private void createEmbeddedTypes(){
        createBooleanType();
        createSetType();
    }

    // </editor-fold>

    // <editor-fold desc="embedded methods">

    private void createAddMethod(){
        Obj addObj;
        Tab.currentScope().addToLocals(addObj = new Obj(Obj.Meth, "add", Tab.noType, 0, 2));
        {
            Tab.openScope();
            Obj a = new Obj(Obj.Var, "a", setObj.getType(), 0, 2);
            Obj b = new Obj(Obj.Var, "b", Tab.intType, 0, 1);

            Obj i = new Obj(Obj.Var, "i", Tab.intType, 0, 1);

            a.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);
            b.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);

            Tab.currentScope().addToLocals(a);
            Tab.currentScope().addToLocals(b);
            Tab.currentScope().addToLocals(i);

            addObj.setLocals(Tab.currentScope.getLocals());
            Tab.closeScope();
        }
    }

    private void createAddAllMethod(){
        Obj addAllObj;
        Tab.currentScope().addToLocals(addAllObj = new Obj(Obj.Meth, "addAll", Tab.noType, 0, 2));
        {
            Tab.openScope();

            Struct intArr = new Struct(Struct.Array, Tab.intType);

            Obj a = new Obj(Obj.Var, "a", setObj.getType(), 0, 1);
            Obj b = new Obj(Obj.Var, "b", intArr, 0, 1);
            Obj i = new Obj(Obj.Var, "i", Tab.intType, 0, 1);

            a.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);
            b.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);

            Tab.currentScope().addToLocals(a);
            Tab.currentScope().addToLocals(b);
            Tab.currentScope().addToLocals(i);

            addAllObj.setLocals(Tab.currentScope.getLocals());
            Tab.closeScope();
        }
    }

    private void createPrintSetMethod(){
        Obj printSetObj;
        Tab.currentScope().addToLocals(printSetObj = new Obj(Obj.Meth, "$printSet", Tab.noType, 0, 3));
        {
            Tab.openScope();

            Obj a = new Obj(Obj.Var, "a", setObj.getType(), 0, 1);
            Obj offset = new Obj(Obj.Var, "offset", Tab.intType, 0, 1);
            Obj i = new Obj(Obj.Var, "i", Tab.intType, 0, 1);

            a.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);
            offset.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);

            Tab.currentScope().addToLocals(a);
            Tab.currentScope().addToLocals(offset);
            Tab.currentScope().addToLocals(i);

            printSetObj.setLocals(Tab.currentScope.getLocals());
            Tab.closeScope();
        }
    }

    private void createUnionSetMethod(){
        Obj unionSetObj;
        Tab.currentScope().addToLocals(unionSetObj = new Obj(Obj.Meth, "$union", Tab.noType, 0, 3));
        {
            Tab.openScope();

            Obj a = new Obj(Obj.Var, "a", setObj.getType(), 0, 1);
            Obj b = new Obj(Obj.Var, "b", setObj.getType(), 0, 1);
            Obj c = new Obj(Obj.Var, "c", setObj.getType(), 0, 1);
            Obj i = new Obj(Obj.Var, "i", Tab.intType, 0, 1);

            a.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);
            b.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);
            c.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);

            Tab.currentScope().addToLocals(a);
            Tab.currentScope().addToLocals(b);
            Tab.currentScope().addToLocals(c);
            Tab.currentScope().addToLocals(i);

            unionSetObj.setLocals(Tab.currentScope.getLocals());
            Tab.closeScope();
        }
    }

    private void createEmbeddedMethods() {
        createAddMethod();
        createAddAllMethod();
        createPrintSetMethod();
        createUnionSetMethod();
    }

    // </editor-fold>

    // <editor-fold desc="interface methods">

    private void init(){
        // inicijalizacija tabele simbola
        Tab.init();

        // embedded types
        createEmbeddedTypes();

        // podesiti fp pos na 1 za sve parametre metoda u originalnoj tabeli simbola
        for (Obj node: Tab.currentScope().getLocals().symbols()){
            if (node.getKind() == Obj.Meth){
                for (Obj local: node.getLocalSymbols()){
                    local.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);
                }
            }
        }

        // embedded methods
        createEmbeddedMethods();
    }

    static boolean isMethodEmbedded(String node) {
        List<String> embeddedMethods = Arrays.asList("add", "addAll", "ord", "ch", "len", "arr", "$printSet", "$union");
        return embeddedMethods.contains(node);
    }

    // </editor-fold>
}