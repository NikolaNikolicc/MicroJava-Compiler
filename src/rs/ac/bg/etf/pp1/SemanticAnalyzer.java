package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SemanticAnalyzer extends VisitorAdaptor{

    public boolean errorDetected = false;
    private Obj currTypeVar = null;
    private Obj currTypeMeth = null;
    private Obj currMeth = null;

    private boolean returnNode = false;

    // because we want to allow initialization of variables that are named int char and bool we are saving pointers to this object nodes
    // this is used in TypeIdent visitor and in that case we are sure we are getting right object node, in other case Tab.find(name) function can return Object node which overrides those names
    private static Obj intObj = Tab.find("int");
    private static Obj charObj = Tab.find("char");
    private static Obj boolObj = Tab.find("bool");
    public static Struct boolType = boolObj.getType();

    private Obj mainMeth = null;
    private boolean parsingFormPars = false;

    private int loopCounter = 0;

    private static final String[] objKindNames = { "Con", "Var", "Type", "Meth", "Fld", "Elem", "Prog" };
    private static final String[] structKindNames = { "None", "Int", "Char", "Array", "Class", "Bool" };

    private Stack<Struct> fpStack = new Stack<>();

    Logger log = Logger.getLogger(getClass());

    public void report_error(String message, SyntaxNode info) {
        errorDetected = true;
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0: info.getLine();
        if (line != 0)
            msg.append (" na liniji ").append(line);
        log.error(msg.toString());
    }

    private void logSymbol(String message, Obj sym, SyntaxNode node) {
        StringBuilder builder = new StringBuilder(message);
        if (node != null) {
            builder.append(" (line " + node.getLine() + ")");
        }
        builder.append(": [");
        builder.append(sym.getName()); // name
        builder.append(", ");
        builder.append(objKindNames[sym.getKind()]); // kind
        builder.append(", ");
        builder.append(structKindNames[sym.getType().getKind()]); // type
        builder.append(", ");
        builder.append(sym.getAdr()); // adr
        builder.append(", ");
        builder.append(sym.getLevel()); // level
        builder.append("]");
        log.info(builder.toString());
    }

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0: info.getLine();
        if (line != 0)
            msg.append (" na liniji ").append(line);
        log.info(msg.toString());
    }

    public boolean passed(){
        return !errorDetected;
    }

    @Override
    public void visit(ProgName node){
        node.obj = Tab.insert(Obj.Prog, node.getProgName(), Tab.noType);
        Tab.openScope();
    }

    // helpers
    private boolean checkIsObjNodeDeclared(String name){
        Obj node = Tab.currentScope().findSymbol(name);
        return node != null;
//        Obj node;
//        if(currMeth != null){
//            node = Tab.currentScope().findSymbol(name);
//        }else{
//            node = Tab.find(name);
//        }
//
//        // we can't write this as one condition with OR because everything that is not null will pass and we don't want behaviour like that
//        if(node == null){
//            return false;
//        }
//        return node != Tab.noObj;
    }

    private void formParsSetLevelAndFpPos(Obj node){
        if(parsingFormPars){
            node.setFpPos(1);
            currMeth.setLevel(currMeth.getLevel() + 1);
        }
    }

    private List<Struct> getFormalParameters(Obj funcNode, SyntaxNode node){
        List<Struct> fpList = new ArrayList<>();
        for (Obj localSym: funcNode.getLocalSymbols()){
            if (localSym.getKind() == Obj.Var && localSym.getFpPos() == 1 && localSym.getLevel() == 1){
                fpList.add(localSym.getType());
            }
        }
        // report_info("Metoda koja se poziva(" + funcNode.getName() + ") ima sledeci broj formalnih parametara: " + fpList.size(), node);
        return fpList;
    }

    private boolean checkArePassedParametersAndFormalParameterListCompatible(List<Struct> fpList, String methName,  SyntaxNode node){

        // report_info("Metoda koja se poziva(" + methName + ") ima sledeci broj prosledjenih parametara: " + fpStack.size(), node);
        if (fpList.size() != fpStack.size()){
            report_error("[FactorFuncCall][DesignatorStatementFuncCall] Lista prosledjenih parametara se ne poklapa se parametrima koji su prosledjeni prilikom poziva metode " + methName + " po broju prosledjenih parametara, ova metoda prima: " + fpList.size() + " parametara", node);
            fpStack = new Stack<>();
            return false;
        }

        boolean errorHappened = false;
        for (int i = 0; i < fpList.size(); i++){
            Struct fpListElem = fpList.get(i);
            Struct fpStackElem = fpStack.pop();
            if(!fpStackElem.assignableTo(fpListElem)){
                report_error("[FactorFuncCall][DesignatorStatementFuncCall] Prosledjeni parametar pod brojem: " + (i + 1) + "(indeksirano od 1) nije kompatibilan sa odgovarajucim formalnim parametrom metode " + methName + " po tipu", node);

                errorHappened = true;
            }
        }
        if (errorHappened){
            fpStack = new Stack<>();
            return false;
        }
        return true;
    }

    @Override
    public void visit(Program node){
        Tab.chainLocalSymbols(node.getProgName().obj);
        Tab.closeScope();

        if(mainMeth == null){
            report_error("[Program] Main metoda mora biti definisana", null);
        }
        if(mainMeth != null && mainMeth.getLevel() > 0){
            report_error("[Program] Main metoda mora biti deklarisana bez argumenata!", null);
        }
    }

    @Override
    public void visit(TypeIdent node){
        String name = node.getI1();
        Obj typeNode;

        switch(name){
            case "int":
                typeNode = intObj;
                break;
            case "char":
                typeNode = charObj;
                break;
            case "bool":
                typeNode = boolObj;
                break;
            default:
                typeNode = Tab.find(name);
                break;
        }
        if(typeNode == Tab.noObj){
            report_error("[TypeIdent] Nije pronadjen tip " + node.getI1() + " u tabeli simbola! ", null);
            currTypeVar = null;
            return;
        }
        if(Obj.Type != typeNode.getKind()){
            report_error("[TypeIdent] Ime " + node.getI1() + " ne predstavlja tip!", node);
            currTypeVar = null;
            return;
        }
        currTypeVar = typeNode;
    }

    @Override
    public void visit(NoVoidMethod node){
        Obj typeNode = Tab.find(node.getI1());
        if(typeNode == Tab.noObj){
            report_error("[NoVoidMethod] Nije pronadjen tip " + node.getI1() + " u tabeli simbola! ", null);
            currTypeVar = null;
            return;
        }
        if(Obj.Type != typeNode.getKind()){
            report_error("[NoVoidMethod] Ime " + node.getI1() + " ne predstavlja tip!", node);
            currTypeVar = null;
            return;
        }
        currTypeMeth = typeNode;
    }

    @Override
    public void visit(VoidMethod node){
        currTypeMeth = null;
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

    @Override
    public void visit(ConstDeclAssign node){
        if(checkIsObjNodeDeclared(node.getI1())){
            report_error("[ConstDeclAssign] Vec je deklarisana konstanta sa imenom " + node.getI1(), node);
            return;
        }
        // assignableTo mora da stoji, a ne equals jer onda u slucaju lose procitanih tokena (noType) opet ubacujemo vrednost
        // u tabelu simbola i onda mozemo tu informaciju koristiti za dalju sintaksnu analizu
        Obj constObj = node.getConstDeclListValue().obj;
        if(!constObj.getType().assignableTo(currTypeVar.getType())){
            report_error("[ConstDeclAssign] Deklariani tip konstante i vrednost koja se dodeljuje nisu kompatibilni!", node);
            return;
        }
        Obj constNode = Tab.insert(Obj.Con, node.getI1(), currTypeVar.getType());
        constNode.setAdr(constObj.getAdr());
    }

    @Override
    public void visit(VarDeclFinalVar node){
        if(checkIsObjNodeDeclared(node.getI1())){
            report_error("[VarDeclFinalVar] Vec je deklarisana promenljiva sa imenom: " + node.getI1(), node);
            return;
        }

        Obj varNode = Tab.insert(Obj.Var, node.getI1(), currTypeVar.getType());
        formParsSetLevelAndFpPos(varNode);
    }

    @Override
    public void visit(VarDeclFinalArray node){
        if(checkIsObjNodeDeclared(node.getI1())){
            report_error("[VarDeclFinalArray] Vec je deklarisana promenljiva sa imenom " + node.getI1(), node);
            return;
        }

        Struct array = new Struct(Struct.Array, currTypeVar.getType());
        Obj varNode = Tab.insert(Obj.Var, node.getI1(), array);
        formParsSetLevelAndFpPos(varNode);
    }

    private void createMethodObjNode(String name){
        currMeth = Tab.insert(Obj.Meth, name, (currTypeMeth == null)? Tab.noType : currTypeMeth.getType());
        currMeth.setLevel(0);
        Tab.openScope();
    }

    @Override
    public void visit(RegularMethod node){
        createMethodObjNode(node.getI1());
    }

    @Override
    public void visit(MainMethod node){
        if(mainMeth != null){
            report_error("[RegularMethod] Vec je definisan main metod!", node);
            return;
        }
        if(currTypeMeth != null){
            report_error("[RegularMethod] Main metoda mora biti povratnog tipa void!", node);
        }
        createMethodObjNode("main");
        mainMeth = currMeth;
    }

    @Override
    public void visit(MethodDecl node){
        Tab.chainLocalSymbols(currMeth);
        Tab.closeScope();

        if (!currMeth.getType().equals(Tab.noType) && !returnNode){
            report_error("[MethodDecl] U metodu povratnog tipa koji nije void se mora pojaviti barem jedna return naredba", node);
        }

        currMeth = null;
        returnNode = false;
    }

    @Override
    public void visit(FactorConstDeclListValue node){
        node.struct = node.getConstDeclListValue().obj.getType();
    }

    @Override
    public void visit(TermFactor node){
        node.struct = node.getFactor().struct;
    }

    @Override
    public void visit(TermTermMulopFactor node){
        if(!node.getFactor().struct.equals(Tab.intType) || !node.getTerm().struct.equals(Tab.intType)){
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
        if(!node.getTerm().struct.equals(Tab.intType)){
            report_error("[ExprMinusTerm] Minus mozemo staviti samo ispred operanda koji je tipa int.", node);
            node.struct = Tab.noType;
            return;
        }
        node.struct = node.getTerm().struct;
    }

    @Override
    public void visit(ExprDesignatorMap node){
        if (node.getMapDesignator().getDesignator().obj == Tab.noObj || node.getDesignator().obj == Tab.noObj){
            return;
        }
        String name = node.getDesignator().obj.getName();
        Obj meth = Tab.find(name);

        if(meth.getKind() != Obj.Meth){
            report_error("[ExprDesignatorMap] Designator(" + name + ") sa leve strane MAP operanda mora biti metoda", node);
            node.struct = Tab.noType;
            return;
        }
        List<Struct> fpList = getFormalParameters(meth, node);
        if (fpList.size() != 1){
            report_error("[ExprDesignatorMap] Metoda sa leve strane MAP operanda mora imati tacno jedan parametar", node);
            node.struct = Tab.noType;
            return;
        }
        if(!fpList.get(0).equals(Tab.intType)){
            report_error("[ExprDesignatorMap] Parametar metoda sa leve strane MAP operanda mora biti tipa int", node);
            node.struct = Tab.noType;
            return;
        }
        if(!meth.getType().equals(Tab.intType)){
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

        String name = node.getDesignator().obj.getName();
        Obj arr = Tab.find(name);
        // logSymbol("Pronadjen simbol arr: ", arr, node);
        if (arr.getType().getKind() != Struct.Array || !arr.getType().getElemType().equals(Tab.intType)){
            report_error("[MapDesignator] Designator(" + name + ") sa desne strane operanda MAP mora predstavljati niz celobrojnih vrednosti", node);
        }
    }

    @Override
    public void visit(ExprAddopTerm node){
        if(!node.getTerm().struct.equals(Tab.intType) || !node.getExpr().struct.equals(Tab.intType)){
            report_error("[ExprAddopTerm] Addop operator zahteva da oba operanda budu int vrednosti.", node);
            node.struct = Tab.noType;
            return;
        }
        node.struct = node.getTerm().struct;
    }

    // Condition

    @Override
    public void visit(StatementConditionCondition node){
        node.struct = node.getCondition().struct;
    }

    @Override
    public void visit(ConditionOr node){
        if (!node.getCondition().struct.equals(boolType) || !node.getCondTerm().struct.equals(boolType)){
            report_error("[ConditionOr] Condition mora biti tipa bool [ConditionOr]", node);
            node.struct = Tab.noType;
            return;
        }

        node.struct = node.getCondition().struct;
    }

    @Override
    public void visit(ConditionCondTerm node){
        if (!node.getCondTerm().struct.equals(boolType)){
            report_error("[ConditionCondTerm] Condition mora biti tipa bool [ConditionCondTerm]", node);
            node.struct = Tab.noType;
            return;
        }

        node.struct = node.getCondTerm().struct;
    }

    @Override
    public void visit(CondTermAnd node){
        if (!node.getCondFact().struct.equals(boolType) || !node.getCondTerm().struct.equals(boolType)){
            report_error("[CondTermAnd] Condition mora biti tipa bool [CondTermAnd]", node);
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
        Struct left = node.getCondFact().struct;
        Struct right = node.getExpr().struct;
        // for case if (true && a < 2){...} - a and 2 only needs to be comparable not necessarily boolType
        if (!left.compatibleWith(right)){
            report_error("[CondFactRelop] Logicki operandi nisu kompatibilni", node);
            node.struct = Tab.noType;
            return;
        }
        if (left.isRefType() || right.isRefType()){
            if (!(node.getRelop() instanceof EqEq) && !(node.getRelop() instanceof NotEq)){
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

    // Factor
    @Override
    public void visit(FactorDesignator node){
        node.struct = node.getDesignator().obj.getType();
    }

    @Override
    public void visit(FactorFuncCall node){
        if(node.getDesignator().obj.getKind() != Obj.Meth){
            report_error("[FactorFuncCall] Poziv neadekvatne metode(" + node.getDesignator().obj.getName() + ")", node);
            node.struct = Tab.noType;
            return;
        }
        node.struct = node.getDesignator().obj.getType();
        List<Struct> fpList = getFormalParameters(node.getDesignator().obj, node);

        if (!checkArePassedParametersAndFormalParameterListCompatible(fpList, node.getDesignator().obj.getName(), node)){

        }
    }

    // function call parameters
    @Override
    public void visit(StackInitialize node){
        fpStack = new Stack<>();
    }

    @Override
    public void visit(ActParsMultipleItems node){
        fpStack.push(node.getExpr().struct);
    }

    @Override
    public void visit(ActParsSigleItem node){
        fpStack.push(node.getExpr().struct);
    }

    @Override
    public void visit(DesignatorVar node){
        Obj var = Tab.find(node.getI1());
        if (var == Tab.noObj){
            report_error("[DesignatorVar] Nije deklarisana promenljiva sa zadatim imenom(" + node.getI1() + ")", node);
            node.obj = Tab.noObj;
            return;
        }
        else if (var.getKind() != Obj.Var && var.getKind() != Obj.Con && var.getKind() != Obj.Meth){
            report_error("[DesignatorVar] Neadekvatna vrsta promenljive " + node.getI1(), node);
            node.obj = Tab.noObj;
            return;
        }
        node.obj = var;
    }

    @Override
    public void visit(DesignatorElem node){
        Obj arr = node.getDesignatorArrayName().obj;
        if (arr == Tab.noObj){
            node.obj = Tab.noObj;
            return;
        }
        else if (!node.getExpr().struct.equals(Tab.intType)){
            report_error("[DesignatorElem] Indeks niza mora biti int vrednost", node);
            node.obj = Tab.noObj;
            return;
        }
        node.obj = new Obj(Obj.Elem, arr.getName() + "[$]", arr.getType().getElemType());
    }

    @Override
    public void visit(FactorCreateArray node){
        if (!node.getExpr().struct.equals(Tab.intType)){
            report_error("[FactorCreateArray] Izraz prosledjen kao velicina niza mora biti int tipa.", node);
            node.struct = Tab.noType;
            return;
        }
        node.struct = new Struct(Struct.Array, currTypeVar.getType());
    }

    @Override
    public void visit(FactorExpr node){
        node.struct = node.getExpr().struct;
    }

    @Override
    public void visit(DesignatorArrayName node){
        Obj arr = Tab.find(node.getI1());
        if (arr == Tab.noObj){
            report_error("[DesignatorArrayName] Nije deklarisana promenljiva niza sa imenom" + node.getI1(), node);
            node.obj = Tab.noObj;
            return;
        }
        else if (arr.getKind() != Obj.Var || arr.getType().getKind() != Struct.Array){
            report_error("[DesignatorArrayName] Neadekvatna vrsta promenljive niza : " + node.getI1(), node);
            node.obj = Tab.noObj;
            return;
        }
        node.obj = arr;
    }

    // Statement

    @Override
    public void visit(StatementBreak node){
        if(loopCounter == 0){
            report_error("[StatementBreak] Break naredba se ne moze pozivati van while petlje", node);
            return;
        }
        loopCounter--;
    }

    @Override
    public void visit(StatementContinue node){
        if(loopCounter == 0){
            report_error("[StatementContinue] Continue naredba se ne moze pozivati van while petlje", node);
            return;
        }
    }

    @Override
    public void visit(StatementReturn node){
        if (currMeth == null){
            report_error("[StatementReturn] Return naredba se moze pozivati samo unutar tela metode", node);
            return;
        }
        if (!currMeth.getType().equals(Tab.noType)){
            report_error("[StatementReturn] U metodu povratnog tipa koji nije void svaka return naredba mora da vraca taj tip", node);
        }
        returnNode = true;
    }

    @Override
    public void visit(StatementReturnExpr node){
        if (currMeth == null){
            report_error("[StatementReturnExpr] Return naredba se moze pozivati samo unutar tela metode", node);
            return;
        }
        if (!currMeth.getType().equals(node.getExpr().struct)){
            report_error("[StatementReturnExpr] Povratni tip metoda i tip koji vraca return naredba nisu kompatibilni", node);
        }
        returnNode = true;
    }

    @Override
    public void visit(StatementRead node){
        int kind = node.getDesignator().obj.getKind();
        String name = node.getDesignator().obj.getName();
        Struct type = node.getDesignator().obj.getType();
        if (kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld){
            report_error("[StatementRead] Read operacija nad neadekvatnom promenljivom(" + name + ")", node);
            return;
        }
        else if(!type.equals(Tab.intType) && !type.equals(boolType) && !type.equals(Tab.charType)){
            report_error("[StatementRead] Read operacija nad promenljivom("+ name + ") koja nije tipa int, char ili bool", node);
            return;
        }
    }

    @Override
    public void visit(StatementPrint node){
        Struct type = node.getExpr().struct;
        if(!type.equals(Tab.intType) && !type.equals(boolType) && !type.equals(Tab.charType)){
            report_error("[StatementPrint] Print operacija nad izrazom koji nije tipa int, char ili bool", node);
            return;
        }
    }

    @Override
    public void visit(StatementPrintNumber node){
        Struct type = node.getExpr().struct;
        if(!type.equals(Tab.intType) && !type.equals(boolType) && !type.equals(Tab.charType)){
            report_error("[StatementPrintNumber] Print operacija nad izrazom koji nije tipa int, char ili bool", node);
            return;
        }
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
    public void visit(DoStatement node){
       loopCounter++;
    }

    // Designator
    @Override
    public void visit(DesignatorAssignExpr node){
        int kind = node.getDesignator().obj.getKind();
        if(kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld){
            report_error("[DesignatorAssignExpr] Dodela u neadekvatnu promenljivu : " + node.getDesignator().obj.getName(), node);
            return;
        }
        // it's important to use assignableTo because of assigning null propery
        else if(!node.getExpr().struct.assignableTo(node.getDesignator().obj.getType())){
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
        else if(!node.getExpr().struct.assignableTo(node.getDesignator().obj.getType())){
            report_error("[DesignatorAssignExprWhile] Tip Expr nije kompatibilan sa tipom neterminala Dedsignator : " + node.getDesignator().obj.getName(), node );
            return;
        }

    }

    @Override
    public void visit(DesignatorStatementUnarySemi node){
        int kind = node.getDesignator().obj.getKind();
        String name = node.getDesignator().obj.getName();
        if (kind != Obj.Var && kind != Obj.Elem && kind != Obj.Fld){

            report_error("[DesignatorStatementUnarySemi] Unarna operacija (-- ili ++) nad neadekvatnom promenljivom(" + name + ")", node);
            return;
        }
        else if(!node.getDesignator().obj.getType().equals(Tab.intType)){
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
        else if(!node.getDesignator().obj.getType().equals(Tab.intType)){
            report_error("[DesignatorStatementUnarySemiWhile] Unarna operacija (-- ili ++) nad promenljivom("+ name + ") koja nije tipa int", node);
            return;
        }
    }

    @Override
    public void visit(DesignatorStatementFuncCall node){
        if(node.getDesignator().obj.getKind() != Obj.Meth){
            report_error("[DesignatorStatementFuncCall] Poziv neadekvatne metode(" + node.getDesignator().obj.getName() + ")", node);
            return;
        }

        List<Struct> fpList = getFormalParameters(node.getDesignator().obj, node);
        if (!checkArePassedParametersAndFormalParameterListCompatible(fpList, node.getDesignator().obj.getName(), node)){

        }


    }

    @Override
    public void visit(DesignatorStatementFuncCallWhile node){
        if(node.getDesignator().obj.getKind() != Obj.Meth){
            report_error("[DesignatorStatementFuncCallWhile] Poziv neadekvatne metode(" + node.getDesignator().obj.getName() + ")", node);
            return;
        }
    }

    @Override
    public void visit(MethodSignatureStartFormPars node){
        parsingFormPars = true;
    }

    @Override
    public void visit(MethodSignatureEndFormPars node){
        parsingFormPars = false;
    }

}
