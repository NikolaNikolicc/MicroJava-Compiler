package rs.ac.bg.etf.pp1.autorun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.code_generation.CodeGenerator;
import rs.ac.bg.etf.pp1.lexical_analysis.output.Yylex;
import rs.ac.bg.etf.pp1.util.MySymbolTableVisitor;
import rs.ac.bg.etf.pp1.semantic_analysis.SemanticAnalyzer;
import rs.ac.bg.etf.pp1.syntax_analysis.output.MJParser;
import rs.ac.bg.etf.pp1.syntax_analysis.output.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.util.TabExtended;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.mj.runtime.Run;
import rs.etf.pp1.mj.runtime.disasm;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;


public class CompilerAutorun {
    private static final int RUNTIME_ERROR_CODE_PROGRAM_FILE_PATH_NOT_SPECIFIED = 1;
    private static final int RUNTIME_ERROR_CODE_INPUT_FILE_PATH_NOT_SPECIFIED = 2;
    private static final int RUNTIME_ERROR_CODE_OBJECT_CODE_FILE_PATH_NOT_SPECIFIED = 3;
    private static final int RUNTIME_ERROR_CODE_SYNTAX_ANALYSIS_ERROR = 4;
    private static final int RUNTIME_ERROR_CODE_SEMANTIC_ANALYSIS_ERROR = 5;
    private static final int RUNTIME_ERROR_CODE_GENERIC_RUNTIME_EXCEPTION = 6;
    private static final int RUNTIME_ERROR_CODE_BUILD_COMMAND_MISSING = 7;
    private static final int RUNTIME_ERROR_CODE_PROGRAM_COMMAND_MISSING = 8;
    private static final int RUNTIME_ERROR_CODE_INPUT_COMMAND_MISSING = 9;
    private static final int RUNTIME_ERROR_CODE_OUTPUT_COMMAND_MISSING = 10;
    private static final int RUNTIME_ERROR_CODE_LOGGER_INITIALIZATION_FAILED = 11;

    private static Logger logger;

    private static String MJProgramFilePath;
    private static Path MJInputFilePath;
    private static String MJObjectCodeFilePath;

    private static void initializeLogger() {
        Path configPath = Paths.get("config/log4j.xml");
        try {
            if (!Files.exists(configPath.getParent())) {
                Files.createDirectories(configPath.getParent());
            }

            if (!Files.exists(configPath)) {
                String configContent =
                        "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                                "<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">\n" +
                                "<log4j:configuration xmlns:log4j=\"http://jakarta.apache.org/log4j/\">\n" +
                                "    <appender name=\"console\" class=\"org.apache.log4j.ConsoleAppender\">\n" +
                                "        <param name=\"Target\" value=\"System.out\"/>\n" +
                                "        <layout class=\"org.apache.log4j.PatternLayout\">\n" +
                                "            <param name=\"ConversionPattern\" value=\"%-5p %d{ABSOLUTE} - %m%n\"/>\n" +
                                "        </layout>\n" +
                                "    </appender>\n" +
                                "    <appender name=\"file\" class=\"org.apache.log4j.DailyRollingFileAppender\">\n" +
                                "        <param name=\"file\" value=\"logs/microjava_compiler.log\"/>\n" +
                                "        <layout class=\"org.apache.log4j.PatternLayout\">\n" +
                                "            <param name=\"ConversionPattern\" value=\"%-5p %d{ABSOLUTE} - %m%n\"/>\n" +
                                "        </layout>\n" +
                                "    </appender>\n" +
                                "    <root>\n" +
                                "        <priority value=\"debug\"/>\n" +
                                "        <appender-ref ref=\"file\"/>\n" +
                                "        <appender-ref ref=\"console\"/>\n" +
                                "    </root>\n" +
                                "</log4j:configuration>\n";
                Files.write(configPath, configContent.getBytes(StandardCharsets.UTF_8));
            }

            DOMConfigurator.configure(configPath.toString());
            Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
            logger = Logger.getLogger(Compiler.class);
        }
        catch (Exception e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
            System.exit(RUNTIME_ERROR_CODE_LOGGER_INITIALIZATION_FAILED);
        }
    }

