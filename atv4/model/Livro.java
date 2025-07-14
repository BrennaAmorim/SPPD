package model;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.DataType;

/**
 * Classe de Modelo para a entidade Livro.
 * Mapeada para a tabela "livro" no banco de dados usando ORMLite.
 */
@DatabaseTable(tableName="livro")
public class Livro {

    @DatabaseField(generatedId = true) // ID gerado automaticamente
    private int id;

    @DatabaseField(dataType=DataType.STRING) // Título do livro
    private String titulo;

    // Campo de chave estrangeira para o Autor.
    // foreign = true: indica que é uma chave estrangeira.
    // foreignAutoRefresh = true: se um Livro for carregado, o objeto Autor associado será automaticamente carregado também.
    // columnName = "autor_id": define o nome da coluna no banco de dados para a chave estrangeira.
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "autor_id")
    private Autor autor; // Relação N:1 (Muitos Livros para Um Autor)

    @DatabaseField(dataType=DataType.STRING) // ISBN do livro
    private String isbn;

    @DatabaseField(dataType=DataType.INTEGER) // Ano de publicação do livro
    private int anoPublicacao;

    // Construtor vazio é necessário para o ORMLite.
    public Livro() {
    }

    // --- Getters e Setters ---

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitulo(){
        return this.titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public Autor getAutor(){
        return this.autor;
    }

    public void setAutor(Autor autor){
        this.autor = autor;
    }

    public String getIsbn(){
        return this.isbn;
    }

    public void setIsbn(String isbn){
        this.isbn = isbn;
    }

    public int getAnoPublicacao(){
        return this.anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao){
        this.anoPublicacao = anoPublicacao;
    }
}