package com.home.kivanov.examples.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class MainConfiguration {

    @Bean
    public Connection postgresConnection() {
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/storageAppDB/?user=postgres&password=postgres");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
