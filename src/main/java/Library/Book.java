package Library;

import javax.persistence.*;
import java.util.Objects;

@Entity  // Book è un'entità
@Table(name = "Books")
public class Book {

    private @Id @GeneratedValue Long id; //ID univoco nel database
    private String title;
    private String author;
    private String editor;
    private String language;

    public Book(String title, String author, String editor, String language){

        if(title.equals(null) || author.equals(null) || editor.equals(null) || language.equals(null))
            throw new IllegalArgumentException();

        this.title = title;
        this.author = author;
        this.editor = editor;
        this.language = language;
    }

    public Book() {
    }


    public Long getId(){ return id; }
    public String getTitle(){ return title;}
    public String getAuthor(){ return author;}
    public String getEditor(){ return editor;}
    public String getLanguage(){ return language;}

    public void setId(Long id){ this.id = id; }
    public void setTitle(String title){ this.title = title; }
    public void setAuthor(String author){ this.author = author; }
    public void setEditor(String editor){ this.editor = editor; }
    public void setLanguage(String language){ this.language= language; }

    public boolean equals(Object other){

        return other instanceof Book &&
                id.equals(((Book) other).id) &&
                title.equals(((Book) other).title) &&
                author.equals(((Book) other).author) &&
                editor.equals(((Book) other).editor) &&
                language.equals(((Book) other).language);
    }


    public int hashCode() {
        return Objects.hash(id,title,author,editor,language);
    }

    public String toString() {
        return "Book" + id + ":\n" +
                "Title: " + title + "\n" +
                "Author:" + author + "\n" +
                "Editor:" + editor + "\n" +
                "Language:" + language + "\n";
    }
}
