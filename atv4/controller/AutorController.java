package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import model.Repositorios;
import model.Autor;
import model.Repositorio;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador para a entidade Autor, gerenciando a interação com a interface gráfica
 * e as operações CRUD para Autores. Estende AbstractCrudController para reuso de lógica.
 */
public class AutorController extends AbstractCrudController<Autor, view.Autor, Integer> {

    @FXML private TextField nomeField;
    @FXML private TextField nacionalidadeField;

    @FXML private TableColumn<view.Autor, Integer> idCol;
    @FXML private TableColumn<view.Autor, String> nomeCol;
    @FXML private TableColumn<view.Autor, String> nacionalidadeCol;

    /**
     * Construtor padrão.
     */
    public AutorController() {
        // O construtor da superclasse é chamado implicitamente ou pode ser chamado explicitamente com super().
    }

    /**
     * Retorna a instância do repositório de Autor.
     * @return O Repositorio de Autor.
     */
    @Override
    protected Repositorio<Autor, Integer> getRepositorio() {
        return Repositorios.getAutorRepositorio();
    }

    /**
     * Configura as colunas da TableView para exibir os dados do Autor.
     */
    @Override
    protected void setupTableColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        nacionalidadeCol.setCellValueFactory(new PropertyValueFactory<>("nacionalidade"));
    }

    /**
     * Carrega todos os Autores do banco de dados e os converte para objetos de view.Autor.
     * @return Uma ObservableList de view.Autor para a TableView.
     */
    @Override
    protected ObservableList<view.Autor> loadAllEntities() {
        ObservableList<view.Autor> lista = FXCollections.observableArrayList();
        try {
            List<Autor> autoresDoBanco = repositorio.readAll();
            for (Autor autor : autoresDoBanco) {
                lista.add(modelToView(autor)); // Converte model.Autor para view.Autor
            }
        } catch (SQLException e) {
            new Alert(AlertType.ERROR, "Erro ao carregar autores: " + e.getMessage()).show();
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Converte um objeto model.Autor para um objeto view.Autor.
     * @param modelEntity O objeto model.Autor a ser convertido.
     * @return O objeto view.Autor correspondente.
     */
    @Override
    protected view.Autor modelToView(Autor modelEntity) {
        return new view.Autor(
            modelEntity.getId(),
            modelEntity.getNome(),
            modelEntity.getNacionalidade()
        );
    }

    /**
     * Converte um objeto view.Autor para um objeto model.Autor.
     * Se o ID for maior que 0, ele é setado na entidade do modelo, indicando uma atualização.
     * @param viewEntity O objeto view.Autor a ser convertido.
     * @return O objeto model.Autor correspondente.
     */
    @Override
    protected Autor viewToModel(view.Autor viewEntity) {
        Autor autor = new Autor();
        // Se o ID da entidade da view for > 0, significa que é um item existente
        if (viewEntity.getId() > 0) {
            autor.setId(viewEntity.getId());
        }
        autor.setNome(viewEntity.getNome());
        autor.setNacionalidade(viewEntity.getNacionalidade());
        return autor;
    }

    /**
     * Cria (ou atualiza) um objeto model.Autor a partir dos campos da interface.
     * Utilizado nas operações de salvar (adicionar/atualizar).
     * @return O objeto model.Autor com os dados dos campos.
     */
    @Override
    protected Autor getEntityFromFields() {
        Autor autor = new Autor();
        // Tenta obter o ID do campo, se não estiver vazio.
        // Isso é importante para operações de atualização.
        if (!idField.getText().isEmpty() && !idField.getText().equals("Gerado Automaticamente")) {
            try {
                autor.setId(Integer.parseInt(idField.getText()));
            } catch (NumberFormatException e) {
                // Lidar com erro se o ID não for um número válido (embora o campo seja não editável)
                System.err.println("Erro de formato de ID: " + e.getMessage());
            }
        }
        autor.setNome(nomeField.getText());
        autor.setNacionalidade(nacionalidadeField.getText());
        return autor;
    }

    /**
     * Preenche os campos da interface com os dados de um objeto view.Autor.
     * Chamado ao selecionar um item na tabela.
     * @param viewEntity O objeto view.Autor cujos dados preencherão os campos.
     */
    @Override
    protected void populateFields(view.Autor viewEntity) {
        idField.setText(String.valueOf(viewEntity.getId()));
        nomeField.setText(viewEntity.getNome());
        nacionalidadeField.setText(viewEntity.getNacionalidade());
    }

    /**
     * Limpa todos os campos de entrada da interface.
     */
    @Override
    protected void clearFields() {
        idField.setText("");
        nomeField.setText("");
        nacionalidadeField.setText("");
    }

    /**
     * Habilita ou desabilita os campos de entrada da interface (exceto o ID).
     * @param disable true para desabilitar, false para habilitar.
     */
    @Override
    protected void setFieldsDisable(boolean disable) {
        nomeField.setDisable(disable);
        nacionalidadeField.setDisable(disable);
        // idField é sempre desabilitado para edição manual
    }

    /**
     * Obtém o ID do campo de texto ID e o converte para Integer.
     * @return O ID como Integer, ou null se o campo estiver vazio.
     */
    @Override
    protected Integer getIdFromField() {
        // Retorna null se o campo estiver vazio ou for "Gerado Automaticamente"
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
     * Valida os campos de entrada do Autor.
     * @return true se a entrada for válida, false caso contrário.
     */
    @Override
    protected boolean validateInput() {
        if (nomeField.getText().trim().isEmpty()) {
            new Alert(AlertType.WARNING, "O campo 'Nome' é obrigatório para um Autor.").show();
            return false;
        }
        return true;
    }

    /**
     * Retorna o nome da entidade para uso em mensagens de alerta.
     * @return "Autor".
     */
    @Override
    protected String getEntityName() {
        return "Autor";
    }
}