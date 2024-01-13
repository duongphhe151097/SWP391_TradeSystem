package Utils.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator {

    public static boolean checkLength(int minLength, int maxLength, String input){
        int inputLength = input.length();
        return inputLength >= minLength && inputLength <= maxLength;
    }

    public static boolean checkMinLength(int minLength, String input){
        int inputLength = input.length();
        return inputLength >= minLength;
    }

    public static boolean checkMaxLength(int maxLength, String input){
        int inputLength = input.length();
        return inputLength <= maxLength;
    }

    public static boolean checkSpecialChar(String input){
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }


}
