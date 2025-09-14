package rs.ac.bg.etf.pp1.semantic_analysis;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.autorun.CompilerAutorun;
import rs.ac.bg.etf.pp1.util.CompilerService;
import rs.ac.bg.etf.pp1.util.TabExtended;
import rs.ac.bg.etf.pp1.syntax_analysis.output.ast.*;
import rs.ac.bg.etf.pp1.util.StructExtended;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.structure.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class SemanticAnalyzer extends VisitorAdaptor{

    private String name;

    public static final int FP_POS_GLOBAL_METHOD = 0; // by default fp pos is set to 0
    public static final int FP_POS_IMPLEMENTED_NONGLOBAL_METHOD = 1;
    public static final int FP_POS_UNIMPLEMENTED_INTERFACE_METHOD = 2;
    public static final int FP_POS_FORMAL_PARAMETER = 1;

    public static final int LEVEL_INTERFACE_VAR = 3; // interface method variables
    public static final int LEVEL_CLASS_FLD = 1; // class field variables
    public static final int LEVEL_CLASS_VAR = 2; // class method variables
    public static final int LEVEL_GLOBAL_METH_VAR = 1; // global method variables
    public static final int LEVEL_GLOBAL_VAR = 0; // global variables

    public boolean errorDetected = false;
    private boolean classMethodDecl = false; // used to check if we are in class method declaration or not
    private boolean voidMethodFlag = false; // used to check if method is void or not
    private boolean returnExprFlag = false; // used to check if method has return expression, no need to check if it is simple return because we do all checks in statementReturn visitor
    private boolean thisVarDetectedFlag = false;

    private Struct currTypeVar = Tab.noType; // Tab.noType - invalid type
    private Struct currTypeMeth = null; // Tab.noType - void method
    private String currTypeName = "";

    private Obj mainMeth = null;
    private Obj currMeth = null; // for creating method
    private Struct currClass = null; // for creating class
    private Struct accessClass = null; // for chaining
    private Struct currInterface = null; // for creating interface
    private Struct parentClass = Tab.noType; // parent class for currClass (can be class or interface)

    public static Struct boolType = Tab.find("bool").getType();
    public static Struct setType = Tab.find("set").getType();

    private static final String[] objKindNames = { "Con", "Var", "Type", "Meth", "Fld", "Elem", "Prog" };
    private static final String[] structKindNames = { "None", "Int", "Char", "Array", "Class", "Bool", "Set", "Interface" };

    private boolean parsingFormPars = false;

    private int loopCounter = 0;

    private final Stack<Struct> apStack = new Stack<>();

    private String importQualifiedName = "";

    private static final StructExtended es = StructExtended.getInstance();
    private static final ModuleHandler moduleHandler = ModuleHandler.getInstance();
    Logger log = Logger.getLogger(getClass());

    // <editor-fold desc="Constructors">

    public SemanticAnalyzer(String name){
        this.name = name;
    }

    // </editor-fold>

    // <editor-fold desc="log methods">

    public void report_error(String message, SyntaxNode info) {
        errorDetected = true;
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0: info.getLine();
        if (line != 0)
            msg.append(" on line ").append(line);
        msg.append(" in module ").append(moduleHandler.getCurrentModule().getName());
        log.error(msg.toString());
    }

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0: info.getLine();
        if (line != 0)
            msg.append(" on line ").append(line);
        log.info(msg.toString());
    }

    private void printScope(SyntaxNode node) {
        report_info("--- CURRENT SCOPE SYMBOLS ---", node);
        for(Obj member: Tab.currentScope().getLocals().symbols()){
            logSymbol("member: ", member, node);
        }
    }

    private void logSymbol(String message, Obj sym, SyntaxNode node) {
        StringBuilder builder = new StringBuilder(message);
        if (node != null) {
            String tmp = " (line " + node.getLine() + ")";
            builder.append(tmp);
        }

        builder.append(": [NAME: ");
        builder.append(sym.getName()); // name
        builder.append(", OBJ KIND: ");
        builder.append(objKindNames[sym.getKind()]); // kind
        builder.append(", TYPE: ");
        builder.append(structKindNames[sym.getType().getKind()]); // type
        builder.append(", ADR: ");
        builder.append(sym.getAdr()); // adr
        builder.append(", LEVEL: ");
        builder.append(sym.getLevel()); // level
        builder.append(", FPPOS: ");
        builder.append(sym.getFpPos()); // fppos
        builder.append(", MODULE: ");
        builder.append(sym.getModule().getName());
        builder.append("]");

        log.info(builder.toString());
    }

    // </editor-fold>

    // <editor-fold desc="helpers for method calls">

    // this method is used to check if semantic analysis passed by Compiler.java
    public boolean passed(){
        return !errorDetected;
    }


    // helpers
    private boolean checkIsObjNodeDeclared(String name){
        Obj node = Tab.currentScope().findSymbol(name);
        return node != null;
    }

    // </editor-fold>

    // <editor-fold desc="Program and Type identification">

    @Override
    public void visit(Program node){
        Tab.chainLocalSymbols(node.getProgName().obj);
        Tab.closeScope();

        moduleHandler.closeModule();

        if(mainMeth == null){
            report_error("[Program] Main method must be defined", node);
        }

        if(mainMeth != null && mainMeth.getLevel() > 0){
            report_error("[Program] Main method must be declared without formal parameters", node);
        }
    }

    @Override
    public void visit(ProgName node){
        node.obj = Tab.insert(Obj.Prog, node.getProgName(), Tab.noType);

        moduleHandler.openModule(this.name);
        Module universe = moduleHandler.getModule("universe");
        if (universe != null){
            moduleHandler.getCurrentModule().importModule(universe);
        } else {
            report_error("[ProgName] Universe module not found and can't be implicitely imported", node);
        }

        if (moduleHandler.getCurrentModule() == moduleHandler.noModule){
            report_error("[ProgName] Circular imports detected, module " + node.getProgName() + " has been imported multiple times.", node);
        }
        Tab.openScope();
    }

    @Override
    public void visit(TypeIdent node){
        // returns NoObj if type is not found and Obj node if type is found
        Obj typeNode = Tab.find(node.getI1());
        if(typeNode == Tab.noObj){
            report_error("[TypeIdent] Type " + node.getI1() + " not found in symbol table", node);
            currTypeVar = Tab.noType;
            node.struct = Tab.noType;
            return;
        }
        if(Obj.Type != typeNode.getKind()){
            report_error("[TypeIdent] Name " + node.getI1() + " does not represent a type", node);
            currTypeVar = Tab.noType;
            node.struct = Tab.noType;
            return;
        }
        node.struct = typeNode.getType();
        currTypeVar = typeNode.getType();
        currTypeName = node.getI1();
    }

    // </editor-fold>

    // <editor-fold desc="Imports">

    @Override
    public void visit(ImportNameFinal node) {
        importQualifiedName = node.getI1();
    }

    @Override
    public void visit(ImportNameDot node) {
        importQualifiedName += "." + node.getI2();
    }

    private Module getAndFetch(Path modulePath) {
        // this will occur when we call getAndFetch with null path (its happens when we call getParent on root module path)
        if (modulePath == null) {
            return null;
        }
        Module m = moduleHandler.getModule(moduleHandler.toPackageName(modulePath));
        if (m != null) {
            // if loaded in moduleHandler return it
            return m;
        }
        // else load module from file, recuresive call
        if (!moduleHandler.existsModuleOnPath(modulePath)) {
            return null;
        }
        // recursive import
        int status =  CompilerService.build(modulePath);
        if (status != CompilerService.COMPILATION_SUCCESSFUL) {
            System.exit(status);
        }

        // we are sure that module exists in path and is loaded in moduleHandler now
        m = moduleHandler.getModule(moduleHandler.toPackageName(modulePath));
        if (m == null){
            report_error("[getAndFetch] Error occured while importing: " + modulePath.toString() + " module.", null);
            return null;
        }
        return m;
    }

    @Override
    public void visit(ImportDeclElem node){
        // if we have circular dependency we can't import module
        if (moduleHandler.getCurrentModule() == moduleHandler.noModule){
            return;
        }
        Path importPath = moduleHandler.fromPackageName(importQualifiedName);
        Path mod1 = moduleHandler.removeExtension(importPath);
        Module module = getAndFetch(mod1);
        if (module != null) {
            // add module in importedModules list of current module
            return;
        }
        Path mod2 = importPath.getParent();
        module = getAndFetch(mod2);
        if (module != null) {
            // check module exports contains alias
            // add alias to importedAliases list of current module
            return;
        }

        String mod1Str = (mod1 != null) ? mod1.toString() : "null";
        String mod2Str = (mod2 != null) ? mod2.toString() : "null";
        report_error("[ImportDeclElem] Import failed: could not resolve module: " + mod1Str + " or its parent: " + mod2Str, node);
    }

