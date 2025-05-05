package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor{

    public boolean errorDetected = false;
    private int nVars = 0;
    private Obj currTypeVar = null;
    private Obj currTypeMeth = null;
    private Obj currMeth = null;
    // because we want to allow initialization of variables that are named int char and bool we are saving pointers to this object nodes
    // this is used in TypeIdent visitor and in that case we are sure we are getting right object node, in other case Tab.find(name) function can return Object node which overrides those names
    private static Obj intObj = Tab.find("int");
    private static Obj charObj = Tab.find("char");
    private static Obj boolObj = Tab.find("bool");
    public static Struct boolType = boolObj.getType();
    private Obj mainMeth = null;
    private boolean parsingFormPars = false;

    private static final String[] objKindNames = { "Con", "Var", "Type", "Meth", "Fld", "Elem", "Prog" };
    private static final String[] structKindNames = { "None", "Int", "Char", "Array", "Class", "Bool" };

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
        builder.append(sym.getName());
        builder.append(", ");
        builder.append(objKindNames[sym.getKind()]);
        builder.append(", ");
        builder.append(structKindNames[sym.getType().getKind()]);
        builder.append(", ");
        builder.append(sym.getAdr());
        builder.append(", ");
        builder.append(sym.getLevel());
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
    public void visit(ProgName progName){
        progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
        Tab.openScope();
    }

    @Override
    public void visit(Program program){
        nVars = Tab.currentScope.getnVars();
        Tab.chainLocalSymbols(program.getProgName().obj);
        Tab.closeScope();

        if(mainMeth == null){
            report_error("Main metoda mora biti definisana", null);
        }
        if(mainMeth != null && mainMeth.getLevel() > 0){
            report_error("Main metoda mora biti deklarisana bez argumenata!", null);
        }
    }

    @Override
    public void visit(TypeIdent type){
        String name = type.getI1();
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
            report_error("Greska: Nije pronadjen tip " + type.getI1() + " u tabeli simbola! ", null);
            currTypeVar = null;
            return;
        }
        if(Obj.Type != typeNode.getKind()){
            report_error("Greska: Ime " + type.getI1() + " ne predstavlja tip!", type);
            currTypeVar = null;
            return;
        }
        currTypeVar = typeNode;
    }

    @Override
    public void visit(NoVoidMethod type){
        Obj typeNode = Tab.find(type.getI1());
        if(typeNode == Tab.noObj){
            report_error("Greska: Nije pronadjen tip " + type.getI1() + " u tabeli simbola! ", null);
            currTypeVar = null;
            return;
        }
        if(Obj.Type != typeNode.getKind()){
            report_error("Greska: Ime " + type.getI1() + " ne predstavlja tip!", type);
            currTypeVar = null;
            return;
        }
        currTypeMeth = typeNode;
    }

    @Override
    public void visit(VoidMethod type){
        currTypeMeth = null;
    }

    @Override
    public void visit(NumConst numConst){
        numConst.obj = new Obj(Obj.Con, "numConst", Tab.intType, numConst.getN1(), 0);
    }

    @Override
    public void visit(CharConst charConst){
        charConst.obj = new Obj(Obj.Con, "charConst", Tab.charType, charConst.getC1(), 0);
    }

    @Override
    public void visit(BoolConst boolConst){
        boolConst.obj = new Obj(Obj.Con, "boolConst", boolType, boolConst.getB1(), 0);
    }

    @Override
    public void visit(ConstDeclAssign constDeclAssign){
        if(checkIsObjNodeDeclared(constDeclAssign.getI1())){
            report_error("Vec je deklarisana konstanta sa imenom " + constDeclAssign.getI1(), constDeclAssign);
            return;
        }
        // assignableTo mora da stoji, a ne equals jer onda u slucaju lose procitanih tokena (noType) opet ubacujemo vrednost
        // u tabelu simbola i onda mozemo tu informaciju koristiti za dalju sintaksnu analizu
        Obj node = constDeclAssign.getConstDeclListValue().obj;
        if(!node.getType().assignableTo(currTypeVar.getType())){
            report_error("Deklariani tip konstante i vrednost koja se dodeljuje nisu kompatibilni!", constDeclAssign);
            return;
        }
        Obj constNode = Tab.insert(Obj.Con, constDeclAssign.getI1(), currTypeVar.getType());
        constNode.setAdr(node.getAdr());
    }

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

    private void formParsSetLevelAndFpPos(Obj varNode){
        if(parsingFormPars){
            varNode.setFpPos(1);
            currMeth.setLevel(currMeth.getLevel() + 1);
        }
    }

    @Override
    public void visit(VarDeclFinalVar varDeclFinalVar){
        if(checkIsObjNodeDeclared(varDeclFinalVar.getI1())){
            report_error("Vec je deklarisana promenljiva sa imenom: " + varDeclFinalVar.getI1(), varDeclFinalVar);
            return;
        }

        Obj varNode = Tab.insert(Obj.Var, varDeclFinalVar.getI1(), currTypeVar.getType());
        formParsSetLevelAndFpPos(varNode);
    }

    @Override
    public void visit(VarDeclFinalArray varDeclFinalArray){
        if(checkIsObjNodeDeclared(varDeclFinalArray.getI1())){
            report_error("Vec je deklarisana promenljiva sa imenom " + varDeclFinalArray.getI1(), varDeclFinalArray);
            return;
        }

        Struct array = new Struct(Struct.Array, currTypeVar.getType());
        Obj varNode = Tab.insert(Obj.Var, varDeclFinalArray.getI1(), array);
        formParsSetLevelAndFpPos(varNode);
    }

    private void createMethodObjNode(String name){
        currMeth = Tab.insert(Obj.Meth, name, (currTypeMeth == null)? Tab.noType : currTypeMeth.getType());
        currMeth.setLevel(0);
        Tab.openScope();
    }

    @Override
    public void visit(RegularMethod regularMethod){
        createMethodObjNode(regularMethod.getI1());
    }

    @Override
    public void visit(MainMethod mainMethod){
        if(mainMeth != null){
            report_error("Vec je definisan main metod!", null);
            return;
        }
        if(currTypeMeth != null){
            report_error("Main metoda mora biti povratnog tipa void!", null);
        }
        createMethodObjNode("main");
        mainMeth = currMeth;
    }

    @Override
    public void visit(MethodDecl methodDecl){
        Tab.chainLocalSymbols(currMeth);
        Tab.closeScope();
        currMeth = null;
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
            report_error("Mulop operator zahteva da oba operanda budu int vrednosti.", node);
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
            report_error("Minus mozemo staviti samo ispred operanda koji je tipa int.", node);
            node.struct = Tab.noType;
            return;
        }
        node.struct = node.getTerm().struct;
    }

    @Override
    public void visit(ExprAddopTerm node){
        if(!node.getTerm().struct.equals(Tab.intType) || !node.getExpr().struct.equals(Tab.intType)){
            report_error("Addop operator zahteva da oba operanda budu int vrednosti.", node);
            node.struct = Tab.noType;
            return;
        }
        node.struct = node.getTerm().struct;
    }

    @Override
    public void visit(FactorDesignator node){
        node.struct = node.getDesignator().obj.getType();
    }

    @Override
    public void visit(DesignatorVar node){
        Obj var = Tab.find(node.getI1());
        if (var == Tab.noObj){
            report_error("Nije deklarisana promenljiva sa imenom" + node.getI1(), node);
            node.obj = Tab.noObj;
            return;
        }
        else if (var.getKind() != Obj.Var && var.getKind() != Obj.Con){
            report_error("Neadekvatna vrsta promenljive " + node.getI1() + ":", node);
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
            report_error("Indeks niza mora biti int vrednost", node);
            node.obj = Tab.noObj;
            return;
        }
        node.obj = new Obj(Obj.Elem, arr.getName() + "[$]", arr.getType().getElemType());
    }

    @Override
    public void visit(FactorCreateArray node){
        if (!node.getExpr().struct.equals(Tab.intType)){
            report_error("Izraz prosledjen kao velicina niza mora biti int tipa.", node);
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
            report_error("Nije deklarisana promenljiva niza sa imenom" + node.getI1(), node);
            node.obj = Tab.noObj;
            return;
        }
        else if (arr.getKind() != Obj.Var || arr.getType().getKind() != Struct.Array){
            report_error("Neadekvatna vrsta promenljive niza : " + node.getI1(), node);
            node.obj = Tab.noObj;
            return;
        }
        node.obj = arr;
    }

    // Designator
    @Override
    public void visit(DesignatorAssignExpr node){
        int kind = node.getDesignator().obj.getKind();
        if(kind != Obj.Var && kind != Obj.Elem){
            report_error("Dodela u neadekvatnu promenljivu : " + node.getDesignator().obj.getName(), node);
            return;
        }
        // it's important to use assignableTo because of assigning null propery
        else if(!node.getExpr().struct.assignableTo(node.getDesignator().obj.getType())){
            report_error("Tip Expr nije kompatibilan sa tipom neterminala Dedsignator : " + node.getDesignator().obj.getName(), node );
            return;
        }

    }

    // we need to repeat this function because of error handling implementation
    @Override
    public void visit(DesignatorAssignExprWhile node){
        int kind = node.getDesignator().obj.getKind();
        if(kind != Obj.Var && kind != Obj.Elem){
            report_error("Dodela u neadekvatnu promenljivu : " + node.getDesignator().obj.getName(), node);
            return;
        }
        // it's important to use assignableTo because of assigning null propery
        else if(!node.getExpr().struct.assignableTo(node.getDesignator().obj.getType())){
            report_error("Tip Expr nije kompatibilan sa tipom neterminala Dedsignator : " + node.getDesignator().obj.getName(), node );
            return;
        }

    }

    @Override
    public void visit(MethodSignatureStartFormPars methodSignatureStartFormPars){
        parsingFormPars = true;
    }

    @Override
    public void visit(MethodSignatureEndFormPars methodSignatureEndFormPars){
        parsingFormPars = false;
    }

}
