package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador principal da aplicação.
 * Responsável por gerenciar a TabPane e a estrutura geral da interface.
 * Os controladores específicos de cada aba (Livro, Autor) serão carregados via fx:include.
 */
public class AppController implements Initializable {

    @FXML
    private TabPane mainTabPane; // Se você quiser manipular a TabPane, declare aqui.

    public AppController() {
        // Construtor
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Qualquer lógica de inicialização para a TabPane principal pode ir aqui.
        // Por exemplo, selecionar uma aba específica ao iniciar:
        // mainTabPane.getSelectionModel().select(0); // Seleciona a primeira aba
    }
}