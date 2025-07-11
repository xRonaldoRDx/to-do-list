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

    @FXML private ComboBox<String> filtroStatusComboBox;

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

        tabelaTarefas.setRowFactory(tv -> new TableRow<Tarefa>() {
            @Override
            protected void updateItem(Tarefa tarefa, boolean empty) {
                super.updateItem(tarefa, empty);
                if (tarefa == null || empty) {
                    setStyle("");
                } else {
                    if (isSelected()) {
                        switch (tarefa.getStatus()) {
                            case NAO_INICIADA: setStyle("-fx-background-color: #A64B4B;"); break;
                            case EM_PROCESSAMENTO: setStyle("-fx-background-color: #A89855;"); break;
                            case CONCLUIDA: setStyle("-fx-background-color: #599962;"); break;
                            default: setStyle(""); break;
                        }
                    } else {
                        switch (tarefa.getStatus()) {
                            case NAO_INICIADA: setStyle("-fx-background-color: #613030;"); break;
                            case EM_PROCESSAMENTO: setStyle("-fx-background-color: #635B33;"); break;
                            case CONCLUIDA: setStyle("-fx-background-color: #355E3B;"); break;
                            default: setStyle(""); break;
                        }
                    }
                }
            }
        });

        filtroStatusComboBox.setItems(FXCollections.observableArrayList("Todas", "Não Iniciadas", "Em Processamento", "Concluídas", "Pendentes"));
        filtroStatusComboBox.getSelectionModel().selectFirst();
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
    private void handleFiltrarTarefas() {
        String filtroSelecionado = filtroStatusComboBox.getSelectionModel().getSelectedItem();
        List<Tarefa> tarefasFiltradas;

        if (tarefaService != null) {
            switch (filtroSelecionado) {
                case "Não Iniciadas":
                    tarefasFiltradas = tarefaService.findByStatus(StatusTarefa.NAO_INICIADA);
                    break;
                case "Em Processamento":
                    tarefasFiltradas = tarefaService.findByStatus(StatusTarefa.EM_PROCESSAMENTO);
                    break;
                case "Concluídas":
                    tarefasFiltradas = tarefaService.listarTarefasConcluidas();
                    break;
                case "Pendentes":
                    tarefasFiltradas = tarefaService.listarTarefasPendentes();
                    break;
                case "Todas":
                default:
                    tarefasFiltradas = tarefaService.listarTodasTarefas();
                    break;
            }
            listaDeTarefasObservavel.clear();
            listaDeTarefasObservavel.addAll(tarefasFiltradas);
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
        handleFiltrarTarefas();
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
            handleFiltrarTarefas();
        }
    }

    @FXML
    private void handleEditarTarefa() {
        Tarefa selecionada = tabelaTarefas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta("Atenção", "Por favor, selecione uma tarefa para editar.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selecionada.getTexto());
        dialog.setTitle("Editar Tarefa");
        dialog.setHeaderText("Editando a tarefa ID: " + selecionada.getId());
        dialog.setContentText("Novo texto:");

        Optional<String> resultado = dialog.showAndWait();

        resultado.ifPresent(novoTexto -> {
            if (!novoTexto.trim().isEmpty()) {
                tarefaService.editarTextoTarefa(selecionada.getId(), novoTexto);
                atualizarTabela();
                handleFiltrarTarefas();
            } else {
                mostrarAlerta("Erro", "O texto da tarefa não pode ser vazio.");
            }
        });
    }

    @FXML
    private void handleAvancarStatus() {
        Tarefa selecionada = tabelaTarefas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta("Atenção", "Por favor, selecione uma tarefa para avançar o status.");
            return;
        }
        tarefaService.avancarStatusTarefa(selecionada.getId());
        atualizarTabela();
        handleFiltrarTarefas();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}