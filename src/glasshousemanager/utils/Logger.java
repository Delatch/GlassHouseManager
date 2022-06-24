package glasshousemanager.utils;

import java.time.LocalDateTime;

public class Logger {
    public void log(String logMessage) {
        System.out.println("(" + LocalDateTime.now() + ") " + logMessage);
    }
}
