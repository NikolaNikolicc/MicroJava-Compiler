package rs.ac.bg.etf.pp1.autorun;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import java.io.File;
import java.util.Arrays;

public class AntRunner {
    private static final int SUCCESS_CODE = 0;
    private static final int RUNTIME_ERROR_CODE_INVALID_ANT_COMMAND = 1;
    private static final int RUNTIME_ERROR_CODE_ANT_COMMAND_MISSING = 2;

    private static void executeAntTarget(String antTargetName) {
        File antBuildFile = new File("build.xml");
        Project antProject = new Project();
        antProject.setUserProperty("ant.file", antBuildFile.getAbsolutePath());

        DefaultLogger antLogger = new DefaultLogger();
        antLogger.setErrorPrintStream(System.err);
        antLogger.setOutputPrintStream(System.out);
        antLogger.setMessageOutputLevel(Project.MSG_INFO);
        antProject.addBuildListener(antLogger);

        try {
            antProject.fireBuildStarted();
            antProject.init();
            ProjectHelper.configureProject(antProject, antBuildFile);
            antProject.executeTarget(antTargetName);
            antProject.fireBuildFinished(null);
            System.out.println("Execution of ant command for '" + antTargetName + "' target completed successfully.");
            System.exit(SUCCESS_CODE);
        } catch (Exception exception) {
            antProject.fireBuildFinished(exception);
            System.exit(RUNTIME_ERROR_CODE_INVALID_ANT_COMMAND);
        }
    }

    private static void executeAntCommand(String[] commandLineArguments) {
        if (Arrays.asList(commandLineArguments).contains("ant")) {
            int antCommandIndex = Arrays.asList(commandLineArguments).indexOf("ant");
            int antTargetNameIndex = antCommandIndex + 1;
            if (antCommandIndex != -1 && antTargetNameIndex < commandLineArguments.length) {
                String antTargetName = commandLineArguments[antTargetNameIndex];
                System.out.println("Executing ant command for '" + antTargetName + "' target...");
                executeAntTarget(antTargetName);
            } else {
                System.err.println("ERROR: Invalid ant command. Target name is missing.");
                System.exit(RUNTIME_ERROR_CODE_INVALID_ANT_COMMAND);
            }
        } else {
            System.err.println("ERROR: ant command is missing.");
            System.exit(RUNTIME_ERROR_CODE_ANT_COMMAND_MISSING);
        }
    }

    public static void main(String[] commandLineArguments) {
        executeAntCommand(commandLineArguments);
    }
}
