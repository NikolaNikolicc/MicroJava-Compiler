package rs.ac.bg.etf.pp1.autorun;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.util.CompilerService;
import rs.etf.pp1.mj.runtime.Run;
import rs.etf.pp1.mj.runtime.disasm;
import rs.etf.pp1.symboltable.ModuleHandler;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;
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

    private static Path MJProgramFilePath;
    private static Path MJInputFilePath;
    private static Path MJObjectCodeFilePath;

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
            MJProgramFilePath = Paths.get(args[programFilePathIndex]);
        } else {
            logger.error("Path to the MicroJava program file not specified.");
            System.exit(RUNTIME_ERROR_CODE_PROGRAM_FILE_PATH_NOT_SPECIFIED);
        }

        int inputCommandIndex = Arrays.asList(args).indexOf("--input");
        int MJInputFilePathIndex = inputCommandIndex + 1;

        if (inputCommandIndex != -1 && MJInputFilePathIndex < args.length) {
            MJInputFilePath = Paths.get(args[MJInputFilePathIndex]);
        } else {
            logger.error("Path to the input file for the MicroJava program not specified.");
            System.exit(RUNTIME_ERROR_CODE_INPUT_FILE_PATH_NOT_SPECIFIED);
        }

        int outputCommandIndex = Arrays.asList(args).indexOf("--output");
        int outputFilePathIndex = outputCommandIndex + 1;
        if (outputCommandIndex != -1 && outputFilePathIndex < args.length) {
            MJObjectCodeFilePath = Paths.get(args[outputFilePathIndex],CompilerService.parseFileNameFromPath(MJProgramFilePath) + ".obj");
            CompilerService.setOutputFolderPath(Paths.get(args[outputFilePathIndex]));
        } else {
            logger.error("Path to the folder where MicroJava object code file should be generated not specified.");
            System.exit(RUNTIME_ERROR_CODE_OBJECT_CODE_FILE_PATH_NOT_SPECIFIED);
        }
    }

    public static void printSymbolTable() {
        SymbolTableVisitor symbolTableVisitor = new DumpSymbolTableVisitor();
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

//        CompilerService.initializeLogger();
        int status = CompilerService.build(MJProgramFilePath);
        if (status != CompilerService.COMPILATION_SUCCESSFUL) {
            System.exit(status);
        }
    }

    private static void executeDisasmCommand(String[] args) {
        if (!Arrays.asList(args).contains("--disasm")) {
            return;
        }

        try {
            logger.info("Disassembling the generated object code file: " + MJObjectCodeFilePath.toString() + "\n");
            String[] disassemblyargs = new String[] { MJObjectCodeFilePath.toString() };
            disasm.main(disassemblyargs);
            System.out.println();
            logger.info("Disassembly has completed successfully for the generated object code file: " + MJObjectCodeFilePath.toString() + "\n");
        } catch (Exception exception) {
            logger.error(exception);
            System.exit(RUNTIME_ERROR_CODE_GENERIC_RUNTIME_EXCEPTION);
        }
    }

    private static void executeDebugCommand(String[] args) {
        if (!Arrays.asList(args).contains("--debug")) {
            return;
        }

        try {
            logger.debug("Running the object code file in debug mode in MicroJava Virtual Machine: " + MJObjectCodeFilePath.toString());
            String[] debugargs = new String[] { MJObjectCodeFilePath.toString(), "-debug" };
            System.setIn(Files.newInputStream(MJInputFilePath));
            Run.main(debugargs);
            System.out.println();
            System.out.println();
            logger.debug("Run in debug mode has completed successfully for the object code file in MicroJava Virtual Machine: " + MJObjectCodeFilePath.toString() + "\n");
        } catch (Exception exception) {
            logger.error(exception);
            System.exit(RUNTIME_ERROR_CODE_GENERIC_RUNTIME_EXCEPTION);
        }
    }

    private static void executeRunCommand(String[] args) {
        if (!Arrays.asList(args).contains("--run")) {
            return;
        }

        try {
            logger.info("Running the object code file in MicroJava Virtual Machine: " + MJObjectCodeFilePath.toString() + "\n");
            String[] runargs = new String[] { MJObjectCodeFilePath.toString() };
            System.setIn(Files.newInputStream(MJInputFilePath));
            Run.main(runargs);
            System.out.println();
            System.out.println();
            logger.info("Run has completed successfully for the object code file in MicroJava Virtual Machine: " + MJObjectCodeFilePath.toString() + "\n");
        } catch (Exception exception) {
            logger.error(exception);
            System.exit(RUNTIME_ERROR_CODE_GENERIC_RUNTIME_EXCEPTION);
        }
    }

    public static void main(String[] args) {
        CompilerService.initializeLogger();
        logger = CompilerService.logger;
        loadFilePaths(args);
        CompilerService.initializaUniverseModule();
        // in this scope all modules will be defined
        Tab.openScope();
        executeBuildCommand(args);
        Tab.closeScope();
        SymbolTableVisitor symbolTableVisitor = new DumpSymbolTableVisitor();
        ModuleHandler.getInstance().dumpModules(symbolTableVisitor);
        logger.debug(symbolTableVisitor.getOutput());
        executeDisasmCommand(args);
        executeDebugCommand(args);
        executeRunCommand(args);
    }
}