//    @Override
//    public void visit(ImportModule node){
//        // if we have circular dependency we can't import module
//        if (moduleHandler.getCurrentModule() == moduleHandler.noModule){
//            return;
//        }
//        // exists module in path is checked in openModule
//        if (!moduleHandler.existsModuleOnPath(node.getI1())){
//            report_error("[ImportModule] Module " + node.getI1() + " not found in module path", node);
//            return;
//        }
//        // load module
//        Module module = moduleHandler.getModule(node.getI1());
//        if (module == moduleHandler.noModule){
//            // recursive import
//
//        }
//        // add module in importedModules list of current module
//    }

//    @Override
//    public void visit(ImportAlias node){
//        // if we have circular dependency we can't import alias
//        if (moduleHandler.getCurrentModule() == moduleHandler.noModule){
//            return;
//        }
//        // exists module in path
//        if (!moduleHandler.existsModuleOnPath(node.getI1())){
//            report_error("[ImportAlias] Module " + node.getI1() + " not found in module path", node);
//            return;
//        }
//        // load module
//        Module module = moduleHandler.getModule(node.getI1());
//        if (module == moduleHandler.noModule){
//            // recursive import
//        }
//        // check module exports contains alias
//        // add alias to importedAliases list of current module
//    }

    // </editor-fold>

    // <editor-fold desc="Const and Var declarations">

    @Override
    public void visit(ConstDeclAssign node){
        report_info("stigli", null);
        if(checkIsObjNodeDeclared(node.getI1())){
            report_error("[ConstDeclAssign] Constant with name " + node.getI1() + " is already declared", node);
            return;
        }

        Obj constObj = node.getConstDeclListValue().obj;
        Obj constNode = Tab.insert(Obj.Con, node.getI1(), currTypeVar);

        if (!es.equals(constObj.getType(), currTypeVar)){
            report_error("[ConstDeclAssign] Declared constant type and assigned value are not compatible", node);
        }

        constNode.setAdr(constObj.getAdr());
//        logSymbol("Detektovana simbolicka konstanta:", constNode, node);
    }

    private void setMethodLevelAndVarFpPosIfFormalParameter(Obj node){
        // second condition is used to check if we are parsing formals for main method if main is declared multiple times
        if(node.getName().equals("this") || parsingFormPars){
            currMeth.setLevel(currMeth.getLevel() + 1);
            node.setFpPos(FP_POS_FORMAL_PARAMETER);
        }
    }

    private void setLevel(Obj node){
        if (currInterface != null && currInterface != Tab.noType){
            node.setLevel(LEVEL_INTERFACE_VAR);
        } else if (currClass != null && currClass != Tab.noType){
            if (classMethodDecl){
                node.setLevel(LEVEL_CLASS_VAR);
            } else {
                node.setLevel(LEVEL_CLASS_FLD);
            }
        } else if (currMeth != null && currMeth != Tab.noObj){
            node.setLevel(LEVEL_GLOBAL_METH_VAR);
        } else {
            node.setLevel(LEVEL_GLOBAL_VAR);
        }
    }

    private Obj insertNewVarNode(String name, Struct type){
        Obj varNode;
        if (currClass != null && currClass != Tab.noType && !classMethodDecl){
            varNode = Tab.insert(Obj.Fld, name, type);
        } else {
            varNode = Tab.insert(Obj.Var, name, type);
        }
        setLevel(varNode);
        setMethodLevelAndVarFpPosIfFormalParameter(varNode);
        return varNode;
    }

    @Override
    public void visit(VarDeclFinalVar node){
        // since we are not chained symbols to class node we are storing them in scope nodes as we visit them
        // we can't override name of class field with funcion variable name with this logic
        if(checkIsObjNodeDeclared(node.getI1())){
            report_error("[VarDeclFinalVar] Variable with name: " + node.getI1() + " is already declared", node);
            return;
        }
        node.obj = insertNewVarNode(node.getI1(), currTypeVar);
//        logSymbol("detektovan Obj cvor", varNode, node);
    }

    @Override
    public void visit(VarDeclFinalArray node){
        if(checkIsObjNodeDeclared(node.getI1())){
            report_error("[VarDeclFinalArray] Variable with name " + node.getI1() + " is already declared", node);
            return;
        }
        Struct array = new Struct(Struct.Array, currTypeVar);
        node.obj = insertNewVarNode(node.getI1(), array);
//        logSymbol("detektovan Obj cvor (elem niza)", varNode, node);
    }


    // </editor-fold>

    // <editor-fold desc="Method Declarations and Returns">

    /**
     *
     * @param name method name
     * @return false if implemented method with that name already exists in current scope
     */
    private boolean createMethodObjNode(String name){
        Obj meth = Tab.currentScope.findSymbol(name);
        if (TabExtended.isMethodEmbedded(name)){
            report_error("[createMethodObjNode] Method with name " + name + " is already defined", null);
            Tab.openScope();
            return false;
        }
        if (meth != null){
            if (meth.getFpPos() == FP_POS_UNIMPLEMENTED_INTERFACE_METHOD){
                currMeth = meth;
            } else{
                currMeth = Tab.noObj;
                Tab.openScope();
                return false;
            }
        } else {
            currMeth = Tab.insert(Obj.Meth, name, currTypeMeth);
        }
        currMeth.setLevel(0);
        Tab.openScope();
        return true;
    }

    private void closeMethod(){
        Tab.chainLocalSymbols(currMeth);
        Tab.closeScope();

        currMeth = null;
        returnExprFlag = false;
        voidMethodFlag = false;
    }

    // this method is called only from interface method signature
    @Override
    public void visit(CloseMethodScope node){
        currMeth.setFpPos(FP_POS_UNIMPLEMENTED_INTERFACE_METHOD);
        closeMethod();
    }

    @Override
    public void visit(MethodSignatureStartFormPars node){
        parsingFormPars = true;
    }

    @Override
    public void visit(MethodSignatureEndFormPars node){
        parsingFormPars = false;
    }

    @Override
    public void visit(NoVoidMethod node){
        // returns NoObj if type is not found and Obj node if type is found
        Obj typeNode = Tab.find(node.getI1());
        voidMethodFlag = false;
        if(typeNode == Tab.noObj){
            report_error("[NoVoidMethod] Type " + node.getI1() + " not found in symbol table", node);
            currTypeVar = Tab.noType;
            return;
        }
        if(Obj.Type != typeNode.getKind()){
            report_error("[NoVoidMethod] Name " + node.getI1() + " does not represent a type", node);
            currTypeVar = Tab.noType;
            return;
        }
        currTypeMeth = typeNode.getType();
    }

    @Override
    public void visit(VoidMethod node){
        currTypeMeth = Tab.noType;
        voidMethodFlag = true;
    }

    @Override
    public void visit(RegularMethod node){
        if (!createMethodObjNode(node.getI1())){
            report_error("A method with the name " + node.getI1() + " is already defined", node);
            node.obj = currMeth; // Tab.noObj;
            return;
        }

        if (currClass != null && currClass != Tab.noType){
            insertNewVarNode("this", currClass);
        }
        if (currInterface != null && currInterface != Tab.noType){
            insertNewVarNode("this", currInterface);
        }

        node.obj = currMeth;
    }

    @Override
    public void visit(MainMethod node){
        createMethodObjNode("main");
        if (mainMeth == null){
            mainMeth = currMeth;

            if(!voidMethodFlag){
                report_error("[MainMethod] Main method must be of return type void", node.getParent());
            }
        } else {
            report_error("[MainMethod] Main method is already defined", node.getParent());
        }

        node.obj = currMeth;
    }

    @Override
    public void visit(MethodDecl node){

        if(currClass != null || currInterface != null){
            currMeth.setFpPos(FP_POS_IMPLEMENTED_NONGLOBAL_METHOD);
        } else {
            currMeth.setFpPos(FP_POS_GLOBAL_METHOD);
        }

        if (!voidMethodFlag && !returnExprFlag && !currMeth.getName().equals("main")){
            report_error("[MethodDecl] In method(" + currMeth.getName() + ") with non-void return type, at least one return statement with a value must appear", node);
        }
        node.obj = currMeth;
        closeMethod();
    }

    @Override
    public void visit(StatementReturn node){
        if (currMeth == null){
            report_error("[StatementReturn] Return statement can only be used inside method body", node);
            return;
        }
        if (!voidMethodFlag){
            report_error("[StatementReturn] In method(" + currMeth.getName() + ") with non-void return type, each return statement must return a value", node);
        }
    }

    @Override
    public void visit(StatementReturnExpr node){
        if (currMeth == null){
            report_error("[StatementReturnExpr] Return statement can only be used inside method body", node);
            return;
        }
        if (voidMethodFlag){
            report_error("[StatementReturnExpr] In method(" + currMeth.getName() + ") with void return type, a value cannot be returned", node);
            returnExprFlag = true;
            return;
        }
        if (!es.equals(currMeth.getType(), node.getExpr().struct)){
            report_error("[StatementReturnExpr] Method return type and return statement type are not compatible", node);
        }
        returnExprFlag = true;
    }

    // </editor-fold>

    // <editor-fold desc="Class Methods and Fields">

    private void chainLocalSymbolsMethod(Obj fromMeth, Obj toMeth){
        SymbolDataStructure locals = new HashTableDataStructure();
        for (Obj localSym: fromMeth.getLocalSymbols()){
            locals.insertKey(localSym);

        }
        toMeth.setLocals(locals);
    }

    @Override
    public void visit(SetClassFieldAddress node){
        // we are creating Obj meth node for unimplemented interface methods in the current class scope
        if(parentClass != Tab.noType && parentClass.getKind() == Struct.Interface){
            for (Obj member: parentClass.getMembers()){
                if(member.getKind() == Obj.Meth && member.getFpPos() == FP_POS_UNIMPLEMENTED_INTERFACE_METHOD){
                    Obj n = Tab.insert(Obj.Meth, member.getName(), member.getType());
                    n.setFpPos(member.getFpPos());
                    chainLocalSymbolsMethod(member, n);
                }
            }
        }
    }

    /*
    this node is used to set currClass to zero, because in method decl inside class we want
    to set variables kind to Obj.Var, not Obj.Fld (see DesignatorVar visitor)
    * */
    @Override
    public void visit(ClassMethodDeclListStart node){
        classMethodDecl = true;
    }

    // </editor-fold>

    // <editor-fold desc="[Read, Print] Standard Input/Output">

    @Override
    public void visit(StatementRead node){
        int kind = node.getDesignator().obj.getKind();
        String name = node.getDesignator().obj.getName();
        Struct type = node.getDesignator().obj.getType();
        if (kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld){
            report_error("[StatementRead] Read operation on an invalid variable(" + name + ")", node);
            return;
        }
        else if(!es.equals(type, Tab.intType) && !es.equals(type, boolType) && !es.equals(type, Tab.charType)){
            report_error("[StatementRead] Read operation on a variable(" + name + ") that is not of type int, char, or bool", node);
            return;
        }
    }

    @Override
    public void visit(StatementPrint node){
        Struct type = node.getExpr().struct;

        if(!es.equals(type, Tab.intType) && !es.equals(type, boolType) && !es.equals(type, Tab.charType) && !es.equals(type, setType)){
            report_error("[StatementPrint] Print operation on an expression that is not of type int, char or bool", node);
            return;
        }
    }

    @Override
    public void visit(StatementPrintNumber node){
        Struct type = node.getExpr().struct;
        if(!es.equals(type, Tab.intType) && !es.equals(type, boolType) && !es.equals(type, Tab.charType) && !es.equals(type, setType)){
            report_error("[StatementPrintNumber] Print operation on an expression that is not of type int, char or bool", node);
            return;
        }
    }

    // </editor-fold>

    // <editor-fold desc="[Expr, Term] Arithmetic Operations (Minus, Addop, Mulop)">

    @Override
    public void visit(TermFactor node){
        node.struct = node.getFactor().struct;
    }

    @Override
    public void visit(TermTermMulopFactor node){
        if(!es.equals(node.getFactor().struct, Tab.intType) || !es.equals(node.getTerm().struct, Tab.intType)){
            report_error("[TermTermMulopFactor] Mulop operator requires both operands to be int values", node);
            node.struct = Tab.noType;
            return;
        }
        node.struct = node.getFactor().struct;
    }

    @Override
    public void visit(ExprTerm node){
        node.struct = node.getTerm().struct;
    }

    @Override
    public void visit(ExprMinusTerm node){
        if(!es.equals(node.getTerm().struct, Tab.intType)){
            report_error("[ExprMinusTerm] Minus can only be placed before an operand of type int", node);
            node.struct = Tab.noType;
            return;
        }
        node.struct = node.getTerm().struct;
    }

    @Override
    public void visit(ExprAddopTerm node){
        if(!es.equals(node.getTerm().struct, Tab.intType) || !es.equals(node.getExpr().struct, Tab.intType)){
            report_error("[ExprAddopTerm] Addop operator requires both operands to be int values", node);
            node.struct = Tab.noType;
            return;
        }
        node.struct = node.getTerm().struct;
    }

    @Override
    public void visit(ExprDesignatorMap node){
        Obj meth = node.getDesignator().obj;
        if (node.getMapDesignator().getDesignator().obj == Tab.noObj || meth == Tab.noObj){
            node.struct = Tab.noType;
            return;
        }

        if(meth.getKind() != Obj.Meth){
            report_error("[ExprDesignatorMap] Designator(" + meth.getName() + ") on the left side of MAP operand must be a method", node);
            node.struct = Tab.noType;
            return;
        }
        List<Struct> fpList = getFormalParameters(meth, node);
        if (fpList.size() != 1){
            report_error("[ExprDesignatorMap] Method on the left side of MAP operand must have exactly one parameter", node);
            node.struct = Tab.noType;
            return;
        }
        if(!es.equals(fpList.get(0), Tab.intType)){
            report_error("[ExprDesignatorMap] Parameter of method on the left side of MAP operand must be of type int", node);
            node.struct = Tab.noType;
            return;
        }
        if(!es.equals(meth.getType(), Tab.intType)){
            report_error("[ExprDesignatorMap] Return value of method on the left side of MAP operand must be of type int", node);
            node.struct = Tab.noType;
            return;
        }
        node.struct = Tab.intType;
    }

    @Override
    public void visit(MapDesignator node){
        if (node.getDesignator().obj == Tab.noObj){
            return;
        }

        Obj arr = node.getDesignator().obj;

        // if we pass array we are sure that we are passing var or field, other kinds can't be arrays or can't be used due to syntax
//        if (arr.getKind() != Obj.Fld && arr.getKind() != Obj.Var || arr.getType().getKind() != Struct.Array || !arr.getType().getElemType().equals(Tab.intType)){
        if (arr.getType().getKind() != Struct.Array || !es.equals(arr.getType().getElemType(), Tab.intType)){
            report_error("[MapDesignator] Designator(" + arr.getName() + ") on the right side of MAP operand must represent an array of integer values", node);
        }
    }

    // </editor-fold>

    // <editor-fold desc="[Factor] Heap Allocation (arrays, objects and sets), Loading Constants, right operands">

    @Override
    public void visit(FactorDesignator node){
        int kind = node.getDesignator().obj.getKind();
        if (kind == Obj.Con || kind == Obj.Fld || kind == Obj.Var || kind == Obj.Elem)
            node.struct = node.getDesignator().obj.getType();
        else
            node.struct = Tab.noType;
    }

    @Override
    public void visit(FactorCreateArray node){
        if (!es.equals(node.getExpr().struct, Tab.intType)){
            report_error("[FactorCreateArray] Expression passed as array size must be of type int", node);
            node.struct = Tab.noType;
            return;
        }
        if (es.equals(node.getType().struct, setType)){
            node.struct = setType;
        }else{
            node.struct = new Struct(Struct.Array, currTypeVar);
        }

    }

    @Override
    public void visit(FactorCreateObject node){
        if (node.getType().struct == Tab.noType){
            report_error("[FactorCreateObject] Nonterminal Type must be a valid user-defined type to create an object from it", node);
            node.struct = Tab.noType;
            return;
        }
        if(node.getType().struct.getKind() != Struct.Class){
            report_error("[FactorCreateObject] Nonterminal Type must denote a class (user-defined type)", node);
            node.struct = Tab.noType;
            return;
        }
        if (currTypeVar == Tab.noType){
            report_error("[FactorCreateObject] Cannot create a class object from an invalid type", node);
            node.struct = Tab.noType;
            return;
        }
        node.struct = currTypeVar;
        report_info("Created object of class: " + currTypeName, node);
    }

    @Override
    public void visit(FactorExpr node){
        node.struct = node.getExpr().struct;
    }

    @Override
    public void visit(FactorConstDeclListValue node){
        node.struct = node.getConstDeclListValue().obj.getType();
    }


    @Override
    public void visit(NumConst node){
        node.obj = new Obj(Obj.Con, "numConst", Tab.intType, node.getN1(), 0);
    }

    @Override
    public void visit(CharConst charConst){
        charConst.obj = new Obj(Obj.Con, "charConst", Tab.charType, charConst.getC1(), 0);
    }

    @Override
    public void visit(BoolConst node){
        node.obj = new Obj(Obj.Con, "boolConst", boolType, node.getB1(), 0);
    }

    // </editor-fold>

    // <editor-fold desc="[Designator Statements, regular and while] function call, assignment, unary operations (inc, dec), set operations (union), left operands">

    @Override
    public void visit(DesignatorStatementUnarySemi node){
        int kind = node.getDesignator().obj.getKind();
        String name = node.getDesignator().obj.getName();
        if (kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld){

            report_error("[DesignatorStatementUnarySemi] Unary operation (-- or ++) on an invalid variable(" + name + ")", node);
            return;
        }
        else if(!es.equals(node.getDesignator().obj.getType(), Tab.intType)){
            report_error("[DesignatorStatementUnarySemi] Unary operation (-- or ++) on a variable(" + name + ") that is not of type int", node);
            return;
        }
    }

    @Override
    public void visit(DesignatorStatementUnarySemiWhile node){
        int kind = node.getDesignator().obj.getKind();
        String name = node.getDesignator().obj.getName();
        if (kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld){

            report_error("[DesignatorStatementUnarySemiWhile] Unary operation (-- or ++) on an invalid variable(" + name + ")", node);
            return;
        }
        else if(!es.equals(node.getDesignator().obj.getType(), Tab.intType)){
            report_error("[DesignatorStatementUnarySemiWhile] Unary operation (-- or ++) on a variable(" + name + ") that is not of type int", node);
            return;
        }
    }

    @Override
    public void visit(DesignatorStatementFuncCall node){
        Obj meth = node.getDesignator().obj;
        funccall(meth, node, "DesignatorStatementFuncCall");

    }

    @Override
    public void visit(DesignatorStatementFuncCallWhile node){
        if(node.getDesignator().obj.getKind() != Obj.Meth){
            report_error("[DesignatorStatementFuncCallWhile] Call to invalid method(" + node.getDesignator().obj.getName() + ")", node);
            return;
        }
        Obj meth = node.getDesignator().obj;
        funccall(meth, node, "DesignatorStatementFuncCallWhile");
    }

    @Override
    public void visit(DesignatorAssignExpr node){
        int kind = node.getDesignator().obj.getKind();
        if(kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld){
            report_error("[DesignatorAssignExpr] Assignment to an invalid variable: " + node.getDesignator().obj.getName(), node);
            return;
        }
        // it's important to use assignableTo because of assigning null properly
        else if(node.getExpr().struct == Tab.noType || !es.assignableTo(node.getExpr().struct, node.getDesignator().obj.getType())){
            report_error("[DesignatorAssignExpr] Expression type is not compatible with Designator type: " + node.getDesignator().obj.getName(), node );
            return;
        }

    }

    // we need to repeat this function because of error handling implementation
    @Override
    public void visit(DesignatorAssignExprWhile node){
        int kind = node.getDesignator().obj.getKind();
        if(kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld){
            report_error("[DesignatorAssignExprWhile] Assignment to an invalid variable: " + node.getDesignator().obj.getName(), node);
            return;
        }
        // it's important to use assignableTo because of assigning null propery
        else if(node.getExpr().struct == Tab.noType || !es.assignableTo(node.getExpr().struct, node.getDesignator().obj.getType())){
            report_error("[DesignatorAssignExprWhile] Expression type is not compatible with Designator type: " + node.getDesignator().obj.getName(), node );
            return;
        }
    }

    @Override
    public void visit(DesignatorAssignSetop node){
        Struct left = node.getDesignator().obj.getType();
        Struct middle = node.getDesignator1().obj.getType();
        Struct right = node.getDesignator2().obj.getType();
        if (!es.equals(left, setType) || !es.equals(middle, setType) || !es.equals(right, setType)){
            report_error("[DesignatorAssignSetop] All Designator terminals must be of type set", node);
            return;
        }
    }

    @Override
    public void visit(DesignatorAssignSetopWhile node){
        Struct left = node.getDesignator().obj.getType();
        Struct middle = node.getDesignator1().obj.getType();
        Struct right = node.getDesignator2().obj.getType();
        if (!es.equals(left, setType) || !es.equals(middle, setType) || !es.equals(right, setType)){
            report_error("[DesignatorAssignSetopWhile] All Designator terminals must be of type set", node);
            return;
        }
    }

    // </editor-fold>

    // <editor-fold desc="[Designator] (DesignatorVar + ClassAccess + ArrayAccess)">

    private void detectSymbol(Obj node, SyntaxNode syntaxNode){

        if (node == Tab.noObj) {
            report_error("[Designator] Symbol detection on non-existent symbol (noObj)", syntaxNode);
            return;
        }

        String message = "Detected symbol usage: ";
        StringBuilder builder = new StringBuilder(message);
        int kind = node.getKind();

        boolean skipLogSymbol = false;

        switch (kind){
            case Obj.Con:
                builder.append(" symbolic constant ");
                break;
            case Obj.Var:

                String parameterType;
                if (node.getFpPos() == FP_POS_FORMAL_PARAMETER){
                    parameterType = " formal";
                } else {
                    parameterType = " local";
                }

                switch (node.getLevel()){
                    case LEVEL_GLOBAL_VAR:
                        builder.append(" global variable ");
                        break;
                    case LEVEL_CLASS_VAR:
                        builder.append(parameterType);
                        builder.append(" class method parameter ");
                        break;
                    case LEVEL_GLOBAL_METH_VAR:
                        builder.append(parameterType);
                        builder.append(" global method parameter ");
                        break;
                    case LEVEL_INTERFACE_VAR:
                        builder.append(parameterType);
                        builder.append(" interface method parameter ");
                        break;
                    default:
                        builder.append(" unknown variable type ");
                        break;
                }
                break;
            case Obj.Fld:
                builder.append(" class field ");
                break;
            case Obj.Elem:
                builder.append(" array element ");
                break;
            case Obj.Meth:
                SyntaxNode parent = syntaxNode.getParent();
                if (parent instanceof FactorDesignator) skipLogSymbol = true;

                switch (node.getFpPos()){
                    case FP_POS_GLOBAL_METHOD:
                        builder.append(" global method ");
                        break;
                    case FP_POS_IMPLEMENTED_NONGLOBAL_METHOD:
                        builder.append(" implemented class or interface method ");
                        break;
                    case FP_POS_UNIMPLEMENTED_INTERFACE_METHOD:
                        builder.append(" unimplemented interface method ");
                        break;
                    default:
                        builder.append(" unknown method type ");
                        break;
                }
                break;
            default:
                builder.append(" unknown variable type ");
                break;
        }
        if (!skipLogSymbol)logSymbol(builder.toString(), node, syntaxNode);
    }

    @Override
    public void visit(DesignatorVar node){
        Obj var = Tab.find(node.getI1());
        if (var == Tab.noObj){
            report_error("[DesignatorVar] Variable with given name(" + node.getI1() + ") is not declared", node);
            node.obj = Tab.noObj;
            return;
        }
        else if (var.getKind() != Obj.Fld && var.getKind() != Obj.Var && var.getKind() != Obj.Con && var.getKind() != Obj.Meth){
            report_error("[DesignatorVar] Invalid variable type " + node.getI1(), node);
            node.obj = Tab.noObj;
            return;
        }
        node.obj = var;
        detectSymbol(node.obj, node);
    }

    // <editor-fold desc="[DesignatorClass] access Class properties">

    @Override
    public void visit(DesignatorPropertyAccess node){
        SyntaxNode parent = node.getParent();
        if (node.getDesignatorClassMore().obj.getKind() == Obj.Meth && !(parent instanceof DesignatorStatementFuncCall || parent instanceof FactorFuncCall || parent instanceof DesignatorStatementFuncCallWhile)){
            report_error("[DesignatorPropertyAccess] When using method name in chaining, it must be followed by a function call, i.e., method(...), not just method without parentheses", node);
            node.obj = Tab.noObj;
            return;
        }
        node.obj = node.getDesignatorClassMore().obj;
    }

    @Override
    public void visit(DesignatorElemPropertyAccess node){
        node.obj = node.getDesignatorClassMore().obj;
    }

    @Override
    public void visit(DesignatorClassName node){
        String name = node.getI1();
        Obj var = Tab.find(name);
        if (var == Tab.noObj){
            report_error("[DesignatorClassName] Access to undeclared class variable: " + name, node);
            node.obj = Tab.noObj;
            accessClass = Tab.noType;
            return;
        }
        if(!(var.getKind() == Obj.Var || var.getKind() == Obj.Fld && currClass != null ) ||
                (var.getType().getKind() != Struct.Class && var.getType().getKind() != Struct.Interface)){
            report_error("[DesignatorClassName] Access to invalid class variable: " + name, node);
            node.obj = Tab.noObj;
            accessClass = Tab.noType;
            return;
        }
        if(node.getI1().equals("this")){
            if (currClass == null && currInterface == null) {
                report_error("[DesignatorClassName] Access to this variable outside class or interface", node);
                node.obj = Tab.noObj;
                accessClass = Tab.noType;
                return;
            } else {
                thisVarDetectedFlag = true;
            }
        }
        node.obj = var;
        detectSymbol(node.obj, node);
        accessClass = var.getType();
    }

    @Override
    public void visit(DesignatorClassElem node){
        Obj arr = node.getDesignatorArrayName().obj;
        if (arr == Tab.noObj){
            node.obj = Tab.noObj;
            accessClass = Tab.noType;
            return;
        }
        else if (!es.equals(node.getExpr().struct, Tab.intType)){
            report_error("[DesignatorClassElem] Array index must be an int value", node);
            node.obj = Tab.noObj;
            accessClass = Tab.noType;
            return;
        }
        Obj var = new Obj(Obj.Elem, arr.getName() + "[$]", arr.getType().getElemType());
        if (var.getType().getKind() != Struct.Class){
            report_error("[DesignatorClassElem] Array element type("+ node.getDesignatorArrayName().obj.getName() +") being accessed must be a class", node);
            node.obj = Tab.noObj;
            accessClass = Tab.noType;
            return;
        }
        node.obj = var;
        detectSymbol(node.obj, node);
        accessClass = node.obj.getType();
    }

    private Obj searchMethod(Struct currClassType, String methodName){
        Struct curr = currClassType;
        for (Struct iface: curr.getImplementedInterfaces()){
            for (Obj member: iface.getMembers()) {
                if (member.getFpPos() == FP_POS_IMPLEMENTED_NONGLOBAL_METHOD && member.getName().equals(methodName)) {
                    return member;
                }
            }
        }
        while (curr != null && curr != Tab.noType){
            for (Obj member: curr.getMembers()){
                if(member.getKind() == Obj.Meth && member.getName().equals(methodName)){
                    return member;
                }
            }
            curr = curr.getElemType();
        }
        return Tab.noObj;
    }

    private boolean isParentPropertyAccess (SyntaxNode parent){
        return parent instanceof DesignatorPropertyAccess || parent instanceof DesignatorElemPropertyAccess;
    }

    private Collection<Obj> getClassMembers(Struct classStruct){
        if (thisVarDetectedFlag){
            thisVarDetectedFlag = false;
            return Tab.currentScope().getOuter().getLocals().symbols();
        } else {
            return classStruct.getMembers();
        }
    }

    private Obj findDesignatorClassMoreIdent (Struct classStruct, SyntaxNode node, String field){
        if (classStruct == Tab.noType){
            return Tab.noObj;
        }

        // we need this check in case of this.field access (bcs Obj nodes are not chained to class node)
        Collection<Obj> members = getClassMembers(classStruct);
        if (members == null){
            // not found (we don't have Obj nodes in outer scope)
            return null;
        }

        SyntaxNode parent = node.getParent();
        for (Obj member: members){
            if ((member.getKind() == Obj.Fld || member.getKind() == Obj.Meth && isParentPropertyAccess(parent)) && member.getName().equals(field)){
                return member;
            }
        }
        // case Obj.Meth
        Obj mem = searchMethod(classStruct, field);
        if ((mem != Tab.noObj) && isParentPropertyAccess(parent)){
            return mem;
        }

        // not fount
        return null;
    }

    private Obj findDesignatorClassMoreElem (Struct classStruct, SyntaxNode node, String field, Struct exprStruct){
        if (classStruct == Tab.noType){
            return Tab.noObj;
        }
        if (!es.equals(exprStruct, Tab.intType)){
            report_error("[DesignatorClassMoreFinalElem] Array index must be of type int", node);
            return Tab.noObj;
        }

        // we need this check in case of this.field access (bcs Obj nodes are not chained to class node)
        Collection<Obj> members = getClassMembers(classStruct);
        if (members == null){
            // not found (we don't have Obj nodes in outer scope)
            return null;
        }

        // if we have this.field we only want to search within class scope (not methods scope) but only for first search
        for (Obj member: members){
            if(member.getType().getKind() == Struct.Array && member.getName().equals(field)){
                return member;
            }
        }
        // not found
        return null;
    }

    @Override
    public void visit(DesignatorClassMoreFinalElem node){
        Struct classStruct = accessClass;
        String field = node.getDesignatorClassArrayName().getI1();

        Obj res = findDesignatorClassMoreElem(classStruct, node, field, node.getExpr().struct);
        accessClass = null;

        if (res != null){
            if (res == Tab.noObj) node.obj = Tab.noObj;
            else node.obj = new Obj(Obj.Elem, res.getName() + "[$]", res.getType().getElemType());
            node.getDesignatorClassArrayName().obj = res;

            detectSymbol(node.getDesignatorClassArrayName().obj, node);
            detectSymbol(node.obj, node);
            return;
        }
        report_error("[DesignatorClassMoreFinalElem] Field(" + field + ") does not exist as a class field", node);
        node.obj = Tab.noObj;
        node.getDesignatorClassArrayName().obj = Tab.noObj;
    }

    @Override
    public void visit(DesignatorClassMoreNotFinal node){
        Struct classStruct = node.getDesignatorClassMore().obj.getType();
        // if we have this.field we only want to search within class scope (not methods scope) but only for first search
        Obj res = findDesignatorClassMoreIdent(classStruct, node, node.getI2());
        accessClass = null;
        if (res != null){
            node.obj = res;
            detectSymbol(node.obj, node);
            return;
        }
        report_error("[DesignatorClassMoreNotFinal] Field(" + node.getI2() + ") does not exist as a class field", node);
        node.obj = Tab.noObj;
    }

    @Override
    public void visit(DesignatorClassMoreNotFinalElem node){
        Struct classStruct = node.getDesignatorClassMore().obj.getType();
        String field = node.getDesignatorClassArrayName().getI1();

        Obj res = findDesignatorClassMoreElem(classStruct, node, field, node.getExpr().struct);
        accessClass = null;

        if (res != null){
            if (res == Tab.noObj) node.obj = Tab.noObj;
            else node.obj = new Obj(Obj.Elem, res.getName() + "[$]", res.getType().getElemType());
            node.getDesignatorClassArrayName().obj = res;

            detectSymbol(node.getDesignatorClassArrayName().obj, node);
            detectSymbol(node.obj, node);
            return;
        }
        report_error("[DesignatorClassMoreNotFinalElem] Field(" + field + ") does not exist as a class field", node);
        node.obj = Tab.noObj;
        node.getDesignatorClassArrayName().obj = Tab.noObj;
    }

    @Override
    public void visit(DesignatorClassMoreFinal node){
        Struct classStruct = accessClass;
        Obj res = findDesignatorClassMoreIdent(classStruct, node, node.getI1());
        accessClass = null;

        if (res != null){
            node.obj = res;
            detectSymbol(node.obj, node);
            return;
        }
        report_error("[DesignatorClassMoreFinal] Field(" + node.getI1() + ") does not exist as a class field", node);
        node.obj = Tab.noObj;
    }

    // </editor-fold>

    // <editor-fold desc="[DesignatorArray] access Arrays">

    @Override
    public void visit(DesignatorArrayName node){
        Obj arr = Tab.find(node.getI1());
        if (arr == Tab.noObj){
            report_error("[DesignatorArrayName] Array variable with name(" + node.getI1() + ") is not declared", node);
            node.obj = Tab.noObj;
            return;
        }
        if (!(arr.getKind() == Obj.Var || arr.getKind() == Obj.Fld && currClass != null ) ||
                arr.getType().getKind() != Struct.Array){
            report_error("[DesignatorArrayName] Invalid array variable type: " + node.getI1(), node);
            node.obj = Tab.noObj;
            return;
        }
        node.obj = arr;
        detectSymbol(node.obj, node);
    }

    @Override
    public void visit(DesignatorClassArrayName node){

    }

    @Override
    public void visit(DesignatorElem node){
        Obj arr = node.getDesignatorArrayName().obj;
        if (arr == Tab.noObj){
            node.obj = Tab.noObj;
            return;
        }
        else if (!es.equals(node.getExpr().struct, Tab.intType)){
            report_error("[DesignatorElem] Array index must be an int value", node);
            node.obj = Tab.noObj;
            return;
        }
        node.obj = new Obj(Obj.Elem, arr.getName() + "[$]", arr.getType().getElemType());
        detectSymbol(node.obj, node);
    }

    // </editor-fold>


    // </editor-fold>

    // <editor-fold desc="[Factor] Function calls">

    private void checkArePassedParametersAndFormalParameterListCompatible(List<Struct> fpList, String methName,  SyntaxNode node, String nodeName){
        if (fpList.size() != apStack.size()){
            report_error("[" + nodeName + "] Parameter list does not match the parameters passed when calling method " + methName + " by number of parameters passed(" + apStack.size() + "), this method accepts: " + fpList.size() + " parameters", node);

            apStack.clear();
//            return false;
            return;
        }

        boolean errorHappened = false;
        for (int i = 0; i < fpList.size(); i++){
            Struct fpListElem = fpList.get(i);
            Struct apStackElem = apStack.pop();
            if (!es.assignableTo(apStackElem, fpListElem)){
                report_error("[" + nodeName + "] Parameter passed at position: " + (i + 1) + "(indexed from 1) is not compatible with corresponding formal parameter of method " + methName + " by type", node);

                errorHappened = true;
            }
        }
        if (errorHappened){
            apStack.clear();
//            return false;
            return;
        }
//        return true;
        return;
    }

    private List<Struct> getFormalParameters(Obj funcNode, SyntaxNode node){
        List<Struct> fpList = new ArrayList<>();
        for (Obj localSym: funcNode.getLocalSymbols()){
            if (localSym.getKind() == Obj.Var && localSym.getFpPos() == FP_POS_FORMAL_PARAMETER && localSym.getLevel() >= 1 && !localSym.getName().equals("this")){
                fpList.add(localSym.getType());
            }
        }
        return fpList;
    }

    private Struct funccall(Obj meth, SyntaxNode node, String nodeName){
        if (meth == Tab.noObj){
            return Tab.noType;
        }
        else if (meth.getKind() != Obj.Meth){
            report_error("[" + nodeName + "] Invalid variable type (" + meth.getName() + " must be a method)", node);
            return Tab.noType;
        }

        List<Struct> fpList = getFormalParameters(meth, node);
        checkArePassedParametersAndFormalParameterListCompatible(fpList, meth.getName(), node, nodeName);

        return meth.getType();
    }


    @Override
    public void visit(FactorFuncCall node){
        Obj meth = node.getDesignator().obj;
        node.struct = funccall(meth, node, "FactorFuncCall");
    }

    // function call parameters
    @Override
    public void visit(StackInitialize node){
        apStack.clear();
    }

    @Override
    public void visit(ActParsMultipleItems node){
        apStack.push(node.getExpr().struct);
    }

    @Override
    public void visit(ActParsSigleItem node){
        apStack.push(node.getExpr().struct);
    }

    // </editor-fold>

    // <editor-fold desc="Conditional Statements If-Else">

    @Override
    public void visit(StatementConditionCondition node){
        node.struct = node.getCondition().struct;
    }

    @Override
    public void visit(ConditionOr node){
        if (!es.equals(node.getCondition().struct, boolType) || !es.equals(node.getCondTerm().struct, boolType)){
            report_error("[ConditionOr] Condition must be of type bool", node);
            node.struct = Tab.noType;
            return;
        }

        node.struct = node.getCondition().struct;
    }

    @Override
    public void visit(ConditionCondTerm node){
        if (!es.equals(node.getCondTerm().struct, boolType)){
            report_error("[ConditionCondTerm] Condition must be of type bool", node);
            node.struct = Tab.noType;
            return;
        }

        node.struct = node.getCondTerm().struct;
    }

    @Override
    public void visit(CondTermAnd node){
        if (!es.equals(node.getCondFact().struct, boolType) || !es.equals(node.getCondTerm().struct, boolType)){
            report_error("[CondTermAnd] Condition must be of type bool", node);
            node.struct = Tab.noType;
            return;
        }

        node.struct = node.getCondFact().struct;
    }

    @Override
    public void visit(CondTermCondFact node){
        node.struct = node.getCondFact().struct;
    }

    @Override
    public void visit(CondFactRelop node){
        Struct left = node.getExpr().struct;
        Struct right = node.getExpr1().struct;
        // for case if (true && a < 2){...} - a and 2 only needs to be comparable not necessarily boolType
        if (!es.compatibleWith(left, right)){
            report_error("[CondFactRelop] Logical operands are not compatible", node);
            node.struct = Tab.noType;
            return;
        }
        if (es.isRefType(left) || es.isRefType(right)){
            if (!(node.getRelop() instanceof RelopEqual) && !(node.getRelop() instanceof RelopNotEqual)){
                report_error("[CondFactRelop] With class or array type variables, only != and == relational operators can be used", node);
                node.struct = Tab.noType;
                return;
            }
        }

        node.struct = boolType;
    }

    @Override
    public void visit(CondFactExpr node){
        node.struct = node.getExpr().struct;
    }

    // </editor-fold>

    // <editor-fold desc="Conditional Statements While">

    @Override
    public void visit(DoStatement node){
        loopCounter++;
    }

    @Override
    public void visit(StatementLoopSimple node){
        loopCounter--;
    }

    @Override
    public void visit(StatementLoopCondition node){
        loopCounter--;
    }

    @Override
    public void visit(StatementLoopComplex node){
        loopCounter--;
    }

    @Override
    public void visit(StatementBreak node){
        if(loopCounter == 0){
            report_error("[StatementBreak] Break statement cannot be called outside of a while loop", node);
            return;
        }
    }

    @Override
    public void visit(StatementContinue node){
        if(loopCounter == 0){
            report_error("[StatementContinue] Continue statement cannot be called outside of a while loop", node);
            return;
        }
    }

    // </editor-fold>

    // <editor-fold desc="Interface declaration">

    private void closeInterface(){
        if (currInterface != Tab.noType){
            Tab.chainLocalSymbols(currInterface);
        }
        Tab.closeScope();
        currInterface = null;
        classMethodDecl = false;
    }

    @Override
    public void visit(InterfaceDecl node){
        node.struct = node.getInterfaceDeclName().struct;
        closeInterface();
    }

    @Override
    public void visit(InterfaceDeclName node){
        if(checkIsObjNodeDeclared(node.getI1())){
            report_error("[InterfaceDeclName] Variable with name: " + node.getI1() + " is already declared", node);
            currInterface = Tab.noType;
        } else {
            currInterface = new Struct(Struct.Interface);
            Tab.insert(Obj.Type, node.getI1(), currInterface);
        }
        node.struct = currInterface;
        classMethodDecl = true;
        Tab.openScope();
        Tab.insert(Obj.Fld, "$tvf", Tab.intType);
    }

    // </editor-fold>

    // <editor-fold desc="Class declaration">

    private void checkIfAllMethodsAreImplemented(SyntaxNode node){
        for(Obj member: currClass.getMembers()){
            if (member.getFpPos() == FP_POS_UNIMPLEMENTED_INTERFACE_METHOD){
                report_error("[checkIfAllMethodsAreImplemented] Method " + member.getName() + " of the interface is not implemented within the class that extends it", node);
                return;
            }
        }
    }

    private void closeClass(SyntaxNode node){
        if (currClass != Tab.noType){
            Tab.chainLocalSymbols(currClass);
            checkIfAllMethodsAreImplemented(node);
        }
        Tab.closeScope();
        currClass = null;
        classMethodDecl = false;
        parentClass = Tab.noType;
    }

    @Override
    public void visit(ClassDeclName node){
        if(checkIsObjNodeDeclared(node.getI1())){
            report_error("[ClassDeclName] Variable with name: " + node.getI1() + " is already declared", node);
            currClass = Tab.noType;
        } else {
            currClass = new Struct(Struct.Class);
            Tab.insert(Obj.Type, node.getI1(), currClass);
        }
        node.struct = currClass;
        Tab.openScope();
        Tab.insert(Obj.Fld, "$tvf", Tab.intType);
    }

    @Override
    public void visit(ClassNoExtend node){
        node.struct = currClass;
        closeClass(node);
    }

    @Override
    public void visit(ClassYesExtend node){
        node.struct = currClass;
        closeClass(node);
    }

    @Override
    public void visit(ClassNoExtendYesMethods node){
        node.struct = currClass;
        closeClass(node);
    }

    @Override
    public void visit(ClassYesExtendYesMethods node){
        node.struct = currClass;
        closeClass(node);
    }

    @Override
    public void visit(ExtendsClass node){
        Struct n = node.getType().struct;
        node.struct = currClass;
        if (n.getKind() != Struct.Class && n.getKind() != Struct.Interface){
            report_error("[ExtendsClass] Nonterminal Type must denote a class or interface (user-defined type)", node);
            parentClass = Tab.noType;
            return;
        }

        parentClass = n;
        if (node.getType().struct.getKind() == Struct.Class){
            currClass.setElementType(n);
            for (Struct implementedInterface: parentClass.getImplementedInterfaces()){
                currClass.addImplementedInterface(implementedInterface);
            }
        }
        else{
            currClass.addImplementedInterface(parentClass);
        }

        for (Obj member: parentClass.getMembers()){
            if(member.getKind() == Obj.Fld){
                // insert only parent class fields
                Tab.currentScope().addToLocals(member);
            }
        }
    }

    // </editor-fold>

}
