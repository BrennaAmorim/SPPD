import com.google.gson.*;
import java.util.*;

public class LivroJsonSerializer {
    private Gson gson;

    public LivroJsonSerializer() {
        gson = new GsonBuilder()
                   .setDateFormat("yyyy-MM-dd")
                   .setPrettyPrinting()
                   .create();
    }

    public String toJson(Livro livro) {
        return gson.toJson(livro);
    }

    public Livro fromJson(String json) {
        return gson.fromJson(json, Livro.class);
    }

    public String toJsonList(List<Livro> livros) {
        return gson.toJson(livros);
    }

    public List<Livro> fromJsonList(String json) {
        Livro[] array = gson.fromJson(json, Livro[].class);
        return Arrays.asList(array);
    }
}
