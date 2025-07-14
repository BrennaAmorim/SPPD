package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType; // Necessário para caixas de diálogo de confirmação
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Optional; // Necessário para lidar com o retorno de caixas de diálogo

import model.LivroRepositorio;
import view.AppView;

public class AppController implements Initializable {
    @FXML
    private TableView<view.Livro> tabela;
    @FXML
    private TableColumn<view.Livro, Integer> idCol;
    @FXML
    private TableColumn<view.Livro, String> tituloCol;
    @FXML
    private TableColumn<view.Livro, String> autorCol;
    @FXML
    private TableColumn<view.Livro, String> isbnCol;
    @FXML
    private TableColumn<view.Livro, Integer> anoPublicacaoCol;
    @FXML
    private TextField idField;
    @FXML
    private TextField tituloField;
    @FXML
    private TextField autorField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField anoPublicacaoField;
    @FXML
    private Button adicionarButton;
    @FXML
    private Button atualizarButton;
    @FXML
    private Button deletarButton;
    @FXML
    private Button cancelarButton;
    @FXML
    private Button salvarButton;

    // A instância da AppView não é diretamente usada aqui, pois o FXML se encarrega de conectar o controlador à view.
    // O método main da AppView é o ponto de entrada principal da aplicação JavaFX.
    // AppView appView;

    // Instâncias estáticas para o banco de dados e repositório de livros.
    // O banco de dados 'app.sqlite' será criado na mesma pasta onde o projeto BlueJ é executado.
    private static model.Database database = new model.Database("app.sqlite");
    private static model.LivroRepositorio livroRepo = new model.LivroRepositorio(database);

    public AppController() {
        // O construtor é chamado pelo FXMLLoader. A inicialização principal ocorre no método initialize.
    }

    // O método main é o ponto de partida para a aplicação quando executada via BlueJ (clicando em AppView.main).
    public static void main(String[] args) throws Exception {
        AppView.main(args); // Chama o método main da classe AppView para iniciar a aplicação JavaFX.
    }

    /**
     * Gerencia o estado de habilitação dos botões da interface.
     * @param adicionar Define se o botão 'Adicionar' está desabilitado.
     * @param atualizar Define se o botão 'Atualizar' está desabilitado.
     * @param deletar Define se o botão 'Deletar' está desabilitado.
     * @param cancelar Define se o botão 'Cancelar' está desabilitado.
     * @param salvar Define se o botão 'Salvar' está desabilitado.
     */
    private void desabilitarBotoes(boolean adicionar, boolean atualizar, boolean deletar, boolean cancelar, boolean salvar) {
        adicionarButton.setDisable(adicionar);
        atualizarButton.setDisable(atualizar);
        deletarButton.setDisable(deletar);
        cancelarButton.setDisable(cancelar);
        salvarButton.setDisable(salvar);
    }

    /**
     * Gerencia o estado de habilitação dos campos de texto da interface.
     * O campo 'idField' é sempre desabilitado para edição manual, pois é gerado automaticamente.
     * @param desabilitado True para desabilitar todos os campos editáveis, false para habilitar.
     */
    private void desabilitarCampos(boolean desabilitado) {
        tituloField.setDisable(desabilitado);
        autorField.setDisable(desabilitado);
        isbnField.setDisable(desabilitado);
        anoPublicacaoField.setDisable(desabilitado);
        idField.setDisable(true); // O ID nunca deve ser editável pelo usuário
    }

    /**
     * Limpa o texto de todos os campos da interface.
     */
    private void limparCampos() {
        idField.setText("");
        tituloField.setText("");
        autorField.setText("");
        isbnField.setText("");
        anoPublicacaoField.setText("");
    }

    /**
     * Ação disparada pelo botão 'Cancelar'.
     * Restaura a interface para o estado inicial: campos desabilitados,
     * botões 'Atualizar', 'Deletar', 'Cancelar' e 'Salvar' desabilitados,
     * e apenas o botão 'Adicionar' habilitado. Limpa a seleção da tabela.
     */
    @FXML
    public void onCancelarButtonAction() {
        desabilitarCampos(true); // Desabilita os campos de texto
        desabilitarBotoes(false, true, true, true, true); // Habilita apenas o botão Adicionar
        limparCampos(); // Limpa todos os campos
        tabela.getSelectionModel().select(-1); // Desseleciona qualquer item na tabela
    }

