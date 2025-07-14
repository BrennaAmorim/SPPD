package model;

import java.util.Date;
import java.text.SimpleDateFormat;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.DataType;

@DatabaseTable(tableName="livro")
public class Livro {
    
    @DatabaseField(generatedId = true)
    private int id;
        
    @DatabaseField(dataType=DataType.STRING)    
    private String titulo;
    
    @DatabaseField(dataType=DataType.STRING)        
    private String autor;
    
    @DatabaseField(dataType=DataType.STRING)        
    private String isbn;
    
    @DatabaseField(dataType=DataType.INTEGER)        
    private int anoPublicacao;
    

//Start GetterSetterExtension Source Code

    /**GET Method Propertie id*/
    public int getId(){
        return this.id;
    }//end method getId

    /**SET Method Propertie id*/
    public void setId(int id){
        this.id = id;
    }//end method setId

    /**GET Method Propertie titulo*/
    public String getTitulo(){
        return this.titulo;
    }//end method getTitulo

    /**SET Method Propertie titulo*/
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }//end method setTitulo

    /**GET Method Propertie autor*/
    public String getAutor(){
        return this.autor;
    }//end method getAutor

    /**SET Method Propertie autor*/
    public void setAutor(String autor){
        this.autor = autor;
    }//end method setAutor

    /**GET Method Propertie isbn*/
    public String getIsbn(){
        return this.isbn;
    }//end method getIsbn

    /**SET Method Propertie isbn*/
    public void setIsbn(String isbn){
        this.isbn = isbn;
    }//end method setIsbn

    /**GET Method Propertie anoPublicacao*/
    public int getAnoPublicacao(){
        return this.anoPublicacao;
    }//end method getAnoPublicacao

    /**SET Method Propertie anoPublicacao*/
    public void setAnoPublicacao(int anoPublicacao){
        this.anoPublicacao = anoPublicacao;
    }//end method setAnoPublicacao

//End GetterSetterExtension Source Code


}//End class