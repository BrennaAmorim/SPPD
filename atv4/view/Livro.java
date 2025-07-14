package view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Classe modelo para a exibição de Livros na interface gráfica (TableView).
 * Utiliza JavaFX Properties para permitir que a TableView observe mudanças e
 * se atualize automaticamente.
 * É uma classe separada do model.Livro para desacoplar a camada de view da camada de modelo.
 */
public class Livro {

    private SimpleIntegerProperty id;
    private SimpleStringProperty titulo;
    // Usa view.Autor para a propriedade, pois esta é a representação do Autor na camada de view.
    private ObjectProperty<view.Autor> autor;
    private SimpleStringProperty isbn;
    private SimpleIntegerProperty anoPublicacao;

    /**
     * Construtor para criar um objeto Livro que será exibido na TableView.
     * @param id O identificador único do livro.
     * @param titulo O título do livro.
     * @param autor O objeto Autor associado ao livro (do pacote view).
     * @param isbn O ISBN do livro.
     * @param anoPublicacao O ano de publicação do livro.
     */
    public Livro(int id, String titulo, view.Autor autor, String isbn, int anoPublicacao) {
        this.id = new SimpleIntegerProperty(id);
        this.titulo = new SimpleStringProperty(titulo);
        this.autor = new SimpleObjectProperty<>(autor);
        this.isbn = new SimpleStringProperty(isbn);
        this.anoPublicacao = new SimpleIntegerProperty(anoPublicacao);
    }

    // --- Métodos Getters e Setters para as propriedades JavaFX ---

    public int getId(){
        return this.id.get();
    }

    public void setId(int id){
        this.id.set(id);
    }

    public String getTitulo(){
        return this.titulo.get();
    }

    public void setTitulo(String titulo){
        this.titulo.set(titulo);
    }

    public view.Autor getAutor(){ // Retorna view.Autor
        return this.autor.get();
    }

    public void setAutor(view.Autor autor){ // Recebe view.Autor
        this.autor.set(autor);
    }

    /**
     * Retorna a propriedade do objeto Autor.
     * Este método é **ESSENCIAL** para o PropertyValueFactory funcionar com a coluna de Autor.
     * Permite que a TableColumn exiba o valor do Autor (que será seu toString()).
     * @return A ObjectProperty<view.Autor> do autor.
     */
    public ObjectProperty<view.Autor> autorProperty() {
        return autor;
    }


    public String getIsbn(){
        return this.isbn.get();
    }

    public void setIsbn(String isbn){
        this.isbn.set(isbn);
    }

    public int getAnoPublicacao(){
        return this.anoPublicacao.get();
    }

    public void setAnoPublicacao(int anoPublicacao){
        this.anoPublicacao.set(anoPublicacao);
    }

    // Métodos Property para outras colunas
    public SimpleIntegerProperty idProperty() { return id; }
    public SimpleStringProperty tituloProperty() { return titulo; }
    public SimpleStringProperty isbnProperty() { return isbn; }
    public SimpleIntegerProperty anoPublicacaoProperty() { return anoPublicacao; }
}