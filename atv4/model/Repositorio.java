package model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe abstrata Repositorio.
 * Define a interface para operações CRUD genéricas para qualquer entidade de modelo.
 * Fornece métodos comuns para persistência usando ORMLite.
 *
 * @param <T> O tipo da entidade (ex: Livro, Autor).
 * @param <ID> O tipo da chave primária da entidade (ex: Integer).
 */
public abstract class Repositorio<T, ID> {

    protected Dao<T, ID> dao; // Objeto DAO do ORMLite para a entidade específica
    protected Database database; // Referência ao objeto de conexão com o banco de dados
    protected Class<T> entityClass; // Classe da entidade que este repositório gerencia

    /**
     * Construtor do Repositorio.
     * @param database A instância do banco de dados.
     * @param entityClass A classe da entidade que este repositório irá manipular.
     * @throws SQLException Se houver um erro de SQL ao inicializar o DAO ou criar a tabela.
     */
    public Repositorio(Database database, Class<T> entityClass) throws SQLException {
        this.database = database;
        this.entityClass = entityClass;
        // Inicializa o DAO para a entidade específica
        this.dao = DaoManager.createDao(database.getConnection(), entityClass);
        // Garante que a tabela da entidade exista no banco de dados.
        // Esta é uma operação segura que só cria a tabela se ela não existir.
        TableUtils.createTableIfNotExists(database.getConnection(), entityClass);
    }

    /**
     * Cria (insere) uma nova entidade no banco de dados.
     * @param entity A entidade a ser criada.
     * @return A entidade criada, possivelmente com IDs gerados pelo banco.
     * @throws SQLException Se ocorrer um erro SQL.
     */
    public T create(T entity) throws SQLException {
        int rowsAffected = dao.create(entity);
        if (rowsAffected == 0) {
            throw new SQLException("Erro: A entidade " + entityClass.getSimpleName() + " não foi salva.");
        }
        return entity;
    }

    /**
     * Busca e retorna uma entidade pelo seu ID.
     * @param id O ID da entidade a ser buscada.
     * @return A entidade encontrada ou null se não existir.
     * @throws SQLException Se ocorrer um erro SQL.
     */
    public T read(ID id) throws SQLException {
        return dao.queryForId(id);
    }

    /**
     * Retorna todas as entidades do banco de dados.
     * @return Uma lista de todas as entidades.
     * @throws SQLException Se ocorrer um erro SQL.
     */
    public List<T> readAll() throws SQLException {
        return dao.queryForAll();
    }

    /**
     * Atualiza uma entidade existente no banco de dados.
     * @param entity A entidade com os dados atualizados.
     * @return O número de linhas afetadas (1 se sucesso, 0 se falha).
     * @throws SQLException Se ocorrer um erro SQL.
     */
    public int update(T entity) throws SQLException {
        return dao.update(entity);
    }

    /**
     * Deleta uma entidade do banco de dados.
     * @param entity A entidade a ser deletada.
     * @return O número de linhas afetadas (1 se sucesso, 0 se falha).
     * @throws SQLException Se ocorrer um erro SQL.
     */
    public int delete(T entity) throws SQLException {
        return dao.delete(entity);
    }

    /**
     * Retorna o DAO associado a este repositório. Pode ser útil para
     * operações mais avançadas não cobertas pelos métodos CRUD básicos.
     * @return O objeto DAO do ORMLite.
     */
    public Dao<T, ID> getDao() {
        return dao;
    }
}