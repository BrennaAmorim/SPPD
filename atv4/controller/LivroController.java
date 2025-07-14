package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import model.Repositorios;
import model.Livro;
import model.Autor; // Importe o Autor do modelo
import model.Repositorio;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controlador para a entidade Livro, gerenciando a interação com a interface gráfica
 * e as operações CRUD para Livros. Estende AbstractCrudController para reuso de lógica.
 */
public class LivroController extends AbstractCrudController<Livro, view.Livro, Integer> {

    @FXML private TextField tituloField;
    @FXML private ComboBox<view.Autor> autorComboBox; // ComboBox usa a classe view.Autor
    @FXML private TextField isbnField;
    @FXML private TextField anoPublicacaoField;

    @FXML private TableColumn<view.Livro, Integer> idCol;
    @FXML private TableColumn<view.Livro, String> tituloCol;
    @FXML private TableColumn<view.Livro, view.Autor> autorCol; // Coluna de Autor usa view.Autor
    @FXML private TableColumn<view.Livro, String> isbnCol;
    @FXML private TableColumn<view.Livro, Integer> anoPublicacaoCol;

    private ObservableList<view.Autor> autoresList; // Lista de autores para o ComboBox

    /**
     * Construtor padrão.
     */
    public LivroController() {
        // O construtor da superclasse é chamado implicitamente.
    }

    /**
     * Retorna a instância do repositório de Livro.
     * @return O Repositorio de Livro.
     */
    @Override
    protected Repositorio<Livro, Integer> getRepositorio() {
        return Repositorios.getLivroRepositorio();
    }

