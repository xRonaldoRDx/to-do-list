package org.example;

import java.util.List;
import java.util.Optional;

public interface TarefaDAO {
    Tarefa save(Tarefa tarefa);
    List<Tarefa> findAll();
    Optional<Tarefa> findById(int id);
    Tarefa update(Tarefa tarefa);
    List<Tarefa> findByStatus(StatusTarefa status);
    void delete(int id);
}