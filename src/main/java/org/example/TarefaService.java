package org.example;

import java.util.List;
import java.util.Optional;

public interface TarefaService {
    void adicionarTarefa(String texto);
    void alterarStatusTarefa(int id, StatusTarefa novoStatus);
    void delete(int id);
    void avancarStatusTarefa(int id);
    void editarTextoTarefa(int id, String novoTexto);

    List<Tarefa> listarTodasTarefas();
    List<Tarefa> listarTarefasPendentes();
    List<Tarefa> listarTarefasConcluidas();
    List<Tarefa> findByStatus(StatusTarefa status); // Esta linha declara o método na interface
}