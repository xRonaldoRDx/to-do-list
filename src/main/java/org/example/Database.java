package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:tarefas.db";

    /**
     * Retorna uma NOVA conexão com o banco de dados a cada chamada.
     * Isso funciona perfeitamente com o padrão try-with-resources usado no DAO.
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            // Lança uma exceção para interromper a execução se a conexão falhar.
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }

    /**
     * Este método deve ser chamado uma única vez, no início da aplicação,
     * para garantir que a tabela de tarefas exista.
     */
    public static void inicializarBanco() {
        // Usamos try-with-resources aqui para garantir que esta conexão específica de inicialização seja fechada.
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS tarefas ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "texto TEXT NOT NULL,"
                    + "status TEXT NOT NULL,"
                    + "data_alteracao TEXT NOT NULL"
                    + ");";
            stmt.execute(sql);

        } catch (SQLException e) {
            // Lança uma exceção se a inicialização da tabela falhar.
            throw new RuntimeException("Erro ao inicializar o banco de dados: " + e.getMessage(), e);
        }
    }
}