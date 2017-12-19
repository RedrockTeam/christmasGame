package org.redrock.christmas.util;

import java.io.*;
import java.util.Properties;

public class PropertyUtil {
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            InputStream inputStream = PropertyUtil.class.getResourceAsStream("/dao.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public static void setProperties(String propertyName, String propertyValue, String comments) throws IOException {
        String filepath = PropertyUtil.class.getResource("/config.properties").getFile();
        Writer writer = new FileWriter(
                new File(filepath));
        properties.setProperty(propertyName, propertyValue);
        properties.store(writer, comments);
        writer.close();
    }
}
