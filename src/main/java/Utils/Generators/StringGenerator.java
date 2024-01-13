package Utils.Generators;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.MessageDigest;

public class StringGenerator {
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:'\",.<>/?";

    public static String generateRandomString(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();

        String allCharacters = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARS;

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allCharacters.length());
            char randomChar = allCharacters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    public static String hashingPassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static boolean verifyPassword(String originPassword, String hashedPassword, String salt){
        return hashedPassword.equals(hashingPassword(originPassword, salt));
    }
}
