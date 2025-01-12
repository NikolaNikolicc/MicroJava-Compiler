package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor{

    public boolean errorDetected = false;
    private int nVars = 0;
    private Obj currType = null;
    private Obj currMeth = null;
    private int constValue;
    private Struct constType = null;
    public static Struct boolType = Tab.find("bool").getType();
    private boolean mainFound = false;

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

        if(!mainFound){
            report_error("Main metoda mora biti definisana", null);
        }
    }

    @Override
    public void visit(TypeIdent type){
        Obj typeNode = Tab.find(type.getI1());
        if(typeNode == Tab.noObj){
            report_error("Greska: Nije pronadjen tip " + type.getI1() + " u tabeli simbola! ", null);
            currType = null;
            return;
        }
        if(Obj.Type != typeNode.getKind()){
            report_error("Greska: Ime " + type.getI1() + " ne predstavlja tip!", type);
            currType = null;
            return;
        }
        currType = typeNode;
        type.struct = typeNode.getType();
    }

    @Override
    public void visit(NumConst numConst){
        constValue = numConst.getN1();
        constType = Tab.intType;
    }

    @Override
    public void visit(CharConst charConst){
        constValue = charConst.getC1();
        constType = Tab.charType;
    }

    @Override
    public void visit(BoolConst boolConst){
        constValue = boolConst.getB1();
        constType = boolType;
    }

    @Override
    public void visit(ConstDeclAssign constDeclAssign){
        Obj node = Tab.find(constDeclAssign.getI1());
        if(node != Tab.noObj){
            report_error("Vec je deklarisana konstanta sa imenom " + constDeclAssign.getI1(), constDeclAssign);
            return;
        }
        // assignableTo mora da stoji, a ne equals jer onda u slucaju lose procitanih tokena (noType) opet ubacujemo vrednost
        // u tabelu simbola i onda mozemo tu informaciju koristiti za dalju sintaksnu analizu
        if(!constType.assignableTo(currType.getType())){
            report_error("Deklariani tip konstante i vrednost koja se dodeljuje nisu kompatibilni", constDeclAssign);
            return;
        }
        Obj constNode = Tab.insert(Obj.Con, constDeclAssign.getI1(), currType.getType());
        constNode.setAdr(constValue);
    }

    private boolean checkIsVarDeclared(String name){
        Obj node;
        if(currMeth != null){
            node = Tab.currentScope().findSymbol(name);
        }else{
            node = Tab.find(name);
        }

        if(node == null){
            return false;
        }
        return node != Tab.noObj;
    }

    @Override
    public void visit(VarDeclFinalVar varDeclFinalVar){
        if(checkIsVarDeclared(varDeclFinalVar.getI1())){
            report_error("Vec je deklarisana promenljiva sa imenom: " + varDeclFinalVar.getI1(), varDeclFinalVar);
            return;
        }

        Obj varNode = Tab.insert(Obj.Var, varDeclFinalVar.getI1(), currType.getType());
    }

    @Override
    public void visit(VarDeclFinalArray varDeclFinalArray){
        if(checkIsVarDeclared(varDeclFinalArray.getI1())){
            report_error("Vec je deklarisana promenljiva sa imenom " + varDeclFinalArray.getI1(), varDeclFinalArray);
            return;
        }

        Struct array = new Struct(Struct.Array, currType.getType());
        Obj varNode = Tab.insert(Obj.Var, varDeclFinalArray.getI1(), array);
    }

    private void createMethodObjNode(String name){
        currMeth = Tab.insert(Obj.Meth, name, Tab.noType);
        Tab.openScope();
    }

    @Override
    public void visit(RegularMethod regularMethod){
        createMethodObjNode(regularMethod.getI1());
    }

    @Override
    public void visit(MainMethod mainMethod){
        if(mainFound){
            report_error("Vec je definisan main metod", null);
            return;
        }
        mainFound = true;
        createMethodObjNode("main");
    }

    @Override
    public void visit(MethodDecl methodDecl){
        Tab.chainLocalSymbols(currMeth);
        Tab.closeScope();
        currMeth = null;
    }

}
