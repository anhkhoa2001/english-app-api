package org.base.ultilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StringUtil {

    public static boolean isBlank(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static boolean isListEmpty(List input) {
        return input != null && !input.isEmpty();
    }

    public static String generatorCurrentDateTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.FORMAT_TIME.DATE_TIME);
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }
}
