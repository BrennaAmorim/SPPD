package view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Classe modelo para a exibição de Autores na interface gráfica (TableView e ComboBox).
 * Utiliza JavaFX Properties para permitir que a TableView observe mudanças e
 * se atualize automaticamente.
 * É uma classe separada do model.Autor para desacoplar a camada de view da camada de modelo.
 */
public class Autor {

    private SimpleIntegerProperty id;
    private SimpleStringProperty nome;
    private SimpleStringProperty nacionalidade;

    /**
     * Construtor para criar um objeto Autor para a view.
     * @param id O identificador único do autor.
     * @param nome O nome do autor.
     * @param nacionalidade A nacionalidade do autor.
     */
    public Autor(int id, String nome, String nacionalidade) {
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.nacionalidade = new SimpleStringProperty(nacionalidade);
    }

    // --- Métodos Getters e Setters para as propriedades JavaFX ---

    public int getId(){
        return this.id.get();
    }

    public void setId(int id){
        this.id.set(id);
    }

    public String getNome(){
        return this.nome.get();
    }

    public void setNome(String nome){
        this.nome.set(nome);
    }

    public String getNacionalidade(){
        return this.nacionalidade.get();
    }

    public void setNacionalidade(String nacionalidade){
        this.nacionalidade.set(nacionalidade);
    }

    // Métodos Property para uso com PropertyValueFactory nas TableColumns
    public SimpleIntegerProperty idProperty() { return id; }
    public SimpleStringProperty nomeProperty() { return nome; }
    public SimpleStringProperty nacionalidadeProperty() { return nacionalidade; }

    /**
     * Sobrescreve o método toString para exibir o nome do autor em ComboBoxes.
     * @return O nome do autor.
     */
    @Override
    public String toString() {
        return getNome();
    }
}