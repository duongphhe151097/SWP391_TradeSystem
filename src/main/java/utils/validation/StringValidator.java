package utils.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator {

    public static boolean checkLength(int minLength, int maxLength, String input) {
        int inputLength = input.length();
        return inputLength >= minLength && inputLength <= maxLength;
    }

    public static boolean checkMinLength(int minLength, String input) {
        int inputLength = input.length();
        return inputLength >= minLength;
    }

    public static boolean checkMaxLength(int maxLength, String input) {
        int inputLength = input.length();
        return inputLength <= maxLength;
    }

    public static boolean checkSpecialChar(String input) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public static boolean isValidEmail(String input) {
        if (input == null || input.isBlank()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public static boolean isValidPassword(String input) {
        if (input == null || input.isBlank()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,32}$");
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public static boolean isTrue(boolean... otherValues) {
        for (boolean value : otherValues) {
            if (!value) {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidUsername(String input) {
        if (input == null || input.isBlank()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public static boolean isValidFullname(String input) {
        return input != null && !input.isBlank() && input.length() <= 500;
    }

    public static boolean isValidUserStatus(String input) {
        if (isNullOrBlank(input)) {
            return false;
        }
        String[] validStatus = new String[]{"ALL", "0", "1", "2", "3"};
        return Arrays.asList(validStatus).contains(input);
    }

    public static boolean isValidDateFormat(String input) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);

        try {
            // Try to parse the input string
            Date parsedDate = dateFormat.parse(input);
            // If parsing is successful, the input string matches the pattern
            return true;
        } catch (ParseException e) {
            // Parsing failed, so the input string does not match the pattern
            return false;
        }
    }




    public static boolean isNullOrBlank(String input){
        return input == null || input.isBlank();
    }
}
