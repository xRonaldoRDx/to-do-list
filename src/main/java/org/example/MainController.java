package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class MainController {
    private TarefaService tarefaService;
    private final ObservableList<Tarefa> listaDeTarefasObservavel = FXCollections.observableArrayList();

    @FXML private TableView<Tarefa> tabelaTarefas;
    @FXML private TableColumn<Tarefa, Integer> colunaId;
    @FXML private TableColumn<Tarefa, String> colunaDescricao;
    @FXML private TableColumn<Tarefa, StatusTarefa> colunaStatus;
    @FXML private TableColumn<Tarefa, LocalDateTime> colunaData;
    @FXML private TextField campoNovaTarefa;

    @FXML
    public void initialize() {
        tabelaTarefas.setItems(listaDeTarefasObservavel);
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("texto"));
        colunaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colunaData.setCellValueFactory(new PropertyValueFactory<>("dataAlteracao"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        colunaData.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : formatter.format(item));
            }
        });
    }

    public void setTarefaService(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
        atualizarTabela();
    }

    private void atualizarTabela() {
        if (tarefaService != null) {
            List<Tarefa> tarefasDoBanco = tarefaService.listarTodasTarefas();
            listaDeTarefasObservavel.clear();
            listaDeTarefasObservavel.addAll(tarefasDoBanco);
        }
    }

    @FXML
    private void handleAdicionarTarefa() {
        String texto = campoNovaTarefa.getText();
        if (texto == null || texto.trim().isEmpty()) {
            mostrarAlerta("Erro", "A descrição da tarefa não pode ser vazia.");
            return;
        }
        tarefaService.adicionarTarefa(texto);
        campoNovaTarefa.clear();
        atualizarTabela();
    }

    @FXML
    private void handleExcluirTarefa() {
        Tarefa selecionada = tabelaTarefas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta("Atenção", "Por favor, selecione uma tarefa para excluir.");
            return;
        }
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Excluir tarefa?");
        confirmacao.setContentText("ID: " + selecionada.getId() + "\nDescrição: " + selecionada.getTexto());
        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            tarefaService.delete(selecionada.getId());
            atualizarTabela();
        }
    }

    /**
     * VOLTAMOS A TER O MÉTODO ORIGINAL:
     * Marca diretamente a tarefa selecionada como CONCLUIDA.
     */
    @FXML
    private void handleMarcarConcluida() {
        Tarefa selecionada = tabelaTarefas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta("Atenção", "Por favor, selecione uma tarefa para marcar como concluída.");
            return;
        }
        tarefaService.alterarStatusTarefa(selecionada.getId(), StatusTarefa.CONCLUIDA);
        atualizarTabela();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}