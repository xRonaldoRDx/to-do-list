package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:tarefas.db";
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL);
                inicializarBanco();
            } catch (SQLException e) {
                System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            }
        }
        return connection;
    }

    private static void inicializarBanco() {
        String sql = "CREATE TABLE IF NOT EXISTS tarefas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "texto TEXT NOT NULL,"
                + "status TEXT NOT NULL,"
                + "data_alteracao TEXT NOT NULL"
                + ");";
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}