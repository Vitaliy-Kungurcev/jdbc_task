package com.epam.java_jdbc.configuration;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private Connection connection;

    Properties properties = loadProperties();

    public ConnectionManager() throws IOException {
        try {
            String host = properties.getProperty("host");
            String login = properties.getProperty("login");
            String password = properties.getProperty("password");
            connection = DriverManager.getConnection(host, login, password);
            System.out.println("Connect open");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static Properties loadProperties() throws IOException {
        File file = new File("src/main/resources/config.properties");
        Properties properties = new Properties();
        properties.load(new FileReader(file));

        return properties;
    }

    public void closeConnection() throws SQLException {
        connection.close();
        System.out.println("Connect close");
    }
}
