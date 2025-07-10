package org.example;

import java.util.List;

public interface TarefaService {
    void adicionarTarefa(String texto);
    List<Tarefa> listarTodasTarefas();
    void alterarStatusTarefa(int id, StatusTarefa novoStatus);
}