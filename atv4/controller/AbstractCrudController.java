package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleIntegerProperty; // Importe para idField

import model.Repositorio;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Classe abstrata base para controladores de operações CRUD.
 * Fornece a lógica comum para Adicionar, Atualizar, Deletar, Salvar e Cancelar,
 * gerenciando o estado da interface (edição, visualização) e a interação com o repositório.
 *
 * @param <M> O tipo da entidade do modelo (ex: model.Livro, model.Autor).
 * @param <V> O tipo da entidade da view (ex: view.Livro, view.Autor), usado na TableView.
 * @param <ID> O tipo da chave primária da entidade (ex: Integer).
 */
public abstract class AbstractCrudController<M, V, ID> implements Initializable {

    @FXML protected Button adicionarButton;
    @FXML protected Button atualizarButton;
    @FXML protected Button deletarButton;
    @FXML protected Button salvarButton;
    @FXML protected Button cancelarButton;
    @FXML protected TableView<V> tabela;
    @FXML protected TextField idField; // Campo de ID, comum a todas as entidades

    protected Repositorio<M, ID> repositorio;
    protected ObservableList<V> dados; // Dados exibidos na tabela
    protected boolean isUpdating = false; // Flag para indicar se a operação é de atualização

    /**
     * Construtor da classe abstrata.
     * Deve ser chamado pelos construtores das subclasses.
     */
    public AbstractCrudController() {
    }

    /**
     * Método de inicialização da interface. Chamado automaticamente pelo JavaFX.
     * Configura o repositório, colunas da tabela e carrega os dados iniciais.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Obtém a instância do repositório específico da subclasse
        this.repositorio = getRepositorio();
        // Configura as colunas da tabela
        setupTableColumns();
        // Carrega os dados iniciais e popula a tabela
        refreshTable();
        // Configura o estado inicial dos botões e campos
        setEditingMode(false); // Inicia no modo de visualização

        // Listener para quando uma linha da tabela é selecionada
        tabela.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    populateFields(newValue); // Preenche os campos com os dados da linha selecionada
                    setEditingMode(false); // Volta para modo de visualização (apenas preenchido)
                    adicionarButton.setDisable(false); // Habilita o botão Adicionar
                    atualizarButton.setDisable(false); // Habilita o botão Atualizar
                    deletarButton.setDisable(false); // Habilita o botão Deletar
                    salvarButton.setDisable(true);   // Desabilita Salvar
                    cancelarButton.setDisable(true); // Desabilita Cancelar
                } else {
                    // Se nenhuma linha estiver selecionada, limpa os campos e volta ao estado inicial
                    clearFields();
                    setEditingMode(false);
                    adicionarButton.setDisable(false);
                    atualizarButton.setDisable(true);
                    deletarButton.setDisable(true);
                    salvarButton.setDisable(true);
                    cancelarButton.setDisable(true);
                }
            }
        );
    }

    /**
     * Atualiza os dados na tabela buscando-os novamente do banco de dados.
     */
    protected void refreshTable() {
        dados = loadAllEntities(); // Carrega todas as entidades do banco
        tabela.setItems(dados); // Define os itens na tabela
        clearFields(); // Limpa os campos após a atualização
        setEditingMode(false); // Volta ao modo de visualização
    }

    /**
     * Callback para o botão "Adicionar".
     * Prepara a interface para a adição de um novo item.
     */
    @FXML
    protected void onAdicionarButtonAction() {
        clearFields(); // Limpa todos os campos para um novo registro
        setEditingMode(true); // Habilita campos e botões para edição/salvamento
        isUpdating = false; // Define que a operação não é uma atualização
        salvarButton.setDisable(false); // Habilita o botão Salvar
        cancelarButton.setDisable(false); // Habilita o botão Cancelar
        atualizarButton.setDisable(true); // Desabilita o botão Atualizar
        deletarButton.setDisable(true); // Desabilita o botão Deletar
        adicionarButton.setDisable(true); // Desabilita o próprio botão Adicionar
        idField.setText("Gerado Automaticamente"); // Feedback visual para ID
    }

    /**
     * Callback para o botão "Atualizar".
     * Prepara a interface para a atualização de um item existente.
     */
    @FXML
    protected void onAtualizarButtonAction() {
        if (tabela.getSelectionModel().getSelectedItem() != null) {
            setEditingMode(true); // Habilita campos para edição
            isUpdating = true; // Define que a operação é uma atualização
            salvarButton.setDisable(false); // Habilita o botão Salvar
            cancelarButton.setDisable(false); // Habilita o botão Cancelar
            adicionarButton.setDisable(true); // Desabilita Adicionar
            deletarButton.setDisable(true); // Desabilita Deletar
            atualizarButton.setDisable(true); // Desabilita o próprio Atualizar
        } else {
            new Alert(Alert.AlertType.WARNING, "Selecione um item para atualizar.").show();
        }
    }

