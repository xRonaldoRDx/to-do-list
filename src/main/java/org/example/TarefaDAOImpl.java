import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}