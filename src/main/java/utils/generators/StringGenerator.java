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

    private static String pagingUrlGenerateCommon(String currentPage, String pageSize, String pageRange, String[] filters) {
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

        if (filters != null) {
            for (String filter : filters) {
                if (!StringValidator.isNullOrBlank(filter)) {
                    result.append("&").append(filter);
                }
            }
        }

        return result.toString();
    }

    public static String pagingUrlGenerate(
            String currentPage,
            String pageSize,
            String pageRange,
            String search,
            String status,
            String start,
            String end
    ) {
        String[] filters = {
                (!StringValidator.isNullOrBlank(search)) ? "search=" + search : "",
                (!StringValidator.isNullOrBlank(status)) ? "f_status=" + status : "",
                (StringValidator.isNullOrBlank(start)) ? "f_start=" + start : "",
                (StringValidator.isNullOrBlank(end)) ? "f_end=" + end : ""
        };

        return pagingUrlGenerateCommon(currentPage, pageSize, pageRange, filters);
    }

    public static String pagingUrlGenerateTransactionHistory(
            String currentPage,
            String pageSize,
            String pageRange,
            String f_amountFrom,
            String f_amountTo,
            String id,
            String user,
            String start,
            String end
    ) {
        String[] filters = {
                (!StringValidator.isNullOrBlank(f_amountFrom)) ? "f_amountFrom=" + f_amountFrom : "",
                (!StringValidator.isNullOrBlank(f_amountTo)) ? "f_amountTo=" + f_amountTo : "",
                (!StringValidator.isNullOrBlank(id)) ? "id=" + id : "",
                (!StringValidator.isNullOrBlank(user)) ? "user=" + user : "",
                (StringValidator.isNullOrBlank(start)) ? "f_start=" + start : "",
                (StringValidator.isNullOrBlank(end)) ? "f_end=" + end : ""
        };

        return pagingUrlGenerateCommon(currentPage, pageSize, pageRange, filters);
    }

    public static String pagingUrlGenerateReportManager(
            String currentPage,
            String pageSize,
            String pageRange,
            String uname,
            String title,
            String status,
            String start,
            String end
    ) {
        String[] filters = {
                (!StringValidator.isNullOrBlank(uname)) ? "f_uname=" + uname : "",
                (!StringValidator.isNullOrBlank(title)) ? "f_title=" + title : "",
                (!StringValidator.isNullOrBlank(status)) ? "f_status=" + status : "",
                (!StringValidator.isNullOrBlank(start)) ? "f_start=" + start : "",
                (!StringValidator.isNullOrBlank(end)) ? "f_end=" + end : "",
        };

        return pagingUrlGenerateCommon(currentPage, pageSize, pageRange, filters);
    }

    public static String pagingUrlGenerateUserReport(
            String currentPage,
            String pageSize,
            String pageRange,
            String title,
            String status,
            String start,
            String end
    ) {
        String[] filters = {
                (!StringValidator.isNullOrBlank(title)) ? "f_title=" + title : "",
                (!StringValidator.isNullOrBlank(status)) ? "f_status=" + status : "",
                (!StringValidator.isNullOrBlank(start)) ? "f_start=" + start : "",
                (!StringValidator.isNullOrBlank(end)) ? "f_end=" + end : "",
        };

        return pagingUrlGenerateCommon(currentPage, pageSize, pageRange, filters);
    }
}
