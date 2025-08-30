package rs.ac.bg.etf.pp1;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.syntax_analysis.output.*;
import rs.ac.bg.etf.pp1.lexical_analysis.output.Yylex;
import rs.ac.bg.etf.pp1.code_generation.CodeGenerator;
import rs.ac.bg.etf.pp1.semantic_analysis.SemanticAnalyzer;
import rs.ac.bg.etf.pp1.syntax_analysis.output.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.ac.bg.etf.pp1.util.TabExtended;
import rs.ac.bg.etf.pp1.util.MySymbolTableVisitor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

public class Compiler {

    static {
        Path configPath = Paths.get("config/log4j.xml");
        DOMConfigurator.configure(configPath.toString());
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

        Logger log = Logger.getLogger(MJParser.class);

        Reader br = null;
        try {
//            File sourceCode = new File("test/rs/ac/bg/etf/pp1/official_tests/test301.mj");
//            File sourceCode = new File("test/rs/ac/bg/etf/pp1/official_tests/test302.mj");
            File sourceCode = new File("test/rs/ac/bg/etf/pp1/official_tests/test303.mj");
//            File sourceCode = new File("test/rs/ac/bg/etf/pp1/code_generation/quicktest.mj");
//            File sourceCode = new File("test/rs/ac/bg/etf/pp1/semantic_analysis/test_method_calls.mj");
            log.info("Compiling source file: " + sourceCode.getAbsolutePath());

            br = new BufferedReader(new FileReader(sourceCode));
            Yylex lexer = new Yylex(br);

            // formiranje AST
            MJParser p = new MJParser(lexer);
            Symbol s = p.parse();  //pocetak parsiranja

            Program prog = (Program)(s.value);

            // inicijalizacija tabele simbola
            TabExtended.getInstance();

            log.info(prog.toString(""));
            log.info("===================================");

            // semanticka analiza
            Code.dataSize = 10;
            SemanticAnalyzer sa = new SemanticAnalyzer();
            prog.traverseBottomUp(sa);

            // ispis sintaksnog stabla
            log.info("===================================");
            tsdump();

			if(!p.isErrorDetected() && sa.passed()){
				log.info("Parsiranje uspesno zavrseno!");
                /* generisanje koda */
                File objFile = new File("src/rs/ac/bg/etf/pp1/code_generation/output/program.obj");
                if (objFile.exists()) objFile.delete();

                CodeGenerator codeGen = new CodeGenerator();
                prog.traverseBottomUp(codeGen);
//                Code.dataSize += sa.nVars;
                Code.mainPc = codeGen.getMainPC();
                Code.write(new FileOutputStream(objFile));
			}else{
				log.error("Parsiranje NIJE uspesno zavrseno!");
			}
        }
        finally {
            if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
        }

    }

}
