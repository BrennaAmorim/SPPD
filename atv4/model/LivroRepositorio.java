package model;

import java.sql.SQLException;

/**
 * Repositório específico para a entidade Livro, estendendo a classe abstrata Repositorio.
 * Herda os métodos CRUD genéricos e pode ter métodos específicos se houver necessidade
 * de consultas mais complexas ou lógicas de negócio específicas para Livros.
 */
public class LivroRepositorio extends Repositorio<Livro, Integer> {

    /**
     * Construtor do LivroRepositorio.
     * @param database A instância do banco de dados.
     * @throws SQLException Se ocorrer um erro ao inicializar o repositório.
     */
    public LivroRepositorio(Database database) throws SQLException {
        // Chama o construtor da classe pai (Repositorio) com a classe da entidade Livro.
        // Isso configura o DAO e garante que a tabela 'livro' seja criada.
        super(database, Livro.class);
    }
    // Métodos específicos para Livro podem ser adicionados aqui se houver necessidade
    // de consultas mais complexas ou lógicas de negócio específicas para Livros.
}