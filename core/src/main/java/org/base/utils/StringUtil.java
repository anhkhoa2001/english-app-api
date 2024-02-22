
package org.base.utils;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class StringUtil {

    public static boolean isBlank(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static boolean isListEmpty(List input) {
        return input == null || input.isEmpty();
    }

    public static boolean isObject(Object object) {
        return object != null;
    }

    public static String nvl(Object objInput, String strNullValue) {
        if (objInput == null)
            return strNullValue;
        return objInput.toString();
    }

    public static String generatorCurrentDateTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.FORMAT_TIME.DATE_TIME);
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }

    public static boolean isNumberic(String sNumber) {
        if (sNumber == null || "".equals(sNumber)) {
            return false;
        }
        char ch_max = (char) 0x39;
        char ch_min = (char) 0x30;

        for (int i = 0; i < sNumber.length(); i++) {
            char ch = sNumber.charAt(i);
            if ((ch < ch_min) || (ch > ch_max)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isUUID(String string) {
        if (isNullOrEmpty(string)) {
            return false;
        }
        try {
            UUID.fromString(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    public static String convertToQueryString(Map<String, String> parameters) {
        return parameters.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }
}