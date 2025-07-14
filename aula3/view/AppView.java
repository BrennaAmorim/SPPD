package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;

/**
 * Interface Gráfica (GUI) do CRUD para a entidade Livro.
 * Esta classe é o ponto de entrada da aplicação JavaFX e carrega o arquivo FXML.
 *
 * @author marceloakira (e melhorias por IA)
 * @version 0.02
 */
public class AppView extends Application
{
    private FXMLLoader loader;
    private URL url;
    private Stage primaryStage; // Mantido para consistência, mas não é usado diretamente neste exemplo simples.

    public AppView() {
        this.loader = new FXMLLoader();
        try {
            // **Ajuste para BlueJ:**
            // Se o arquivo 'app.fxml' está na mesma pasta 'view' que esta classe 'AppView.java',
            // o caminho relativo "app.fxml" é suficiente para o FXMLLoader encontrá-lo.
            // Se você colocou 'app.fxml' em uma subpasta específica dentro de 'view',
            // o caminho precisaria ser ajustado (ex: "view/app.fxml" se a pasta do projeto BlueJ for a raiz).
            // A forma mais robusta no BlueJ é usar getClass().getResource() se o FXML estiver na mesma pasta ou em subpacotes da mesma hierarquia.
            // Exemplo: this.url = getClass().getResource("app.fxml");
            // Para simplicidade e compatibilidade com sua estrutura original, mantive o File().toURI().toURL()
            // e assumi que 'view/app.fxml' é o caminho relativo correto a partir da raiz do projeto BlueJ.
            this.url = new File("view/app.fxml").toURI().toURL();
        } catch (Exception e) {
            System.err.println("Erro ao carregar o arquivo FXML: " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace completo para depuração.
            System.exit(1); // Sai da aplicação em caso de erro crítico no carregamento do FXML.
        }
        this.loader.setLocation(this.url);
    }

    /**
     * O método 'start' é o principal método da aplicação JavaFX, chamado após a inicialização.
     * Ele carrega o FXML, cria a cena e exibe o palco (Stage).
     * @param primaryStage O palco principal da aplicação.
     * @throws Exception Se ocorrer um erro durante o carregamento do FXML ou exibição.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Carrega o layout definido no arquivo FXML.
            Pane pane = loader.<Pane>load();
            Scene scene = new Scene(pane); // Cria uma nova cena com o layout carregado.
            this.primaryStage = primaryStage; // Armazena a referência ao palco principal.
            primaryStage.setTitle("CRUD de Livros - JavaFX"); // Define o título da janela.
            primaryStage.setScene(scene); // Define a cena para o palco.
            primaryStage.show(); // Exibe a janela.
        } catch (Exception e) {
            System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace completo para depuração.
        }
    }

    /**
     * O método 'stop' é chamado quando a aplicação está prestes a ser encerrada.
     * Aqui, garantimos que o programa seja finalizado corretamente.
     */
    @Override
    public void stop() {
        // Garante que o aplicativo JavaFX seja completamente encerrado.
        System.exit(0);
    }

    /**
     * O método 'main' tradicional do Java. É o ponto de entrada da aplicação
     * quando executada como um programa Java comum. Ele delega o controle ao JavaFX.
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {
        Application.launch(args); // Inicia o ciclo de vida da aplicação JavaFX.
    }
}