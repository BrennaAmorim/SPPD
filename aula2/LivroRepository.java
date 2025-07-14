import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class LivroRepository {
    private Dao<Livro, Integer> dao;
    private LivroJsonSerializer serializer = new LivroJsonSerializer();

    public LivroRepository(Database db) {
        try {
            dao = DaoManager.createDao(db.getConnection(), Livro.class);
            TableUtils.createTableIfNotExists(db.getConnection(), Livro.class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Livro create(Livro livro) {
        try {
            dao.create(livro);
        } catch (Exception e) {
            System.out.println(e);
        }
        return livro;
    }

    public List<Livro> loadAll() {
        try {
            return dao.queryForAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ArrayList<Livro>();
    }

    public String dumpData(String formato) {
        if (!formato.equalsIgnoreCase("json")) return null;
        List<Livro> livros = loadAll();
        return serializer.toJsonList(livros);
    }

    public boolean dumpFile(String formato, File arquivo) {
        if (!formato.equalsIgnoreCase("json")) return false;
        try (FileWriter fw = new FileWriter(arquivo)) {
            fw.write(dumpData("json"));
            return true;
        } catch (IOException e) {
            System.out.println(e);
        }
        return false;
    }

    public Livro createFromJSON(String json) {
        Livro livro = serializer.fromJson(json);
        return create(livro);
    }

    public int importData(String formato, String data) {
        if (!formato.equalsIgnoreCase("json")) return 0;
        List<Livro> livros = serializer.fromJsonList(data);
        int count = 0;
        for (Livro l : livros) {
            create(l);
            count++;
        }
        return count;
    }

    public int importFile(String formato, File arquivo) {
        if (!formato.equalsIgnoreCase("json")) return 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            StringBuilder sb = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                sb.append(linha).append("\n");
            }
            return importData("json", sb.toString());
        } catch (IOException e) {
            System.out.println(e);
        }
        return 0;
    }
}