    /**
     * Callback para o botão "Deletar".
     * Remove o item selecionado do banco de dados.
     */
    @FXML
    protected void onDeletarButtonAction() {
        V selectedItem = tabela.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja deletar o " + getEntityName() + " selecionado?");
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    try {
                        M modelEntityToDelete = viewToModel(selectedItem); // Converte a entidade da view para o modelo
                        repositorio.delete(modelEntityToDelete); // Deleta do banco
                        new Alert(Alert.AlertType.INFORMATION, getEntityName() + " deletado com sucesso!").show();
                        refreshTable(); // Atualiza a tabela
                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Erro ao deletar " + getEntityName() + ": " + e.getMessage()).show();
                        e.printStackTrace();
                    }
                }
            });
        } else {
            new Alert(Alert.AlertType.WARNING, "Selecione um item para deletar.").show();
        }
    }

    /**
     * Callback para o botão "Salvar".
     * Salva (cria ou atualiza) o item no banco de dados.
     */
    @FXML
    protected void onSalvarButtonAction() {
        if (!validateInput()) { // Valida os campos antes de salvar
            return;
        }

        try {
            M entity = getEntityFromFields(); // Obtém a entidade dos campos da UI

            if (isUpdating) {
                // Para atualização, precisamos garantir que o ID esteja presente.
                // O ID vem do idField, que é populado ao selecionar uma linha.
                if (getIdFromField() == null) {
                     new Alert(Alert.AlertType.ERROR, "Erro: ID não encontrado para atualização.").show();
                     return;
                }
                repositorio.update(entity); // Chama o método de atualização do repositório
                new Alert(Alert.AlertType.INFORMATION, getEntityName() + " atualizado com sucesso!").show();
            } else {
                repositorio.create(entity); // Chama o método de criação do repositório
                new Alert(Alert.AlertType.INFORMATION, getEntityName() + " adicionado com sucesso!").show();
            }
            refreshTable(); // Atualiza a tabela após a operação
            setEditingMode(false); // Volta ao modo de visualização
            isUpdating = false; // Reseta a flag de atualização
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Erro ao salvar " + getEntityName() + ": " + e.getMessage()).show();
            e.printStackTrace();
        } catch (NumberFormatException e) {
             new Alert(Alert.AlertType.ERROR, "Formato de número inválido em um dos campos. " + e.getMessage()).show();
             e.printStackTrace();
        }
    }

    /**
     * Callback para o botão "Cancelar".
     * Cancela a operação atual e retorna ao estado de visualização.
     */
    @FXML
    protected void onCancelarButtonAction() {
        clearFields(); // Limpa os campos
        setEditingMode(false); // Desabilita os campos e botões de edição
        isUpdating = false; // Reseta a flag de atualização
        tabela.getSelectionModel().clearSelection(); // Limpa a seleção da tabela
    }

    /**
     * Controla o estado dos botões e campos de entrada com base no modo de edição.
     * @param editing true para modo de edição, false para modo de visualização.
     */
    protected void setEditingMode(boolean editing) {
        // Os campos são desabilitados/habilitados pelas subclasses
        setFieldsDisable(!editing);

        adicionarButton.setDisable(editing);
        atualizarButton.setDisable(editing || tabela.getSelectionModel().isEmpty());
        deletarButton.setDisable(editing || tabela.getSelectionModel().isEmpty());
        salvarButton.setDisable(!editing);
        cancelarButton.setDisable(!editing);

        // O campo ID é sempre não editável, mas pode ser limpo ou preenchido.
        idField.setEditable(false);
    }


    // --- Métodos Abstratos que devem ser implementados pelas subclasses ---

    /**
     * Deve retornar o repositório específico para a entidade gerenciada.
     */
    protected abstract Repositorio<M, ID> getRepositorio();

    /**
     * Deve configurar as PropertyValueFactory para as colunas da TableView.
     */
    protected abstract void setupTableColumns();

    /**
     * Deve carregar todas as entidades do modelo do banco de dados e convertê-las
     * para a lista de entidades da view.
     */
    protected abstract ObservableList<V> loadAllEntities();

    /**
     * Deve converter uma entidade do modelo (M) para uma entidade da view (V).
     */
    protected abstract V modelToView(M modelEntity);

    /**
     * Deve converter uma entidade da view (V) para uma entidade do modelo (M).
     */
    protected abstract M viewToModel(V viewEntity);

    /**
     * Deve extrair os dados dos campos da UI e criar uma nova instância da entidade do modelo.
     */
    protected abstract M getEntityFromFields();

    /**
     * Deve preencher os campos da UI com os dados de uma entidade da view selecionada.
     */
    protected abstract void populateFields(V viewEntity);

    /**
     * Deve limpar todos os campos da UI.
     */
    protected abstract void clearFields();

    /**
     * Deve habilitar ou desabilitar os campos de entrada da UI, exceto o ID.
     */
    protected abstract void setFieldsDisable(boolean disable);

    /**
     * Deve obter o ID do campo ID da UI, convertendo para o tipo ID.
     */
    protected abstract ID getIdFromField();

    /**
     * Deve conter a lógica de validação dos campos de entrada.
     * Retorna true se os campos são válidos, false caso contrário.
     */
    protected abstract boolean validateInput();

    /**
     * Deve retornar o nome da entidade (ex: "Livro", "Autor") para mensagens de alerta.
     */
    protected abstract String getEntityName();
}