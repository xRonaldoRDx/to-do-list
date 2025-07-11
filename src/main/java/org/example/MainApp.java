package org.example;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Parent root = loader.load();
        TarefaDAO tarefaDAO = new TarefaDAOImpl();
        TarefaService tarefaService = new TarefaServiceImpl(tarefaDAO);
        MainController controller = loader.getController();
        controller.setTarefaService(tarefaService);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Gerenciador de Tarefas Moderno");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        Database.inicializarBanco();
        launch(args);
    }
}