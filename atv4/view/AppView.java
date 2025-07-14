package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.net.URL; // Importe para usar URL
import model.Repositorios;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Interface Gráfica (GUI) principal do CRUD.
 * Esta classe é o ponto de entrada da aplicação JavaFX e carrega o FXML principal.
 */
public class AppView extends Application
{
    private FXMLLoader loader;

    public AppView() {
        this.loader = new FXMLLoader();
        try {
            // *** MUDANÇA CRUCIAL AQUI: Usando getClass().getResource() ***
            // Busca o FXML como um recurso do classpath, relativo à localização desta classe.
            // Se AppView está em 'view/', e app.fxml está na mesma pasta 'view/', o caminho é "app.fxml".
            URL fxmlLocation = getClass().getResource("app.fxml");
            if (fxmlLocation == null) {
                // Mensagem mais clara se o FXML não for encontrado.
                throw new IllegalStateException("Erro: app.fxml não encontrado! Verifique o caminho e a estrutura de pastas. Caminho esperado: view/app.fxml");
            }
            this.loader.setLocation(fxmlLocation); // Define a localização do FXML para o loader
        } catch (Exception e) {
            System.err.println("Erro ao carregar o arquivo FXML principal: " + e.getMessage());
            e.printStackTrace();
            System.exit(1); // Encerra a aplicação em caso de erro fatal na inicialização do FXML
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Inicializa os repositórios AQUI no início da aplicação.
            // O banco de dados 'app.sqlite' será criado na raiz do seu projeto.
            Repositorios.initialize("app.sqlite");

            // Linha onde o loader tenta carregar o FXML.
            BorderPane root = loader.<BorderPane>load();
            Scene scene = new Scene(root);
            // Definir o título da janela principal
            primaryStage.setTitle("Sistema de Gerenciamento de Biblioteca");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao iniciar a aplicação: " + e.getMessage() + "\nVerifique os logs para mais detalhes.").show();
            System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
            e.printStackTrace();
            System.exit(1); // Encerra a aplicação em caso de erro fatal no start
        }
    }

    @Override
    public void stop() {
        // Fecha a conexão com o banco de dados ao encerrar a aplicação.
        Repositorios.closeDatabase();
        System.out.println("Aplicação encerrada. Conexão com o banco de dados fechada.");
        // O System.exit(0) já é implícito após o stop, mas pode ser usado para garantir.
        // System.exit(0);
    }

    // O método main tradicional. No BlueJ, é recomendado executá-lo a partir de MainApp.java.
    // Ele simplesmente delega o início do ciclo de vida do JavaFX.
    public static void main(String[] args) {
        Application.launch(args);
    }
}