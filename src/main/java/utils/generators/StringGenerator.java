package utils.generators;

import utils.validation.StringValidator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class StringGenerator {
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
//    private static final String SPECIAL_CHARS = "!#$%&'()*+,-./:;<=>?@[\\]_";

    public static String generateRandomString(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();

        String allCharacters = LOWERCASE + UPPERCASE + DIGITS;

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

    public static boolean verifyPassword(String originPassword, String hashedPassword, String salt) {
        return hashedPassword.equals(hashingPassword(originPassword, salt));
    }

    public static String pagingUrlGenerate(String currentPage, String pageSize, String pageRange, String search, String status, String start, String end) {
        StringBuilder result = new StringBuilder();
        if (StringValidator.isNullOrBlank(currentPage)) {
            currentPage = "";
        }
        result.append("?current=").append(currentPage);

        if (StringValidator.isNullOrBlank(pageSize)) {
            pageSize = "";
        }
        result.append("&size=").append(pageSize);

        if (StringValidator.isNullOrBlank(pageRange)) {
            pageRange = "";
        }

        result.append("&range=").append(pageRange);
        if (!StringValidator.isNullOrBlank(search)) result.append("&search=").append(search);
        if (!StringValidator.isNullOrBlank(status)) result.append("&f_status=").append(status);
        if (StringValidator.isNullOrBlank(start)) result.append("&f_start=").append(start);
        if (StringValidator.isNullOrBlank(end)) result.append("&f_end=").append(end);

        return result.toString();
    }
}
