package com.example.translationapi;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class DataBaseRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/translation_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "aircraft";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveRecord(String ip, String text, String translatedText){
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO records (ip_address, input_text, translated_text) VALUES ('"+ip+"', '"+text+"', '"+translatedText+"')");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
