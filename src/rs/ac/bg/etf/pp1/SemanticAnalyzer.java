package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.structure.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class SemanticAnalyzer extends VisitorAdaptor{

    static final int FP_POS_GLOBAL_METHOD = 0; // by default fp pos is set to 0
    static final int FP_POS_IMPLEMENTED_NONGLOBAL_METHOD = 1;
    static final int FP_POS_UNIMPLEMENTED_INTERFACE_METHOD = 2;

    static final int FP_POS_FORMAL_PARAMETER = 1;

    static final int LEVEL_INTERFACE_VAR = 3; // interface method variables
    static final int LEVEL_CLASS_FLD = 1; // class field variables
    static final int LEVEL_CLASS_VAR = 2; // class method variables
    static final int LEVEL_GLOBAL_METH_VAR = 1; // global method variables
    static final int LEVEL_GLOBAL_VAR = 0; // global variables

    // package rs.ac.bg.etf.pp1;
    int nVars;

    public boolean errorDetected = false;
    private boolean classMethodDecl = false; // used to check if we are in class method declaration or not
    private boolean voidMethodFlag = false; // used to check if method is void or not
    private boolean returnExprFlag = false; // used to check if method has return expression, no need to check if it is simple return because we do all checks in statementReturn visitor
    private boolean thisVarDetectedFlag = false;

    private Struct currTypeVar = Tab.noType; // Tab.noType - invalid type
    private Struct currTypeMeth = null; // null - void method, Tab.noType - invalid return type
    private String currTypeName = "";

    private Obj mainMeth = null;
    private Obj currMeth = null; // for creating method
    private Struct currClass = null; // for creating class
    private Struct accessClass = null; // for chaining
    private Struct currInterface = null; // for creating interface
    private Struct parentClass = Tab.noType; // parent class for currClass (can be class or interface)

    public static Struct boolType = Tab.find("bool").getType();
    public static Struct setType = Tab.find("set").getType();

    private boolean parsingFormPars = false;

    private int loopCounter = 0;

    private static final String[] objKindNames = { "Con", "Var", "Type", "Meth", "Fld", "Elem", "Prog" };
    private static final String[] structKindNames = { "None", "Int", "Char", "Array", "Class", "Bool", "Set", "Interface" };

    private final Stack<Struct> apStack = new Stack<>();

    private final ExtendedStruct es = ExtendedStruct.getInstance();
    Logger log = Logger.getLogger(getClass());

    // <editor-fold desc="log methods">

    public void report_error(String message, SyntaxNode info) {
        errorDetected = true;
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0: info.getLine();
        if (line != 0)
            msg.append (" na liniji ").append(line);
        log.error(msg.toString());
    }

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0: info.getLine();
        if (line != 0)
            msg.append (" na liniji ").append(line);
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
            String tmp = " (linija " + node.getLine() + ")";
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
        nVars = Tab.currentScope().getnVars();
        Tab.chainLocalSymbols(node.getProgName().obj);
        Tab.closeScope();

        if(mainMeth == null){
            report_error("[Program] Main metoda mora biti definisana", node);
        }

        if(mainMeth != null && mainMeth.getLevel() > 0){
            report_error("[Program] Main metoda mora biti deklarisana bez formalnih parametara", node);
        }
    }

    @Override
    public void visit(ProgName node){
        node.obj = Tab.insert(Obj.Prog, node.getProgName(), Tab.noType);
        Tab.openScope();
    }

    @Override
    public void visit(TypeIdent node){
        // returns NoObj if type is not found and Obj node if type is found
        Obj typeNode = Tab.find(node.getI1());
        if(typeNode == Tab.noObj){
            report_error("[TypeIdent] Nije pronadjen tip " + node.getI1() + " u tabeli simbola", node);
            currTypeVar = Tab.noType;
            node.struct = Tab.noType;
            return;
        }
        if(Obj.Type != typeNode.getKind()){
            report_error("[TypeIdent] Ime " + node.getI1() + " ne predstavlja tip", node);
            currTypeVar = Tab.noType;
            node.struct = Tab.noType;
            return;
        }
        node.struct = typeNode.getType();
        currTypeVar = typeNode.getType();
        currTypeName = node.getI1();
    }

    // </editor-fold>

    // <editor-fold desc="Const and Var declarations">

    @Override
    public void visit(ConstDeclAssign node){
        if(checkIsObjNodeDeclared(node.getI1())){
            report_error("[ConstDeclAssign] Vec je deklarisana konstanta sa imenom " + node.getI1(), node);
            return;
        }

        Obj constObj = node.getConstDeclListValue().obj;
        Obj constNode = Tab.insert(Obj.Con, node.getI1(), currTypeVar);

        if (!es.equals(constObj.getType(), currTypeVar)){
            report_error("[ConstDeclAssign] Deklariani tip konstante i vrednost koja se dodeljuje nisu kompatibilni", node);
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
            report_error("[VarDeclFinalVar] Vec je deklarisana promenljiva sa imenom: " + node.getI1(), node);
            return;
        }
        node.obj = insertNewVarNode(node.getI1(), currTypeVar);
//        logSymbol("detektovan Obj cvor", varNode, node);
    }

    @Override
    public void visit(VarDeclFinalArray node){
        if(checkIsObjNodeDeclared(node.getI1())){
            report_error("[VarDeclFinalArray] Vec je deklarisana promenljiva sa imenom " + node.getI1(), node);
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
            report_error("[createMethodObjNode] Vec je definisan metod sa imenom " + name, null);
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
            report_error("[NoVoidMethod] Nije pronadjen tip " + node.getI1() + " u tabeli simbola", node);
            currTypeVar = Tab.noType;
            return;
        }
        if(Obj.Type != typeNode.getKind()){
            report_error("[NoVoidMethod] Ime " + node.getI1() + " ne predstavlja tip", node);
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
            report_error("Vec je definisan metod sa imenom " + node.getI1(), node);
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
                report_error("[MainMethod] Main metoda mora biti povratnog tipa void", node.getParent());
            }
        } else {
            report_error("[MainMethod] Vec je definisan main metod", node.getParent());
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
            report_error("[MethodDecl] U metodu("+ currMeth.getName() +") povratnog tipa koji nije void se mora pojaviti barem jedna return naredba koja vraca vrednost", node);
        }
        closeMethod();
    }

    @Override
    public void visit(StatementReturn node){
        if (currMeth == null){
            report_error("[StatementReturn] Return naredba se moze pozivati samo unutar tela metode", node);
            return;
        }
        if (!voidMethodFlag){
            report_error("[StatementReturn] U metodu("+ currMeth.getName() +") povratnog tipa koji nije void svaka return naredba mora da vraca vrednost", node);
        }
    }

    @Override
    public void visit(StatementReturnExpr node){
        if (currMeth == null){
            report_error("[StatementReturnExpr] Return naredba se moze pozivati samo unutar tela metode", node);
            return;
        }
        if (voidMethodFlag){
            report_error("[StatementReturnExpr] U metodu("+ currMeth.getName() +") povratnog tipa koji je void ne moze se vratiti vrednost", node);
            returnExprFlag = true;
            return;
        }
        if (!es.equals(currMeth.getType(), node.getExpr().struct)){
            report_error("[StatementReturnExpr] Povratni tip metoda i tip koji vraca return naredba nisu kompatibilni", node);
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
            report_error("[StatementRead] Read operacija nad neadekvatnom promenljivom(" + name + ")", node);
            return;
        }
        else if(!es.equals(type, Tab.intType) && !es.equals(type, boolType) && !es.equals(type, Tab.charType)){
            report_error("[StatementRead] Read operacija nad promenljivom("+ name + ") koja nije tipa int, char ili bool", node);
            return;
        }
    }

    @Override
    public void visit(StatementPrint node){
        Struct type = node.getExpr().struct;

        if(!es.equals(type, Tab.intType) && !es.equals(type, boolType) && !es.equals(type, Tab.charType) && !es.equals(type, setType)){
            report_error("[StatementPrint] Print operacija nad izrazom koji nije tipa int, char ili bool", node);
            return;
        }
    }

    @Override
    public void visit(StatementPrintNumber node){
        Struct type = node.getExpr().struct;
        if(!es.equals(type, Tab.intType) && !es.equals(type, boolType) && !es.equals(type, Tab.charType) && !es.equals(type, setType)){
            report_error("[StatementPrintNumber] Print operacija nad izrazom koji nije tipa int, char ili bool", node);
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
            report_error("[TermTermMulopFactor] Mulop operator zahteva da oba operanda budu int vrednosti.", node);
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
            report_error("[ExprMinusTerm] Minus mozemo staviti samo ispred operanda koji je tipa int", node);
            node.struct = Tab.noType;
            return;
        }
        node.struct = node.getTerm().struct;
    }

    @Override
    public void visit(ExprAddopTerm node){
        if(!es.equals(node.getTerm().struct, Tab.intType) || !es.equals(node.getExpr().struct, Tab.intType)){
            report_error("[ExprAddopTerm] Addop operator zahteva da oba operanda budu int vrednosti", node);
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
            report_error("[ExprDesignatorMap] Designator(" + meth.getName() + ") sa leve strane MAP operanda mora biti metoda", node);
            node.struct = Tab.noType;
            return;
        }
        List<Struct> fpList = getFormalParameters(meth, node);
        if (fpList.size() != 1){
            report_error("[ExprDesignatorMap] Metoda sa leve strane MAP operanda mora imati tacno jedan parametar", node);
            node.struct = Tab.noType;
            return;
        }
        if(!es.equals(fpList.get(0), Tab.intType)){
            report_error("[ExprDesignatorMap] Parametar metoda sa leve strane MAP operanda mora biti tipa int", node);
            node.struct = Tab.noType;
            return;
        }
        if(!es.equals(meth.getType(), Tab.intType)){
            report_error("[ExprDesignatorMap] Povratna vrednost metoda sa leve strana MAP operanda mora biti tipa int", node);
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
            report_error("[MapDesignator] Designator(" + arr.getName() + ") sa desne strane operanda MAP mora predstavljati niz celobrojnih vrednosti", node);
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
            report_error("[FactorCreateArray] Izraz prosledjen kao velicina niza mora biti int tipa", node);
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
            report_error("[FactorCreateObject] Neterminal Type mora da bude validan korisnicki tip da bismo od njega kreirali objekat", node);
            node.struct = Tab.noType;
            return;
        }
        if(node.getType().struct.getKind() != Struct.Class){
            report_error("[FactorCreateObject] Neterminal Type mora da oznacava klasu (korisnicki definisan tip)", node);
            node.struct = Tab.noType;
            return;
        }
        if (currTypeVar == Tab.noType){
            report_error("[FactorCreateObject] Ne moze se kreirati objekat klase od nevalidnog tipa", node);
            node.struct = Tab.noType;
            return;
        }
        node.struct = currTypeVar;
        report_info("Kreiran objekat klase: " + currTypeName, node);
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

            report_error("[DesignatorStatementUnarySemi] Unarna operacija (-- ili ++) nad neadekvatnom promenljivom(" + name + ")", node);
            return;
        }
        else if(!es.equals(node.getDesignator().obj.getType(), Tab.intType)){
            report_error("[DesignatorStatementUnarySemi] Unarna operacija (-- ili ++) nad promenljivom("+ name + ") koja nije tipa int", node);
            return;
        }
    }

    @Override
    public void visit(DesignatorStatementUnarySemiWhile node){
        int kind = node.getDesignator().obj.getKind();
        String name = node.getDesignator().obj.getName();
        if (kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld){

            report_error("[DesignatorStatementUnarySemiWhile] Unarna operacija (-- ili ++) nad neadekvatnom promenljivom(" + name + ")", node);
            return;
        }
        else if(!es.equals(node.getDesignator().obj.getType(), Tab.intType)){
            report_error("[DesignatorStatementUnarySemiWhile] Unarna operacija (-- ili ++) nad promenljivom("+ name + ") koja nije tipa int", node);
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
            report_error("[DesignatorStatementFuncCallWhile] Poziv neadekvatne metode(" + node.getDesignator().obj.getName() + ")", node);
            return;
        }
        Obj meth = node.getDesignator().obj;
        funccall(meth, node, "DesignatorStatementFuncCallWhile");
    }

    @Override
    public void visit(DesignatorAssignExpr node){
        int kind = node.getDesignator().obj.getKind();
        if(kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld){
            report_error("[DesignatorAssignExpr] Dodela u neadekvatnu promenljivu : " + node.getDesignator().obj.getName(), node);
            return;
        }
        // it's important to use assignableTo because of assigning null properly
        else if(node.getExpr().struct == Tab.noType || !es.assignableTo(node.getExpr().struct, node.getDesignator().obj.getType())){
            report_error("[DesignatorAssignExpr] Tip Expr nije kompatibilan sa tipom neterminala Designator : " + node.getDesignator().obj.getName(), node );
            return;
        }

    }

    // we need to repeat this function because of error handling implementation
    @Override
    public void visit(DesignatorAssignExprWhile node){
        int kind = node.getDesignator().obj.getKind();
        if(kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld){
            report_error("[DesignatorAssignExprWhile] Dodela u neadekvatnu promenljivu : " + node.getDesignator().obj.getName(), node);
            return;
        }
        // it's important to use assignableTo because of assigning null propery
        else if(node.getExpr().struct == Tab.noType || !es.assignableTo(node.getExpr().struct, node.getDesignator().obj.getType())){
            report_error("[DesignatorAssignExprWhile] Tip Expr nije kompatibilan sa tipom neterminala Dedsignator : " + node.getDesignator().obj.getName(), node );
            return;
        }
    }

    @Override
    public void visit(DesignatorAssignSetop node){
        Struct left = node.getDesignator().obj.getType();
        Struct middle = node.getDesignator1().obj.getType();
        Struct right = node.getDesignator2().obj.getType();
        if (!es.equals(left, setType) || !es.equals(middle, setType) || !es.equals(right, setType)){
            report_error("[DesignatorAssignSetop] Svi Designator neterminali moraju biti tipa set", node);
            return;
        }
    }

    @Override
    public void visit(DesignatorAssignSetopWhile node){
        Struct left = node.getDesignator().obj.getType();
        Struct middle = node.getDesignator1().obj.getType();
        Struct right = node.getDesignator2().obj.getType();
        if (!es.equals(left, setType) || !es.equals(middle, setType) || !es.equals(right, setType)){
            report_error("[DesignatorAssignSetopWhile] Svi Designator neterminali moraju biti tipa set", node);
            return;
        }
    }

    // </editor-fold>

    // <editor-fold desc="[Designator] (DesignatorVar + ClassAccess + ArrayAccess)">

    private void detectSymbol(Obj node, SyntaxNode syntaxNode){

        if (node == Tab.noObj) {
            report_info("[Designator] Detektovanje simbola nad nepostojećim simbolom (noObj)", syntaxNode);
            return;
        }

        String message = "Detektovano koriscnje simbola: ";
        StringBuilder builder = new StringBuilder(message);
        int kind = node.getKind();

        boolean skipLogSymbol = false;

        switch (kind){
            case Obj.Con:
                builder.append(" simbolicka konstanta ");
                break;
            case Obj.Var:

                String parameterType;
                if (node.getFpPos() == FP_POS_FORMAL_PARAMETER){
                    parameterType = " formalni";
                } else {
                    parameterType = " lokalni";
                }

                switch (node.getLevel()){
                    case LEVEL_GLOBAL_VAR:
                        builder.append(" globalna promenljiva ");
                        break;
                    case LEVEL_CLASS_VAR:
                        builder.append(parameterType);
                        builder.append(" parametar metode klase ");
                        break;
                    case LEVEL_GLOBAL_METH_VAR:
                        builder.append(parameterType);
                        builder.append(" parametar globalne metode ");
                        break;
                    case LEVEL_INTERFACE_VAR:
                        builder.append(parameterType);
                        builder.append(" parametar metode interfejsa ");
                        break;
                    default:
                        builder.append(" nepoznata vrsta promenljive ");
                        break;
                }
                break;
            case Obj.Fld:
                builder.append(" polje klase ");
                break;
            case Obj.Elem:
                builder.append(" element niza ");
                break;
            case Obj.Meth:
                SyntaxNode parent = syntaxNode.getParent();
                if (parent instanceof FactorDesignator) skipLogSymbol = true;

                switch (node.getFpPos()){
                    case FP_POS_GLOBAL_METHOD:
                        builder.append(" globalna metoda ");
                        break;
                    case FP_POS_IMPLEMENTED_NONGLOBAL_METHOD:
                        builder.append(" implementirana metoda klase ili interfejsa ");
                        break;
                    case FP_POS_UNIMPLEMENTED_INTERFACE_METHOD:
                        builder.append(" neimplementirana metoda interfejsa ");
                        break;
                    default:
                        builder.append(" nepoznata vrsta metode ");
                        break;
                }
                break;
            default:
                builder.append(" nepoznata vrsta promenljive ");
                break;
        }
        if (!skipLogSymbol)logSymbol(builder.toString(), node, syntaxNode);
    }

    @Override
    public void visit(DesignatorVar node){
        Obj var = Tab.find(node.getI1());
        if (var == Tab.noObj){
            report_error("[DesignatorVar] Nije deklarisana promenljiva sa zadatim imenom(" + node.getI1() + ")", node);
            node.obj = Tab.noObj;
            return;
        }
        else if (var.getKind() != Obj.Fld && var.getKind() != Obj.Var && var.getKind() != Obj.Con && var.getKind() != Obj.Meth){
            report_error("[DesignatorVar] Neadekvatna vrsta promenljive " + node.getI1(), node);
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
            report_error("[DesignatorPropertyAccess] Ukoliko koristimo ime metoda prilikom ulancavanja, ono iza sebe mora imati i poziv funkcije, dakle metoda(...), a ne samo metoda bez zagrada", node);
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
            report_error("[DesignatorClassName] Pristup nedeklarisanoj promenljivoj klase: " + name, node);
            node.obj = Tab.noObj;
            accessClass = Tab.noType;
            return;
        }
        if(!(var.getKind() == Obj.Var || var.getKind() == Obj.Fld && currClass != null ) ||
                (var.getType().getKind() != Struct.Class && var.getType().getKind() != Struct.Interface)){
            report_error("[DesignatorClassName] Pristup neadekvatnoj promenljivoj klase: " + name, node);
            node.obj = Tab.noObj;
            accessClass = Tab.noType;
            return;
        }
        if(node.getI1().equals("this")){
            if (currClass == null && currInterface == null) {
                report_error("[DesignatorClassName] Pristup this promenljivoj van klase ili interfejsa", node);
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
            report_error("[DesignatorClassElem] Indeks niza mora biti int vrednost", node);
            node.obj = Tab.noObj;
            accessClass = Tab.noType;
            return;
        }
        Obj var = new Obj(Obj.Elem, arr.getName() + "[$]", arr.getType().getElemType());
        if (var.getType().getKind() != Struct.Class){
            report_error("[DesignatorClassElem] Tip elementa niza("+ node.getDesignatorArrayName().obj.getName() +") kojem se pristupa mora biti klasa", node);
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
            report_error("[DesignatorClassMoreFinalElem] Indeks niza mora biti tipa int", node);
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
        report_error("[DesignatorClassMoreFinalElem] Ovo polje("+ field +") ne postoji kao polje klase", node);
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
        report_error("[DesignatorClassMoreNotFinal] Ovo polje("+ node.getI2() +") ne postoji kao polje klase", node);
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
        report_error("[DesignatorClassMoreNotFinalElem] Ovo polje("+ field +") ne postoji kao polje klase", node);
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
        report_error("[DesignatorClassMoreFinal] Ovo polje("+ node.getI1() +") ne postoji kao polje klase", node);
        node.obj = Tab.noObj;
    }

    // </editor-fold>

    // <editor-fold desc="[DesignatorArray] access Arrays">

    @Override
    public void visit(DesignatorArrayName node){
        Obj arr = Tab.find(node.getI1());
        if (arr == Tab.noObj){
            report_error("[DesignatorArrayName] Nije deklarisana promenljiva niza sa imenom(" + node.getI1() + ")", node);
            node.obj = Tab.noObj;
            return;
        }
        if (!(arr.getKind() == Obj.Var || arr.getKind() == Obj.Fld && currClass != null ) ||
                arr.getType().getKind() != Struct.Array){
            report_error("[DesignatorArrayName] Neadekvatna vrsta promenljive niza : " + node.getI1(), node);
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
            report_error("[DesignatorElem] Indeks niza mora biti int vrednost", node);
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
            report_error("["+ nodeName +"] Lista prosledjenih parametara se ne poklapa se parametrima koji su prosledjeni prilikom poziva metode " + methName + " po broju prosledjenih parametara("+ apStack.size() +"), ova metoda prima: " + fpList.size() + " parametara", node);

            apStack.clear();
//            return false;
            return;
        }

        boolean errorHappened = false;
        for (int i = 0; i < fpList.size(); i++){
            Struct fpListElem = fpList.get(i);
            Struct apStackElem = apStack.pop();
            if (!es.assignableTo(apStackElem, fpListElem)){
                report_error("["+ nodeName +"] Prosledjeni parametar pod brojem: " + (i + 1) + "(indeksirano od 1) nije kompatibilan sa odgovarajucim formalnim parametrom metode " + methName + " po tipu", node);

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
            report_error("["+ nodeName +"] Neadekvatna vrsta promenljive (" + meth.getName() + " mora biti metoda)", node);
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
            report_error("[ConditionOr] Condition mora biti tipa bool", node);
            node.struct = Tab.noType;
            return;
        }

        node.struct = node.getCondition().struct;
    }

    @Override
    public void visit(ConditionCondTerm node){
        if (!es.equals(node.getCondTerm().struct, boolType)){
            report_error("[ConditionCondTerm] Condition mora biti tipa bool", node);
            node.struct = Tab.noType;
            return;
        }

        node.struct = node.getCondTerm().struct;
    }

    @Override
    public void visit(CondTermAnd node){
        if (!es.equals(node.getCondFact().struct, boolType) || !es.equals(node.getCondTerm().struct, boolType)){
            report_error("[CondTermAnd] Condition mora biti tipa bool", node);
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
            report_error("[CondFactRelop] Logicki operandi nisu kompatibilni", node);
            node.struct = Tab.noType;
            return;
        }
        if (es.isRefType(left) || es.isRefType(right)){
            if (!(node.getRelop() instanceof RelopEqual) && !(node.getRelop() instanceof RelopNotEqual)){
                report_error("[CondFactRelop] Uz promenljive tipa klase ili niza, od relacionih operatora, mogu se koristiti samo != i ==", node);
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
            report_error("[StatementBreak] Break naredba se ne moze pozivati van while petlje", node);
            return;
        }
    }

    @Override
    public void visit(StatementContinue node){
        if(loopCounter == 0){
            report_error("[StatementContinue] Continue naredba se ne moze pozivati van while petlje", node);
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
            report_error("[InterfaceDeclName] Vec je deklarisana promenljiva sa imenom: " + node.getI1(), node);
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
                report_error("[checkIfAllMethodsAreImplemented] Metod " + member.getName() + " interfejsa nije implementiran unutar klase koja ga prosiruje", node);
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
            report_error("[ClassDeclName] Vec je deklarisana promenljiva sa imenom: " + node.getI1(), node);
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
            report_error("[ExtendsClass] Neterminal Type mora da oznacava klasu ili interfejs (korisnicki definisan tip)", node);
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
