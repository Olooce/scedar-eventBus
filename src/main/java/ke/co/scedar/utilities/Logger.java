package ke.co.scedar.utilities;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * scedar-eventBus-POC (ke.co.scedar.utilities)
 * Created by: oloo
 * On: 08/04/2025. 22:26
 * Description:
 **/

public class Logger {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ANSI escape codes for colors
    private static final String RESET = "\u001B[0m";
    private static final String INFO_COLOR = "\u001B[32m";   // Green
    private static final String WARN_COLOR = "\u001B[33m";   // Yellow
    private static final String ERROR_COLOR = "\u001B[31m";  // Red

    public static void info(String message) {
        log("INFO", message, INFO_COLOR);
    }

    public static void warn(String message) {
        log("WARN", message, WARN_COLOR);
    }

    public static void error(String message) {
        log("ERROR", message, ERROR_COLOR);
    }

    public static void error(String message, Throwable throwable) {
        log("ERROR", message + " - " + throwable.getMessage(), ERROR_COLOR);
        throwable.printStackTrace(System.err);
    }

    private static void log(String level, String message, String color) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String caller = getCallingClassAndMethod();
        System.out.println(color + "[" + timestamp + "] [" + level + "] " + caller + ": "+ message  + RESET);
    }

    private static String getCallingClassAndMethod() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        //NOTE: The first element is getStackTrace, the second is log, the third is level, and the fourth is the actual calling method
        if (stackTrace.length > 4) {
            String className = stackTrace[4].getClassName(); // The class name from which log() was called
            String methodName = stackTrace[4].getMethodName(); // The method name from which log() was called
            return className + "." + methodName;
        }
        return "Unknown class and method";
    }
}

