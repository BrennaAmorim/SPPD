package model;

import java.sql.SQLException;

/**
 * Repositório específico para a entidade Autor, estendendo a classe abstrata Repositorio.
 * Permite realizar operações CRUD em objetos Autor.
 */
public class AutorRepositorio extends Repositorio<Autor, Integer> {

    /**
     * Construtor do AutorRepositorio.
     * @param database A instância do banco de dados.
     * @throws SQLException Se ocorrer um erro ao inicializar o repositório.
     */
    public AutorRepositorio(Database database) throws SQLException {
        // Chama o construtor da classe pai (Repositorio) com a classe da entidade Autor.
        // Isso configura o DAO e garante que a tabela 'autor' seja criada.
        super(database, Autor.class);
    }
}