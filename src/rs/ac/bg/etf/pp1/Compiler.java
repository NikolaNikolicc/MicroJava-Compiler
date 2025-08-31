package rs.ac.bg.etf.pp1;

import java.io.*;
import java.nio.file.Files;
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

    public static void dump() {
        MySymbolTableVisitor stv = new MySymbolTableVisitor();
        for(Scope s = Tab.currentScope; s != null; s = s.getOuter()) {
            s.accept(stv);
        }
        System.out.println(stv.getOutput());
    }

    public static void main(String[] args) throws Exception {

        Logger log = Logger.getLogger(MJParser.class);

        Reader bufferedReader = null;
        try {
            File sourceCode = new File("test/rs/ac/bg/etf/pp1/official_tests/test303.mj");
            log.info("Compiling source file: " + sourceCode.getAbsolutePath());

            bufferedReader = new BufferedReader(new FileReader(sourceCode));
            // lexical analysis
            Yylex lexer = new Yylex(bufferedReader);
            // syntax analysis
            MJParser parser = new MJParser(lexer);
            Symbol abstractSyntaxTreeRootSymbol = parser.parse();
            Program programSyntaxNode = (Program)(abstractSyntaxTreeRootSymbol.value);
            log.info("=====================ABSTRACT SYNTAX TREE DUMP=========================");
            log.info(programSyntaxNode.toString(""));
            log.info("=====================ABSTRACT SYNTAX TREE END=========================");
            if (parser.isErrorDetected()) {
            	log.error("Errors were detected during syntax analysis, compilation aborted.");
            	return;
            }
            log.info("Syntax analysis has completed successfully.");
            // semantic analysis
            TabExtended.getInstance();
            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
            log.info("===================================");
            programSyntaxNode.traverseBottomUp(semanticAnalyzer);
            log.info("===================================");
            log.info("=====================SYMBOL TABLE DUMP=========================");
            dump();
            log.info("=====================SYMBOL TABLE END=========================");
            if (semanticAnalyzer.errorDetected) {
                log.error("Errors were detected during semantic analysis, aborting compilation.");
                return;
            }
            log.info("Semantic analysis has completed successfully");
            // code generation
            Code.dataSize = 1;
            CodeGenerator codeGenerator = new CodeGenerator();
            programSyntaxNode.traverseBottomUp(codeGenerator);
            Code.mainPc = codeGenerator.getMainPC();
            log.info("Code generation has completed successfully.");
            // save generated code to .obj file
            File objFile = new File("src/rs/ac/bg/etf/pp1/code_generation/output/mjprogram.obj");
            if (objFile.exists()) objFile.delete();
            Code.write(Files.newOutputStream(objFile.toPath()));
        }
        finally {
            if (bufferedReader != null) try { bufferedReader.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
        }

    }
}
