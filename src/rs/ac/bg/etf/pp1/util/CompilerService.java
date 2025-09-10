package rs.ac.bg.etf.pp1.util;

import java_cup.runtime.Symbol;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import rs.ac.bg.etf.pp1.autorun.CompilerAutorun;
import rs.ac.bg.etf.pp1.code_generation.CodeGenerator;
import rs.ac.bg.etf.pp1.lexical_analysis.output.Yylex;
import rs.ac.bg.etf.pp1.semantic_analysis.SemanticAnalyzer;
import rs.ac.bg.etf.pp1.syntax_analysis.output.MJParser;
import rs.ac.bg.etf.pp1.syntax_analysis.output.ast.Program;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.ModuleHandler;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Scope;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CompilerService {

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

    static Logger logger;

    static {
        Path configPath = Paths.get("config/log4j.xml");
        DOMConfigurator.configure(configPath.toString());
        Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
    }

    public static void initializaUniverseModule(){

    }

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
            logger = Logger.getLogger(CompilerService.class);
        } catch (Exception e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
            System.exit(RUNTIME_ERROR_CODE_LOGGER_INITIALIZATION_FAILED);
        }
    }

    public static String toPackageName(Path fullPath) {
        // Convert to string
        String pathStr = fullPath.toString();

        // Remove extension if present
        int dotIndex = pathStr.lastIndexOf('.');
        String withoutExtension = (dotIndex == -1) ? pathStr : pathStr.substring(0, dotIndex);

        // Replace OS-specific separator (\ or /) with '.'
        return withoutExtension.replace(File.separatorChar, '.');
    }

    public static Path fromPackageName(String packageName) {
        // zameni '.' sa sistemskim separatorom
        String pathStr = packageName.replace('.', File.separatorChar);

        // dodaj ekstenziju
        pathStr = pathStr + ".mj";

        return Paths.get(pathStr);
    }


    public static Path removeExtension(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);

        return path.getParent() != null
                ? path.getParent().resolve(baseName)
                : Paths.get(baseName);
    }
}
