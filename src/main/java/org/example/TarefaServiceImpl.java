package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
            return;
        }
        Tarefa novaTarefa = new Tarefa();
        novaTarefa.setTexto(texto);
        novaTarefa.setStatus(StatusTarefa.NAO_INICIADA);
        novaTarefa.setDataAlteracao(LocalDateTime.now());
        tarefaDAO.save(novaTarefa);
    }

    @Override
    public void alterarStatusTarefa(int id, StatusTarefa novoStatus) {
        Optional<Tarefa> tarefaOptional = tarefaDAO.findById(id);
        if (tarefaOptional.isPresent()) {
            Tarefa tarefa = tarefaOptional.get();
            tarefa.setStatus(novoStatus);
            tarefa.setDataAlteracao(LocalDateTime.now());
            tarefaDAO.update(tarefa);
        }
    }

    @Override
    public void delete(int id) {
        tarefaDAO.delete(id);
    }

    @Override
    public List<Tarefa> listarTodasTarefas() {
        return tarefaDAO.findAll();
    }

    @Override
    public List<Tarefa> listarTarefasPendentes() {
        List<Tarefa> pendentes = new ArrayList<>();
        pendentes.addAll(tarefaDAO.findByStatus(StatusTarefa.NAO_INICIADA));
        pendentes.addAll(tarefaDAO.findByStatus(StatusTarefa.EM_PROCESSAMENTO));
        return pendentes;
    }

    @Override
    public List<Tarefa> listarTarefasConcluidas() {
        return tarefaDAO.findByStatus(StatusTarefa.CONCLUIDA);
    }
}