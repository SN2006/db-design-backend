package com.example.app.dbdesignbackend.db;

import com.example.app.dbdesignbackend.exception.UnableConnectToDatabaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class DBConnectionFactory {

    @Value("${database.url}")
    private String url;
    @Value("${dataabase.anonymous.username}")
    private String anonymousUsername;
    @Value("${dataabase.anonymous.password}")
    private String anonymousPassword;

    public Connection getConnection(String username, String password) {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new UnableConnectToDatabaseException("Failed to create database connection");
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, anonymousUsername, anonymousPassword);
        } catch (Exception e) {
            throw new UnableConnectToDatabaseException("Failed to create database connection");
        }
    }

}
