package rs.ac.bg.etf.pp1.util;

import rs.ac.bg.etf.pp1.semantic_analysis.SemanticAnalyzer;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.Arrays;
import java.util.List;

public class TabExtended {

    Obj setObj;
    private static List<String> embeddedMethods;

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
        String name = "add";
        embeddedMethods.add(name);
        Tab.currentScope().addToLocals(addObj = new Obj(Obj.Meth, name, Tab.noType, 0, 3));
        {
            Tab.openScope();
            Obj a = new Obj(Obj.Var, "a", setObj.getType(), 0, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj b = new Obj(Obj.Var, "b", Tab.intType, 1, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj i = new Obj(Obj.Var, "i", Tab.intType, 2, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);

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
        String name = "addAll";
        embeddedMethods.add(name);
        Tab.currentScope().addToLocals(addAllObj = new Obj(Obj.Meth, name, Tab.noType, 0, 3));
        {
            Tab.openScope();

            Struct intArr = new Struct(Struct.Array, Tab.intType);

            Obj a = new Obj(Obj.Var, "a", setObj.getType(), 0, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj b = new Obj(Obj.Var, "b", intArr, 1, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj i = new Obj(Obj.Var, "i", Tab.intType, 2, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);

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
        String name = "$printSet";
        embeddedMethods.add(name);
        Tab.currentScope().addToLocals(printSetObj = new Obj(Obj.Meth, name, Tab.noType, 0, 3));
        {
            Tab.openScope();

            Obj a = new Obj(Obj.Var, "a", setObj.getType(), 0, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj offset = new Obj(Obj.Var, "offset", Tab.intType, 1, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj i = new Obj(Obj.Var, "i", Tab.intType, 2, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);

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
        String name = "$union";
        embeddedMethods.add(name);
        Tab.currentScope().addToLocals(unionSetObj = new Obj(Obj.Meth, name, setObj.getType(), 0, 4));
        {
            Tab.openScope();

            Obj a = new Obj(Obj.Var, "a", setObj.getType(), 0, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR); // hidden parameter for return vallue
            Obj b = new Obj(Obj.Var, "b", setObj.getType(), 1, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj c = new Obj(Obj.Var, "c", setObj.getType(), 2, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj i = new Obj(Obj.Var, "i", Tab.intType, 3, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);

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

    private void createIntersectSetsMethod(){
        Obj intersectSetsObj;
        String name = "$intersect";
        embeddedMethods.add(name);
        Tab.currentScope().addToLocals(intersectSetsObj = new Obj(Obj.Meth, name, setObj.getType(), 0, 5));
        {
            Tab.openScope();

            Obj s1 = new Obj(Obj.Var, "s1", setObj.getType(), 0, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj s2 = new Obj(Obj.Var, "s2", setObj.getType(), 1, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj dest = new Obj(Obj.Var, "dst", setObj.getType(), 2, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR); // hidden parameter for return value
            Obj i = new Obj(Obj.Var, "i", Tab.intType, 3, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj j = new Obj(Obj.Var, "j", Tab.intType, 4, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);

            dest.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);
            s1.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);
            s2.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);

            Tab.currentScope().addToLocals(s1);
            Tab.currentScope().addToLocals(s2);
            Tab.currentScope().addToLocals(dest);
            Tab.currentScope().addToLocals(i);
            Tab.currentScope().addToLocals(j);

            intersectSetsObj.setLocals(Tab.currentScope.getLocals());
            Tab.closeScope();
        }
    }

    private void createDifferenceSetsMethod(){
        Obj differenceSetsObj;
        String name = "$difference";
        embeddedMethods.add(name);
        Tab.currentScope().addToLocals(differenceSetsObj = new Obj(Obj.Meth, name, setObj.getType(), 0, 5));
        {
            Tab.openScope();

            Obj s1 = new Obj(Obj.Var, "s1", setObj.getType(), 0, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj s2 = new Obj(Obj.Var, "s2", setObj.getType(), 1, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj dest = new Obj(Obj.Var, "dst", setObj.getType(), 2, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR); // hidden parameter for return value
            Obj i = new Obj(Obj.Var, "i", Tab.intType, 3, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);
            Obj j = new Obj(Obj.Var, "j", Tab.intType, 4, SemanticAnalyzer.LEVEL_GLOBAL_METH_VAR);

            dest.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);
            s1.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);
            s2.setFpPos(SemanticAnalyzer.FP_POS_FORMAL_PARAMETER);

            Tab.currentScope().addToLocals(s1);
            Tab.currentScope().addToLocals(s2);
            Tab.currentScope().addToLocals(dest);
            Tab.currentScope().addToLocals(i);
            Tab.currentScope().addToLocals(j);

            differenceSetsObj.setLocals(Tab.currentScope.getLocals());
            Tab.closeScope();
        }
    }

    private void createEmbeddedMethods() {
        createAddMethod();
        createAddAllMethod();
        createPrintSetMethod();
        createUnionSetMethod();
        createIntersectSetsMethod();
        createDifferenceSetsMethod();
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
                embeddedMethods.add(node.getName());
            }
        }

        // embedded methods
        createEmbeddedMethods();
    }



    public static boolean isMethodEmbedded(String node) {
        return embeddedMethods.contains(node);
    }

    // </editor-fold>
}