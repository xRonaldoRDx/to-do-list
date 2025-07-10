import java.util.List;

public interface TarefaDAO {
    Tarefa save(Tarefa tarefa);
    List<Tarefa> findAll();
}