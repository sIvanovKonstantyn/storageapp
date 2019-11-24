package com.home.kivanov.examples.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractRepository {

    private static final String URL = "jdbc:postgresql://localhost:5432/storageAppDB/?user=postgres&password=postgres";

    protected Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
