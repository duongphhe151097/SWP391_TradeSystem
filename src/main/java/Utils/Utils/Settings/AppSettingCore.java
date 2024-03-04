package Utils.Utils.Settings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppSettingCore {
    private static final String FILE_CONFIG = "/appsettings.properties";
    private static Properties properties;
    private static volatile AppSettingCore instance;

    private AppSettingCore() {
        properties = new Properties();
    }

    public static AppSettingCore getInstance() {
        AppSettingCore result = instance;
        if (result == null) {
            result = new AppSettingCore();
            result.readConfig();
        }
        return result;
    }

    public String getKey(String key) {
        return properties.getProperty(key);
    }

    private void readConfig() {
        InputStream stream = null;
        try {
            stream = AppSettingCore.class.getResourceAsStream(FILE_CONFIG);
            properties.load(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
