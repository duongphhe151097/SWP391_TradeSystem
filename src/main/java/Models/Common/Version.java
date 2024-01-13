package Models.Common;

import Utils.Settings.AppSettings;

public class Version {
    private String appName;
    private String appAuthor;
    private String appVersion;

    public Version(){
        this.appVersion = AppSettings.getAppVersion();
        this.appAuthor = AppSettings.getAuthor();
        this.appName = AppSettings.getAppName();
    }

    public Version(String appName, String appAuthor, String appVersion) {
        this.appName = appName;
        this.appAuthor = appAuthor;
        this.appVersion = appVersion;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppAuthor() {
        return appAuthor;
    }

    public void setAppAuthor(String appAuthor) {
        this.appAuthor = appAuthor;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
