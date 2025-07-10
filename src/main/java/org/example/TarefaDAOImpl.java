package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TarefaDAOImpl implements TarefaDAO {
    @Override
    public Tarefa save(Tarefa tarefa) {
        String sql = "INSERT INTO tarefas (texto, status, data_alteracao) VALUES (?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, tarefa.getTexto());
            pstmt.setString(2, tarefa.getStatus().name());
            pstmt.setString(3, tarefa.getDataAlteracao().toString());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        tarefa.setId(rs.getInt(1));
                    }
                }
            }
            return tarefa;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar tarefa: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Tarefa> findAll() {
        String sql = "SELECT * FROM tarefas";
        List<Tarefa> tarefas = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tarefas.add(mapResultSetToTarefa(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as tarefas: " + e.getMessage());
        }
        return tarefas;
    }

    private Tarefa mapResultSetToTarefa(ResultSet rs) throws SQLException {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(rs.getInt("id"));
        tarefa.setTexto(rs.getString("texto"));
        tarefa.setStatus(StatusTarefa.valueOf(rs.getString("status")));
        tarefa.setDataAlteracao(LocalDateTime.parse(rs.getString("data_alteracao")));
        return tarefa;
    }

    @Override
    public Optional<Tarefa> findById(int id) {
        String sql = "SELECT * FROM tarefas WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToTarefa(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tarefa por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Tarefa update(Tarefa tarefa) {
        String sql = "UPDATE tarefas SET texto = ?, status = ?, data_alteracao = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tarefa.getTexto());
            pstmt.setString(2, tarefa.getStatus().name());
            pstmt.setString(3, tarefa.getDataAlteracao().toString());
            pstmt.setInt(4, tarefa.getId());
            pstmt.executeUpdate();
            return tarefa;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar tarefa: " + e.getMessage());
            return null;
        }
    }
}