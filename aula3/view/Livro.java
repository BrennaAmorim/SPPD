package view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Livro {
    
    private SimpleIntegerProperty id;
    private SimpleStringProperty titulo;
    private SimpleStringProperty autor;
    private SimpleStringProperty isbn;
    private SimpleIntegerProperty anoPublicacao;
    
    public Livro(int id, String titulo, String autor, String isbn, int anoPublicacao) {
        this.id = new SimpleIntegerProperty(id);
        this.titulo = new SimpleStringProperty(titulo);
        this.autor = new SimpleStringProperty(autor);
        this.isbn = new SimpleStringProperty(isbn);
        this.anoPublicacao = new SimpleIntegerProperty(anoPublicacao);        
    }
    
//Start GetterSetterExtension Source Code

    /**GET Method Propertie id*/
    public int getId(){
        return this.id.get();
    }//end method getId

    /**SET Method Propertie id*/
    public void setId(int id){
        this.id.set(id);
    }//end method setId

    /**GET Method Propertie titulo*/
    public String getTitulo(){
        return this.titulo.get();
    }//end method getTitulo

    /**SET Method Propertie titulo*/
    public void setTitulo(String titulo){
        this.titulo.set(titulo);
    }//end method setTitulo

    /**GET Method Propertie autor*/
    public String getAutor(){
        return this.autor.get();
    }//end method getAutor

    /**SET Method Propertie autor*/
    public void setAutor(String autor){
        this.autor.set(autor);
    }//end method setAutor

    /**GET Method Propertie isbn*/
    public String getIsbn(){
        return this.isbn.get();
    }//end method getIsbn

    /**SET Method Propertie isbn*/
    public void setIsbn(String isbn){
        this.isbn.set(isbn);
    }//end method setIsbn

    /**GET Method Propertie anoPublicacao*/
    public int getAnoPublicacao(){
        return this.anoPublicacao.get();
    }//end method getAnoPublicacao

    /**SET Method Propertie anoPublicacao*/
    public void setAnoPublicacao(int anoPublicacao){
        this.anoPublicacao.set(anoPublicacao);
    }//end method setAnoPublicacao

//End GetterSetterExtension Source Code


}//End class