import java.util.Date;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.DataType;

@DatabaseTable(tableName = "livro")
public class Livro {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String titulo;

    @DatabaseField
    private String autor;

    @DatabaseField(dataType = DataType.DATE)
    private Date publicadoEm;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public Date getPublicadoEm() { return publicadoEm; }
    public void setPublicadoEm(Date publicadoEm) { this.publicadoEm = publicadoEm; }
}
