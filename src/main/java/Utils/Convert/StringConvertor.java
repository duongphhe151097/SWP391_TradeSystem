package Utils.Convert;

import Utils.Settings.AppSettings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class StringConvertor {

    public static int convertToInt(String input){
        int output = 0;
        try{
            output = Integer.parseInt(input);
        }catch (Exception e){
            throw new NumberFormatException();
        }
        return output;
    }

    public static String encodeBase64(String input){
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static String decodeBase64(String input){
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        return new String(decodedBytes);
    }
}
