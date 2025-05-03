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

    Logger log = Logger.getLogger(getClass());

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
            return;
        }
        node.struct = node.getTerm().struct;
    }

    @Override
    public void visit(ExprAddopTerm node){
        if(!node.getTerm().struct.equals(Tab.intType) || !node.getExpr().struct.equals(Tab.intType)){
            report_error("Addop operator zahteva da oba operanda budu int vrednosti.", node);
            return;
        }
        node.struct = node.getTerm().struct;
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
