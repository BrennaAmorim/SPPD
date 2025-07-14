import java.io.File;

public class Main {
    public static void main(String[] args) {
        Database db = new Database("livros.db");
        LivroRepository repo = new LivroRepository(db);

        Livro l1 = new Livro();
        l1.setTitulo("Dom Casmurro");
        l1.setAutor("Machado de Assis");
        l1.setPublicadoEm(new java.util.Date());

        repo.create(l1);

        String json = repo.dumpData("json");
        System.out.println("JSON gerado:\n" + json);

        File arquivo = new File("livros.json");
        repo.dumpFile("json", arquivo);

        db.close();
    }
}
