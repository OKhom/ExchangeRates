package com.javapro.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbProperties {
    private final String url;
    private final String user;
    private final String password;

    public DbProperties() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();

        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        url = properties.getProperty("db.url");
        user = properties.getProperty("db.user");
        password = properties.getProperty("db.password");
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
