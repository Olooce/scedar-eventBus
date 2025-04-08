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

    public static void info(String message) {
        log("INFO", message);
    }

    public static void warn(String message) {
        log("WARN", message);
    }

    public static void error(String message) {
        log("ERROR", message);
    }

    public static void error(String message, Throwable throwable) {
        log("ERROR", message + " - " + throwable.getMessage());
        throwable.printStackTrace(System.err);
    }

    private static void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.println("[" + timestamp + "] [" + level + "] " + message);
    }
}

