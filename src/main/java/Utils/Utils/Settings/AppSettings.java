package Utils.Utils.Settings;

import Utils.Settings.AppSettingCore;

public class AppSettings {
    private static final AppSettingCore setting = AppSettingCore.getInstance();

    public static String getAppName() {
        return setting.getKey("APP_NAME");
    }

    public static String getAuthor() {
        return setting.getKey("APP_AUTHOR");
    }

    public static String getAppVersion() {
        return setting.getKey("APP_VERSION");
    }

    public static String getDbName() {
        return setting.getKey("DB_NAME");
    }

    public static String getDbHost() {
        return setting.getKey("DB_HOST");
    }

    public static String getDbPort() {
        return setting.getKey("DB_PORT");
    }

    public static String getDbUser() {
        return setting.getKey("DB_USER");
    }

    public static String getDbPassword() {
        return setting.getKey("DB_PWD");
    }

    public static String passwordSalt() {
        return setting.getKey("APP_SALT");
    }

    public static int getPageSize() {
        return Integer.parseInt(setting.getKey("PAGE_SIZE"));
    }

    public static String getMailUser() {
        return setting.getKey("MAIL_USER");
    }

    public static String getMailPassword() {
        return setting.getKey("MAIL_PASSWORD");
    }
}