    /**
     * Configura as colunas da TableView para exibir os dados do Livro.
     */
    @Override
    protected void setupTableColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        tituloCol.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        // Para a coluna do Autor, usamos autorProperty() da classe view.Livro
        autorCol.setCellValueFactory(cellData -> cellData.getValue().autorProperty());
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        anoPublicacaoCol.setCellValueFactory(new PropertyValueFactory<>("anoPublicacao"));
    }

    /**
     * Carrega todos os Livros do banco de dados e os converte para objetos de view.Livro.
     * @return Uma ObservableList de view.Livro para a TableView.
     */
    @Override
    protected ObservableList<view.Livro> loadAllEntities() {
        ObservableList<view.Livro> lista = FXCollections.observableArrayList();
        try {
            List<Livro> livrosDoBanco = repositorio.readAll();
            for (Livro livro : livrosDoBanco) {
                lista.add(modelToView(livro)); // Converte model.Livro para view.Livro
            }
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, "Erro ao carregar livros: " + e.getMessage()).show();
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Carrega todos os autores do banco de dados para popular o ComboBox de autores.
     */
    private void loadAutoresForComboBox() {
        autoresList = FXCollections.observableArrayList();
        try {
            List<Autor> autoresModel = Repositorios.getAutorRepositorio().readAll();
            for (Autor autor : autoresModel) {
                // Converte model.Autor para view.Autor para exibição no ComboBox
                autoresList.add(new view.Autor(autor.getId(), autor.getNome(), autor.getNacionalidade()));
            }
            autorComboBox.setItems(autoresList);
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, "Erro ao carregar autores para o ComboBox: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    /**
     * Método de inicialização. Chama a inicialização da superclasse e carrega os autores para o ComboBox.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources); // Chama o initialize da AbstractCrudController
        loadAutoresForComboBox(); // Carrega os autores para o ComboBox
    }

    /**
     * Converte um objeto model.Livro para um objeto view.Livro.
     * Lida com a conversão do objeto Autor associado.
     * @param modelEntity O objeto model.Livro a ser convertido.
     * @return O objeto view.Livro correspondente.
     */
    @Override
    protected view.Livro modelToView(Livro modelEntity) {
        view.Autor viewAutor = null;
        if (modelEntity.getAutor() != null) {
            Autor autor = modelEntity.getAutor();
            // Converte model.Autor para view.Autor
            viewAutor = new view.Autor(autor.getId(), autor.getNome(), autor.getNacionalidade());
        }
        return new view.Livro(
            modelEntity.getId(),
            modelEntity.getTitulo(),
            viewAutor, // Passa o view.Autor
            modelEntity.getIsbn(),
            modelEntity.getAnoPublicacao()
        );
    }

    /**
     * Converte um objeto view.Livro para um objeto model.Livro.
     * É crucial buscar o objeto model.Autor completo do banco de dados,
     * pois a entidade do modelo Livro precisa da referência ao objeto Autor, não apenas ao seu ID.
     * @param viewEntity O objeto view.Livro a ser convertido.
     * @return O objeto model.Livro correspondente.
     */
    @Override
    protected Livro viewToModel(view.Livro viewEntity) {
        Livro livro = new Livro();
        if (viewEntity.getId() > 0) {
            livro.setId(viewEntity.getId());
        }
        livro.setTitulo(viewEntity.getTitulo());

        // Converte view.Autor para model.Autor, buscando-o do banco de dados
        if (viewEntity.getAutor() != null) {
            try {
                // Busca o autor completo do banco para garantir que todas as relações sejam mantidas
                Autor autorModel = Repositorios.getAutorRepositorio().read(viewEntity.getAutor().getId());
                livro.setAutor(autorModel);
            } catch (SQLException e) {
                new Alert(AlertType.ERROR, "Erro ao buscar autor para conversão: " + e.getMessage()).show();
                e.printStackTrace();
            }
        }
        livro.setIsbn(viewEntity.getIsbn());
        livro.setAnoPublicacao(viewEntity.getAnoPublicacao()); // Já é int na view.Livro

        return livro;
    }

    /**
     * Cria (ou atualiza) um objeto model.Livro a partir dos campos da interface.
     * Utilizado nas operações de salvar (adicionar/atualizar).
     * @return O objeto model.Livro com os dados dos campos.
     */
    @Override
    protected Livro getEntityFromFields() {
        Livro livro = new Livro();
        if (!idField.getText().isEmpty() && !idField.getText().equals("Gerado Automaticamente")) {
            try {
                livro.setId(Integer.parseInt(idField.getText()));
            } catch (NumberFormatException e) {
                System.err.println("Erro de formato de ID no Livro: " + e.getMessage());
            }
        }
        livro.setTitulo(tituloField.getText());

        // Obtém o Autor selecionado no ComboBox (que é um view.Autor)
        view.Autor selectedViewAutor = autorComboBox.getSelectionModel().getSelectedItem();
        if (selectedViewAutor != null) {
            try {
                // Busca o objeto model.Autor correspondente no banco de dados
                Autor autorModel = Repositorios.getAutorRepositorio().read(selectedViewAutor.getId());
                livro.setAutor(autorModel);
            } catch (SQLException e) {
                new Alert(AlertType.ERROR, "Erro ao obter autor do ComboBox para salvar: " + e.getMessage()).show();
                e.printStackTrace();
            }
        }

        livro.setIsbn(isbnField.getText());
        try {
            livro.setAnoPublicacao(Integer.parseInt(anoPublicacaoField.getText()));
        } catch (NumberFormatException e) {
            // A validação já deve pegar isso, mas é uma segurança.
            System.err.println("Erro: Ano de Publicação não é um número válido.");
            livro.setAnoPublicacao(0); // Valor padrão ou tratamento de erro
        }
        return livro;
    }

    /**
     * Preenche os campos da interface com os dados de um objeto view.Livro.
     * Chamado ao selecionar um item na tabela.
     * @param viewEntity O objeto view.Livro cujos dados preencherão os campos.
     */
    @Override
    protected void populateFields(view.Livro viewEntity) {
        idField.setText(String.valueOf(viewEntity.getId()));
        tituloField.setText(viewEntity.getTitulo());
        // Seleciona o autor correto no ComboBox
        if (viewEntity.getAutor() != null) {
            autorComboBox.getSelectionModel().select(
                autoresList.stream()
                           .filter(a -> a.getId() == viewEntity.getAutor().getId())
                           .findFirst()
                           .orElse(null)
            );
        } else {
            autorComboBox.getSelectionModel().clearSelection();
        }
        isbnField.setText(viewEntity.getIsbn());
        anoPublicacaoField.setText(String.valueOf(viewEntity.getAnoPublicacao()));
    }

    /**
     * Limpa todos os campos de entrada da interface.
     */
    @Override
    protected void clearFields() {
        idField.setText("");
        tituloField.setText("");
        autorComboBox.getSelectionModel().clearSelection();
        isbnField.setText("");
        anoPublicacaoField.setText("");
    }

    /**
     * Habilita ou desabilita os campos de entrada da interface (exceto o ID).
     * @param disable true para desabilitar, false para habilitar.
     */
    @Override
    protected void setFieldsDisable(boolean disable) {
        tituloField.setDisable(disable);
        autorComboBox.setDisable(disable);
        isbnField.setDisable(disable);
        anoPublicacaoField.setDisable(disable);
        // idField é sempre desabilitado para edição manual
    }

    /**
     * Obtém o ID do campo de texto ID e o converte para Integer.
     * @return O ID como Integer, ou null se o campo estiver vazio.
     */
    @Override
    protected Integer getIdFromField() {
        if (idField.getText().isEmpty() || idField.getText().equals("Gerado Automaticamente")) {
            return null;
        }
        try {
            return Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            new Alert(AlertType.ERROR, "ID inválido. Por favor, insira um número para o ID.").show();
            return null;
        }
    }

    /**
     * Valida os campos de entrada do Livro.
     * @return true se a entrada for válida, false caso contrário.
     */
    @Override
    protected boolean validateInput() {
        if (tituloField.getText().trim().isEmpty() || isbnField.getText().trim().isEmpty() ||
            anoPublicacaoField.getText().trim().isEmpty() || autorComboBox.getSelectionModel().isEmpty()) {
            new Alert(AlertType.WARNING, "Todos os campos (Título, Autor, ISBN, Ano de Publicação) são obrigatórios.").show();
            return false;
        }
        try {
            Integer.parseInt(anoPublicacaoField.getText());
        } catch (NumberFormatException e) {
            new Alert(AlertType.ERROR, "Ano de Publicação deve ser um número inteiro válido.").show();
            return false;
        }
        return true;
    }

    /**
     * Retorna o nome da entidade para uso em mensagens de alerta.
     * @return "Livro".
     */
    @Override
    protected String getEntityName() {
        return "Livro";
    }
}