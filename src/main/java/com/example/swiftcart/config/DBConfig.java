package com.example.swiftcart.config;

import java.io.InputStream;
import java.util.Properties;

public class DBConfig {

    private static final String PROPS_FILE = "/application.properties";

    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream in = DBConfig.class.getResourceAsStream(PROPS_FILE)) {
            Properties p = new Properties();
            if (in != null) {
                p.load(in);
                url = p.getProperty("db.url");
                user = p.getProperty("db.user");
                password = p.getProperty("db.password");
            } else {
                throw new RuntimeException("application.properties not found on classpath");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load DB config: " + e.getMessage(), e);
        }
    }

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }
}
