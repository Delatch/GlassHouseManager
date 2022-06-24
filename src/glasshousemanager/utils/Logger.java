package glasshousemanager.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Logger {
    public static void log(String logMessage) {
        System.out.println("(" + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(LocalDateTime.now()) + ") " + logMessage);
    }
}
