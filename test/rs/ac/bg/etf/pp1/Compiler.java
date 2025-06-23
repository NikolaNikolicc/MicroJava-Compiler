package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;
import rs.ac.bg.etf.pp1.util.MySymbolTableVisitor;

public class Compiler {

    static {
        DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
        Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
    }

    public static void dump(MySymbolTableVisitor stv) {
        System.out.println("=====================SYMBOL TABLE DUMP=========================");
        if (stv == null) {
            stv = new MySymbolTableVisitor();
        }

        for(Scope s = Tab.currentScope; s != null; s = s.getOuter()) {
            s.accept(stv);
        }

        System.out.println(stv.getOutput());
    }

    public static void tsdump(){
        dump((MySymbolTableVisitor) null);
    }

    public static void main(String[] args) throws Exception {

        Logger log = Logger.getLogger(MJParserTest.class);

        Reader br = null;
        try {
//             File sourceCode = new File("test/GenerisanjeKoda-TestPrimeri_24_25_jan_feb/test301.mj");
//            File sourceCode = new File("test/GenerisanjeKoda-TestPrimeri_24_25_jan_feb/test302.mj");
//            File sourceCode = new File("test/GenerisanjeKoda-TestPrimeri_24_25_jan_feb/test303.mj");
            File sourceCode = new File("test/test.mj");
            log.info("Compiling source file: " + sourceCode.getAbsolutePath());

            br = new BufferedReader(new FileReader(sourceCode));
            Yylex lexer = new Yylex(br);

            // formiranje AST
            MJParser p = new MJParser(lexer);
            Symbol s = p.parse();  //pocetak parsiranja

            Program prog = (Program)(s.value);

            // inicijalizacija tabele simbola
            Tab.init();

            Struct boolStruct = new Struct(Struct.Bool);
            Obj boolObj = Tab.insert(Obj.Type, "bool", boolStruct);
            boolObj.setAdr(-1);
            boolObj.setLevel(-1);

            Struct setStruct = new Struct(Struct.Enum);
            Obj setObj = Tab.insert(Obj.Type, "set", setStruct);
            setObj.setAdr(-1);
            setObj.setLevel(-1);

            // podesiti fp pos na 1 za sve parametre metoda
            for (Obj node: Tab.currentScope().getLocals().symbols()){
                if (node.getKind() == Obj.Meth){
                    for (Obj local: node.getLocalSymbols()){
                        local.setFpPos(1);
                    }
                }
            }

            Obj addObj;
            Tab.currentScope().addToLocals(addObj = new Obj(Obj.Meth, "add", Tab.noType, 0, 1));
            {
                Tab.openScope();
                Obj a = new Obj(Obj.Var, "a", setObj.getType(), 0, 2);
                Obj b = new Obj(Obj.Var, "b", Tab.intType, 0, 1);

                a.setFpPos(1);
                b.setFpPos(1);

                Tab.currentScope().addToLocals(a);
                Tab.currentScope().addToLocals(b);

                addObj.setLocals(Tab.currentScope.getLocals());
                Tab.closeScope();
            }

            Obj addAllObj;
            Tab.currentScope().addToLocals(addAllObj = new Obj(Obj.Meth, "addAll", Tab.noType, 0, 2));
            {
                Tab.openScope();

                Struct intArr = new Struct(Struct.Array, Tab.intType);

                Obj a = new Obj(Obj.Var, "a", setObj.getType(), 0, 1);
                Obj b = new Obj(Obj.Var, "b", intArr, 0, 1);

                a.setFpPos(1);
                b.setFpPos(1);

                Tab.currentScope().addToLocals(a);
                Tab.currentScope().addToLocals(b);

                addAllObj.setLocals(Tab.currentScope.getLocals());
                Tab.closeScope();
            }

            log.info(prog.toString(""));
            log.info("===================================");

            // semanticka analiza
            SemanticAnalyzer v = new SemanticAnalyzer();
            prog.traverseBottomUp(v);

            // ispis sintaksnog stabla
            log.info("===================================");
            tsdump();

			if(!p.errorDetected && v.passed()){
				log.info("Parsiranje uspesno zavrseno!");
			}else{
				log.error("Parsiranje NIJE uspesno zavrseno!");
			}
        }
        finally {
            if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
        }

    }

}