    /**
     * Ação disparada pelo botão 'Salvar'.
     * Realiza a operação de **criação** (se o ID estiver vazio) ou **atualização** (se o ID estiver preenchido).
     * Inclui validações básicas nos campos de entrada.
     */
    @FXML
    public void onSalvarButtonAction() {
        try {
            // **Melhoria:** Validação de campos obrigatórios para evitar dados inconsistentes.
            if (tituloField.getText().isEmpty() || autorField.getText().isEmpty() ||
                isbnField.getText().isEmpty() || anoPublicacaoField.getText().isEmpty()) {
                new Alert(AlertType.WARNING, "Por favor, preencha todos os campos obrigatórios (Título, Autor, ISBN, Ano de Publicação).").show();
                return; // Impede a continuação se os campos essenciais estiverem vazios.
            }

            // **Melhoria:** Validação para o campo Ano de Publicação (deve ser um número inteiro).
            int anoPublicacao;
            try {
                anoPublicacao = Integer.parseInt(anoPublicacaoField.getText());
            } catch (NumberFormatException e) {
                new Alert(AlertType.ERROR, "O campo 'Ano de Publicação' deve conter um número inteiro válido.").show();
                return; // Impede a continuação se o ano não for um número.
            }

            model.Livro livro = new model.Livro();
            livro.setTitulo(tituloField.getText());
            livro.setAutor(autorField.getText());
            livro.setIsbn(isbnField.getText());
            livro.setAnoPublicacao(anoPublicacao);

            // Verifica se o campo ID está preenchido para determinar se é uma atualização ou uma nova criação.
            if (!idField.getText().isEmpty()) {
                // **Operação de Atualização (UPDATE)**
                livro.setId(Integer.parseInt(idField.getText())); // O ID é necessário para identificar o livro a ser atualizado.
                livroRepo.update(livro); // Chama o método de atualização no repositório.

                // Atualiza o item na ObservableList da tabela para refletir as mudanças na UI sem recarregar tudo.
                view.Livro livroViewAtualizado = modelToView(livro);
                int selectedIndex = tabela.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) { // Garante que há um item selecionado para atualizar.
                    tabela.getItems().set(selectedIndex, livroViewAtualizado);
                }
                new Alert(AlertType.INFORMATION, "Livro atualizado com sucesso!").show();
            } else {
                // **Operação de Criação (CREATE)**
                model.Livro livro_salvo = livroRepo.create(livro); // Salva o novo livro no banco de dados.
                view.Livro livroView = modelToView(livro_salvo); // Converte o livro do modelo para o formato da view.
                tabela.getItems().add(livroView); // Adiciona o novo livro à tabela na interface.
                tabela.getSelectionModel().select(livroView); // Seleciona o livro recém-adicionado na tabela.
                new Alert(AlertType.INFORMATION, "Livro adicionado com sucesso!").show();
            }

            // Retorna a interface ao estado de "visualização": campos desabilitados e botões de ação restritos.
            desabilitarCampos(true);
            desabilitarBotoes(false, true, true, true, true);
            limparCampos(); // Limpa os campos após a operação.
            tabela.getSelectionModel().select(-1); // Desseleciona qualquer item da tabela.
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao salvar o livro: " + e.getMessage()).show();
            e.printStackTrace(); // Imprime o stack trace para ajudar na depuração de erros inesperados.
        }
    }

    /**
     * Ação disparada pelo botão 'Adicionar'.
     * Prepara a interface para a inserção de um novo livro: limpa campos,
     * habilita campos para edição e ajusta os botões.
     */
    @FXML
    public void onAdicionarButtonAction() {
        tabela.getSelectionModel().select(-1); // Desseleciona qualquer item na tabela.
        limparCampos(); // Limpa os campos para uma nova entrada.
        desabilitarCampos(false); // Habilita os campos para edição.
        desabilitarBotoes(true, true, true, false, false); // Habilita 'Cancelar' e 'Salvar', desabilita outros.
        tituloField.requestFocus(); // Coloca o foco no campo Título para facilitar a entrada.
    }

    /**
     * Ação disparada pelo botão 'Atualizar'.
     * Habilita os campos de texto para edição do livro atualmente selecionado na tabela.
     */
    @FXML
    public void onAtualizarButtonAction() {
        view.Livro livroSelecionado = tabela.getSelectionModel().getSelectedItem();
        if (livroSelecionado != null) {
            desabilitarCampos(false); // Habilita os campos para que o usuário possa editá-los.
            desabilitarBotoes(true, true, true, false, false); // Habilita 'Cancelar' e 'Salvar' para a edição.
            tituloField.requestFocus(); // Coloca o foco no campo de título.
        } else {
            // Se nenhum livro estiver selecionado, exibe um alerta.
            new Alert(AlertType.WARNING, "Por favor, selecione um livro na tabela para atualizar.").show();
        }
    }

    /**
     * Ação disparada pelo botão 'Deletar'.
     * Remove o livro selecionado da tabela e do banco de dados, após uma confirmação do usuário.
     */
    @FXML
    public void onDeletarButtonAction() {
        view.Livro livroSelecionado = tabela.getSelectionModel().getSelectedItem();
        if (livroSelecionado != null) {
            // **Melhoria:** Adiciona uma caixa de diálogo de confirmação antes de realizar a exclusão.
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmar Exclusão");
            confirmAlert.setHeaderText("Excluir Livro");
            confirmAlert.setContentText("Tem certeza que deseja excluir o livro '" + livroSelecionado.getTitulo() + "' (ID: " + livroSelecionado.getId() + ")?");

            // Espera pela resposta do usuário.
            Optional<ButtonType> result = confirmAlert.showAndWait();
            // Se o usuário clicou em OK, procede com a exclusão.
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Cria um objeto model.Livro com apenas o ID para a exclusão.
                    // O método delete do repositório geralmente só precisa do ID.
                    model.Livro livroParaDeletar = new model.Livro();
                    livroParaDeletar.setId(livroSelecionado.getId());

                    // **Operação de Exclusão (DELETE)**
                    livroRepo.delete(livroParaDeletar); // Chama o método de exclusão no repositório.

                    tabela.getItems().remove(livroSelecionado); // Remove o item da tabela da interface.
                    limparCampos(); // Limpa os campos após a exclusão.
                    desabilitarCampos(true); // Desabilita os campos.
                    desabilitarBotoes(false, true, true, true, true); // Retorna ao estado inicial dos botões.
                    new Alert(AlertType.INFORMATION, "Livro excluído com sucesso!").show();
                } catch (Exception e) {
                    new Alert(AlertType.ERROR, "Erro ao excluir o livro: " + e.getMessage()).show();
                    e.printStackTrace(); // Para depuração, caso ocorra um erro no banco.
                }
            }
        } else {
            // Se nenhum livro estiver selecionado, exibe um alerta.
            new Alert(AlertType.WARNING, "Por favor, selecione um livro na tabela para deletar.").show();
        }
    }

    /**
     * Manipula a seleção de itens na tabela. Quando um livro é selecionado,
     * seus dados são exibidos nos campos de texto e os botões de 'Atualizar' e 'Deletar' são habilitados.
     * @param newSelection O objeto view.Livro que foi recém-selecionado na tabela.
     */
    private void handleLivroSelected(view.Livro newSelection) {
        if (newSelection != null) {
            // Preenche os campos de texto com os dados do livro selecionado.
            idField.setText(Integer.toString(newSelection.getId()));
            tituloField.setText(newSelection.getTitulo());
            autorField.setText(newSelection.getAutor());
            isbnField.setText(newSelection.getIsbn());
            anoPublicacaoField.setText(Integer.toString(newSelection.getAnoPublicacao()));
            // Habilita os botões de 'Atualizar', 'Deletar' e 'Cancelar' quando um item é selecionado,
            // e desabilita 'Salvar' e 'Adicionar' (pois não estamos criando ou salvando agora).
            desabilitarBotoes(false, false, false, false, true); // Adicionar e Cancelar ativos, Salvar desativado
            desabilitarCampos(true); // Campos desabilitados para edição inicial.
        } else {
            // Se a seleção for limpa (nenhum item selecionado), limpa os campos
            // e retorna os botões ao estado inicial (apenas 'Adicionar' ativo).
            limparCampos();
            desabilitarBotoes(false, true, true, true, true);
        }
    }

    /**
     * Método de inicialização do controlador. É chamado automaticamente pelo FXMLLoader
     * após o carregamento do arquivo FXML e a injeção dos elementos com @FXML.
     * Configura as colunas da tabela e carrega os dados iniciais do banco de dados.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configura como cada coluna da TableView deve obter seu valor a partir das propriedades do objeto view.Livro.
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        tituloCol.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        autorCol.setCellValueFactory(new PropertyValueFactory<>("autor"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        anoPublicacaoCol.setCellValueFactory(new PropertyValueFactory<>("anoPublicacao"));

        // Carrega todos os livros do banco de dados e os exibe na TableView.
        tabela.setItems(loadAllLivros());

        // Adiciona um listener à propriedade de seleção da tabela.
        // Sempre que a seleção de um item na tabela mudar, o método handleLivroSelected é chamado.
        tabela.getSelectionModel().selectedItemProperty().addListener(
            (observableValue, oldSelection, newSelection) -> {
                handleLivroSelected(newSelection);
            });

        // Define o estado inicial da interface do usuário ao iniciar a aplicação.
        desabilitarCampos(true); // Todos os campos de texto começam desabilitados.
        desabilitarBotoes(false, true, true, true, true); // Apenas o botão 'Adicionar' está ativo no início.
    }

    /**
     * Converte um objeto Livro do pacote 'model' para um objeto Livro do pacote 'view'.
     * Isso é necessário porque a TableView do JavaFX interage com o modelo de dados definido em 'view.Livro'.
     * @param livro O objeto Livro do modelo de dados.
     * @return Um novo objeto view.Livro com os mesmos dados.
     */
    private view.Livro modelToView(model.Livro livro) {
        return new view.Livro(
            livro.getId(),
            livro.getTitulo(),
            livro.getAutor(),
            livro.getIsbn(),
            livro.getAnoPublicacao()
        );
    }

    /**
     * Carrega todos os livros do repositório (banco de dados) e os converte em
     * uma ObservableList de objetos view.Livro, que é o formato exigido pela TableView.
     * @return Uma ObservableList contendo todos os livros para exibição na tabela.
     */
    private ObservableList<view.Livro> loadAllLivros() {
        ObservableList<view.Livro> lista = FXCollections.observableArrayList();
        List<model.Livro> listaFromDatabase = livroRepo.loadAll(); // Obtém a lista de livros do banco de dados.
        for (model.Livro livro : listaFromDatabase) {
            lista.add(modelToView(livro)); // Adiciona cada livro convertido para a lista da view.
        }
        return lista;
    }
}