    private static void loadFilePaths(String[] args) {
        if (!Arrays.asList(args).contains("--program")) {
            logger.error("--program command is missing.");
            System.exit(RUNTIME_ERROR_CODE_PROGRAM_COMMAND_MISSING);
        }
        if (!Arrays.asList(args).contains("--input")) {
            logger.error("--input command is missing.");
            System.exit(RUNTIME_ERROR_CODE_INPUT_COMMAND_MISSING);
        }
        if (!Arrays.asList(args).contains("--output")) {
            logger.error("--output command is missing.");
            System.exit(RUNTIME_ERROR_CODE_OUTPUT_COMMAND_MISSING);
        }

        int programCommandIndex = Arrays.asList(args).indexOf("--program");
        int programFilePathIndex = programCommandIndex + 1;
        if (programCommandIndex != -1 && programFilePathIndex < args.length){
            MJProgramFilePath = args[programFilePathIndex];
        }
        else {
            logger.error("Path to the MicroJava program file not specified.");
            System.exit(RUNTIME_ERROR_CODE_PROGRAM_FILE_PATH_NOT_SPECIFIED);
        }

        int inputCommandIndex = Arrays.asList(args).indexOf("--input");
        int MJInputFilePathIndex = inputCommandIndex + 1;

        if (inputCommandIndex != -1 && MJInputFilePathIndex < args.length) {
            MJInputFilePath = Paths.get(args[MJInputFilePathIndex]);
        }
        else {
            logger.error("Path to the input file for the MicroJava program not specified.");
            System.exit(RUNTIME_ERROR_CODE_INPUT_FILE_PATH_NOT_SPECIFIED);
        }

        int outputCommandIndex = Arrays.asList(args).indexOf("--output");
        int outputFilePathIndex = outputCommandIndex + 1;
        if (outputCommandIndex != -1 && outputFilePathIndex < args.length) {
            MJObjectCodeFilePath = args[outputFilePathIndex] + "/mjprogram.obj";
        }
        else
        {
            logger.error("Path to the folder where MicroJava object code file should be generated not specified.");
            System.exit(RUNTIME_ERROR_CODE_OBJECT_CODE_FILE_PATH_NOT_SPECIFIED);
        }
    }

    private static void printSymbolTable() {
        SymbolTableVisitor symbolTableVisitor = new MySymbolTableVisitor();
        for (Scope currentScope = Tab.currentScope; currentScope != null; currentScope = currentScope.getOuter()) {
            currentScope.accept(symbolTableVisitor);
        }
        logger.debug(symbolTableVisitor.getOutput());
    }

