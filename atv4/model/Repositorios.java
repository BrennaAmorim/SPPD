package model;

import java.sql.SQLException;

/**
 * Classe utilitária para instanciar e fornecer acesso aos repositórios de cada entidade.
 * Garante que apenas uma instância de cada repositório seja criada (Singleton pattern para os repositórios).
 * Centraliza a inicialização do banco de dados e dos repositórios.
 */
public class Repositorios {

    private static Database database;
    private static LivroRepositorio livroRepositorio;
    private static AutorRepositorio autorRepositorio;

    /**
     * Inicializa a conexão com o banco de dados e todos os repositórios.
     * Este método deve ser chamado uma única vez no início da aplicação (ex: em AppView.start()).
     * @param dbName O nome do arquivo do banco de dados (ex: "app.sqlite").
     * @throws SQLException Se houver um erro de SQL durante a inicialização.
     */
    public static void initialize(String dbName) throws SQLException {
        if (database == null) {
            database = new Database(dbName);
        }
        if (livroRepositorio == null) {
            livroRepositorio = new LivroRepositorio(database);
        }
        if (autorRepositorio == null) {
            autorRepositorio = new AutorRepositorio(database);
        }
    }

    /**
     * Obtém a instância do LivroRepositorio.
     * @return A instância do LivroRepositorio.
     * @throws IllegalStateException Se o método initialize não foi chamado primeiro.
     */
    public static LivroRepositorio getLivroRepositorio() {
        if (livroRepositorio == null) {
            throw new IllegalStateException("Os repositórios não foram inicializados. Chame Repositorios.initialize() primeiro.");
        }
        return livroRepositorio;
    }

    /**
     * Obtém a instância do AutorRepositorio.
     * @return A instância do AutorRepositorio.
     * @throws IllegalStateException Se o método initialize não foi chamado primeiro.
     */
    public static AutorRepositorio getAutorRepositorio() {
        if (autorRepositorio == null) {
            throw new IllegalStateException("Os repositórios não foram inicializados. Chame Repositorios.initialize() primeiro.");
        }
        return autorRepositorio;
    }

    /**
     * Fecha a conexão com o banco de dados.
     * Deve ser chamado ao encerrar a aplicação para liberar recursos.
     */
    public static void closeDatabase() {
        if (database != null) {
            database.close();
            database = null; // Limpa a referência para permitir nova inicialização se necessário
            livroRepositorio = null; // Zera as referências dos repositórios também
            autorRepositorio = null;
        }
    }
}