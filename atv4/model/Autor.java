package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * Classe de Modelo para a entidade Autor.
 * Mapeada para a tabela "autor" no banco de dados usando ORMLite.
 */
@DatabaseTable(tableName="autor")
public class Autor {

    @DatabaseField(generatedId = true) // ID gerado automaticamente
    private int id;

    @DatabaseField(dataType=DataType.STRING, canBeNull = false) // Nome, campo obrigatório
    private String nome;

    @DatabaseField(dataType=DataType.STRING) // Nacionalidade, campo opcional
    private String nacionalidade;

    @ForeignCollectionField(eager = false) // Coleção de Livros associados a este autor (carregamento lazy)
    private ForeignCollection<Livro> livros;

    // Construtor vazio é necessário para o ORMLite
    public Autor() {
    }

    /**
     * Construtor para criar um novo Autor.
     * @param nome O nome completo do autor.
     * @param nacionalidade A nacionalidade do autor.
     */
    public Autor(String nome, String nacionalidade) {
        this.nome = nome;
        this.nacionalidade = nacionalidade;
    }

    // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    /**
     * Retorna a coleção de livros associados a este autor.
     * Observe que esta coleção é carregada de forma "lazy" por padrão,
     * o que significa que os livros só serão buscados do banco quando este método for chamado.
     * @return Uma ForeignCollection de Livros.
     */
    public ForeignCollection<Livro> getLivros() {
        return livros;
    }

    public void setLivros(ForeignCollection<Livro> livros) {
        this.livros = livros;
    }

    /**
     * Sobrescreve o método toString para exibir o nome do autor em ComboBoxes, etc.
     * @return O nome do autor.
     */
    @Override
    public String toString() {
        return nome;
    }
}