    private static void executeBuildCommand(String[] args) {
        if (!Arrays.asList(args).contains("--build")) {
            logger.error("Command for compiling the MicroJava program (--build) is missing.");
            System.exit(RUNTIME_ERROR_CODE_BUILD_COMMAND_MISSING);
        }

        try ( BufferedReader bufferedReader = new BufferedReader(new FileReader(MJProgramFilePath))) {
            logger.info("Compiling source file: " + MJProgramFilePath + "\n");

            Yylex lexer = new Yylex(bufferedReader);
            MJParser parser = new MJParser(lexer);
            Symbol abstractSyntaxTreeRootSymbol = parser.parse();
            Program programSyntaxNode = (Program) abstractSyntaxTreeRootSymbol.value;
            logger.info("=====================ABSTRACT SYNTAX TREE DUMP=========================");
            logger.info(programSyntaxNode.toString(""));
            logger.info("=====================ABSTRACT SYNTAX TREE END=========================");
            if (parser.isErrorDetected()) {
                logger.error("Errors were detected during syntax analysis, aborting compilation.");
                System.exit(RUNTIME_ERROR_CODE_SYNTAX_ANALYSIS_ERROR);
            }
            logger.info("Syntax analysis has completed successfully for the source file: " + MJProgramFilePath + "\n");

            TabExtended.getInstance();
            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
            logger.info("===================================");
            programSyntaxNode.traverseBottomUp(semanticAnalyzer);
            logger.info("===================================");
            logger.info("=====================SYMBOL TABLE DUMP=========================");
            printSymbolTable();
            logger.info("=========================SYMBOL TABLE END=========================");
            if (semanticAnalyzer.errorDetected) {
                logger.error("Errors were detected during semantic analysis, aborting compilation.");
                System.exit(RUNTIME_ERROR_CODE_SEMANTIC_ANALYSIS_ERROR);
            }
            logger.info("Semantic analysis has completed successfully for the source file: " + MJProgramFilePath + "\n");

            Code.dataSize = 1;
            CodeGenerator codeGenerator = new CodeGenerator();
            programSyntaxNode.traverseBottomUp(codeGenerator);
            Code.mainPc = codeGenerator.getMainPC();
            File objectCodeFile = new File(MJObjectCodeFilePath);
            if (objectCodeFile.exists()) {
                objectCodeFile.delete();
            }
            Code.write(Files.newOutputStream(objectCodeFile.toPath()));
            logger.info("Code generation has completed successfully for the source file: " + MJProgramFilePath + "\n");
        }
        catch (Exception exception) {
            logger.error(exception);
            System.exit(RUNTIME_ERROR_CODE_GENERIC_RUNTIME_EXCEPTION);
        }
    }

    private static void executeDisasmCommand(String[] args) {
        if (!Arrays.asList(args).contains("--disasm")) {
            return;
        }

        try {
            logger.info("Disassembling the generated object code file: " + MJObjectCodeFilePath + "\n");
            String[] disassemblyargs = new String[] { MJObjectCodeFilePath };
            disasm.main(disassemblyargs);
            System.out.println();
            logger.info("Disassembly has completed successfully for the generated object code file: " + MJObjectCodeFilePath + "\n");
        }
        catch (Exception exception) {
            logger.error(exception);
            System.exit(RUNTIME_ERROR_CODE_GENERIC_RUNTIME_EXCEPTION);
        }
    }

    private static void executeDebugCommand(String[] args) {
        if (!Arrays.asList(args).contains("--debug")) {
            return;
        }

        try {
            logger.debug("Running the object code file in debug mode in MicroJava Virtual Machine: " + MJObjectCodeFilePath);
            String[] debugargs = new String[] { MJObjectCodeFilePath, "-debug" };
            System.setIn(Files.newInputStream(MJInputFilePath));
            Run.main(debugargs);
            System.out.println();
            System.out.println();
            logger.debug("Run in debug mode has completed successfully for the object code file in MicroJava Virtual Machine: " + MJObjectCodeFilePath + "\n");
        }
        catch (Exception exception) {
            logger.error(exception);
            System.exit(RUNTIME_ERROR_CODE_GENERIC_RUNTIME_EXCEPTION);
        }
    }

    private static void executeRunCommand(String[] args) {
        if (!Arrays.asList(args).contains("--run")) {
            return;
        }

        try {
            logger.info("Running the object code file in MicroJava Virtual Machine: " + MJObjectCodeFilePath + "\n");
            String[] runargs = new String[] { MJObjectCodeFilePath };
            System.setIn(Files.newInputStream(MJInputFilePath));
            Run.main(runargs);
            System.out.println();
            System.out.println();
            logger.info("Run has completed successfully for the object code file in MicroJava Virtual Machine: " + MJObjectCodeFilePath + "\n");
        }
        catch (Exception exception) {
            logger.error(exception);
            System.exit(RUNTIME_ERROR_CODE_GENERIC_RUNTIME_EXCEPTION);
        }
    }

    public static void main(String[] args) {
        initializeLogger();
        loadFilePaths(args);
        executeBuildCommand(args);
        executeDisasmCommand(args);
        executeDebugCommand(args);
        executeRunCommand(args);
    }
}
