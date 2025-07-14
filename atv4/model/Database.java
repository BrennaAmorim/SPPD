package model;

import java.sql.SQLException;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados SQLite.
 * Utiliza ORMLite para abstrair a conexão JDBC.
 */
public class Database
{
    private String databaseName = null;
    private JdbcConnectionSource connection = null;

    /**
     * Construtor da classe Database.
     * @param databaseName O nome do arquivo do banco de dados (ex: "app.sqlite").
     * O arquivo será criado na pasta raiz do projeto onde a JVM é executada.
     */
    public Database(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Obtém a conexão com o banco de dados. Se a conexão ainda não existir, ela é criada.
     * @return A fonte de conexão JDBC do ORMLite.
     * @throws SQLException Se ocorrer um erro ao estabelecer a conexão.
     */
    public JdbcConnectionSource getConnection() throws SQLException {
        if ( databaseName == null ) {
            throw new SQLException("Nome do banco de dados não pode ser nulo.");
        }
        if ( connection == null ) {
            try {
                // Cria uma nova conexão SQLite. O arquivo será criado se não existir.
                connection = new JdbcConnectionSource("jdbc:sqlite:" + databaseName);
                System.out.println("Conexão com o banco de dados '" + databaseName + "' estabelecida com sucesso.");
            } catch ( Exception e ) {
                System.err.println( "Erro ao conectar ao banco de dados: " + e.getClass().getName() + ": " + e.getMessage() );
                e.printStackTrace();
                // Em um aplicativo GUI real, você pode mostrar um Alert aqui em vez de System.exit(1).
                System.exit(1); // Sai do programa em caso de erro fatal na conexão.
            }
        }
        return connection;
    }

    /**
     * Fecha a conexão com o banco de dados, se ela estiver aberta.
     */
    public void close() {
        if ( connection != null ) {
            try {
                connection.close(); // Fecha a conexão.
                this.connection = null; // Zera a referência para forçar uma nova conexão se for solicitada.
                System.out.println("Conexão com o banco de dados '" + databaseName + "' fechada.");
            } catch (java.lang.Exception e) {
                System.err.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}