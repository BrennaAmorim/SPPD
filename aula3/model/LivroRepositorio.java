package model;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import com.j256.ormlite.table.TableUtils;
import java.util.List;
import java.util.ArrayList;

/**
 * Repositório para a entidade Livro, gerenciando operações CRUD
 * com o banco de dados usando ORMLite.
 */
public class LivroRepositorio
{
    private static Database database;
    private static Dao<Livro, Integer> dao; // Objeto DAO do ORMLite para operações com a entidade Livro
    private List<Livro> loadedLivros; // Lista de livros carregados (opcional, mas pode ser útil para cache)
    private Livro loadedLivro; // Livro individual carregado (opcional)

    /**
     * Construtor do repositório de livros.
     * @param database A instância do objeto Database para obter a conexão.
     */
    public LivroRepositorio(Database database) {
        LivroRepositorio.setDatabase(database); // Define a conexão com o banco de dados.
        loadedLivros = new ArrayList<Livro>(); // Inicializa a lista de livros.
    }

    /**
     * Define a instância do banco de dados e inicializa o objeto DAO do ORMLite.
     * Também cria a tabela 'livro' se ela ainda não existir no banco de dados.
     * @param database A instância do Database.
     */
    public static void setDatabase(Database database) {
        LivroRepositorio.database = database;
        try {
            // Cria o DAO (Data Access Object) para a classe Livro.
            dao = DaoManager.createDao(database.getConnection(), Livro.class);
            // Cria a tabela 'livro' no banco de dados se ela ainda não existir.
            TableUtils.createTableIfNotExists(database.getConnection(), Livro.class);
        }
        catch(SQLException e) {
            System.err.println("Erro ao inicializar o DAO ou criar a tabela: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cria (insere) um novo livro no banco de dados.
     * @param livro O objeto Livro a ser criado.
     * @return O objeto Livro criado, com o ID gerado pelo banco de dados.
     * @throws SQLException Se ocorrer um erro SQL.
     */
    public Livro create(Livro livro) throws SQLException {
        int nrows = 0;
        try {
            nrows = dao.create(livro); // Tenta criar o livro no banco.
            if ( nrows == 0 )
                throw new SQLException("Erro: Objeto Livro não foi salvo no banco de dados.");
            this.loadedLivro = livro; // Atribui o livro recém-criado.
            // Opcional: Adicionar à lista de livros carregados se você estiver mantendo um cache.
            // loadedLivros.add(livro);
        } catch (SQLException e) {
            System.err.println("Erro ao criar livro: " + e.getMessage());
            throw e; // Relança a exceção para ser tratada pelo controlador.
        }
        return livro;
    }

    /**
     * Atualiza um livro existente no banco de dados.
     * @param livro O objeto Livro com os dados atualizados (o ID deve estar preenchido).
     * @throws SQLException Se ocorrer um erro SQL.
     */
    public void update(Livro livro) throws SQLException {
        try {
            int nrows = dao.update(livro); // Tenta atualizar o livro no banco.
            if (nrows == 0) {
                throw new SQLException("Erro: Nenhum livro foi atualizado. ID não encontrado ou dados idênticos.");
            }
            this.loadedLivro = livro; // Atualiza a referência do último livro carregado/operado.
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar livro: " + e.getMessage());
            throw e; // Relança a exceção.
        }
    }

    /**
     * Exclui um livro do banco de dados com base no seu ID.
     * @param livro O objeto Livro contendo o ID do livro a ser excluído.
     * @throws SQLException Se ocorrer um erro SQL.
     */
    public void delete(Livro livro) throws SQLException {
        try {
            // O método delete do DAO ORMLite pode receber o objeto completo,
            // mas internamente ele usa o ID para a exclusão se o ID for definido.
            int nrows = dao.delete(livro);
            if (nrows == 0) {
                throw new SQLException("Erro: Nenhum livro foi excluído. ID não encontrado.");
            }
            // Opcional: Remover da lista de livros carregados se você estiver mantendo um cache.
            // loadedLivros.removeIf(l -> l.getId() == livro.getId());
            this.loadedLivro = null; // Limpa o último livro carregado, pois foi excluído.
        } catch (SQLException e) {
            System.err.println("Erro ao deletar livro: " + e.getMessage());
            throw e; // Relança a exceção.
        }
    }

    /**
     * Carrega um livro do banco de dados pelo seu ID.
     * @param id O ID do livro a ser carregado.
     * @return O objeto Livro correspondente ao ID, ou null se não for encontrado.
     * @throws SQLException Se ocorrer um erro SQL.
     */
    public Livro loadFromId(int id) throws SQLException {
        try {
            this.loadedLivro = dao.queryForId(id); // Consulta o livro pelo ID.
            // Opcional: Adicionar à lista de livros carregados se você estiver mantendo um cache.
            // if (this.loadedLivro != null && !loadedLivros.contains(this.loadedLivro))
            //     this.loadedLivros.add(this.loadedLivro);
        } catch (SQLException e) {
            System.err.println("Erro ao carregar livro por ID: " + e.getMessage());
            throw e; // Relança a exceção.
        }
        return this.loadedLivro;
    }

    /**
     * Carrega todos os livros presentes no banco de dados.
     * @return Uma lista de todos os objetos Livro encontrados no banco de dados.
     * @throws SQLException Se ocorrer um erro SQL.
     */
    public List<Livro> loadAll() throws SQLException {
        try {
            this.loadedLivros = dao.queryForAll(); // Consulta todos os livros.
            if (!this.loadedLivros.isEmpty()) {
                this.loadedLivro = this.loadedLivros.get(0); // Define o primeiro como o último carregado.
            } else {
                this.loadedLivro = null;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar todos os livros: " + e.getMessage());
            throw e; // Relança a exceção.
        }
        return this.loadedLivros;
    }
}