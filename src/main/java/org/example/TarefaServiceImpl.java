package org.example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TarefaServiceImpl implements TarefaService {
    private final TarefaDAO tarefaDAO;

    public TarefaServiceImpl(TarefaDAO tarefaDAO) {
        this.tarefaDAO = tarefaDAO;
    }

    @Override
    public void adicionarTarefa(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            System.err.println("O texto da tarefa não pode ser vazio.");
            return;
        }
        Tarefa novaTarefa = new Tarefa();
        novaTarefa.setTexto(texto);
        novaTarefa.setStatus(StatusTarefa.NAO_INICIADA);
        novaTarefa.setDataAlteracao(LocalDateTime.now());
        tarefaDAO.save(novaTarefa);
        System.out.println("\nTarefa adicionada com sucesso!");
    }

    @Override
    public List<Tarefa> listarTodasTarefas() {
        return tarefaDAO.findAll();
    }

    @Override
    public void alterarStatusTarefa(int id, StatusTarefa novoStatus) {
        Optional<Tarefa> tarefaOptional = tarefaDAO.findById(id);
        if (tarefaOptional.isPresent()) {
            Tarefa tarefa = tarefaOptional.get();
            tarefa.setStatus(novoStatus);
            tarefa.setDataAlteracao(LocalDateTime.now());
            tarefaDAO.update(tarefa);
            System.out.println("\nStatus da tarefa " + id + " alterado para " + novoStatus);
        } else {
            System.err.println("Tarefa com ID " + id + " não encontrada.");
        }
    }
}