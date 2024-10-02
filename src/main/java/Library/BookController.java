package Library;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class BookController {

    // Repository dove sono contenute le entità
    private static BookRepository repository;
    // Assemblatore: Incapsula gli oggetti libro in oggetti più complessi e li collega ad un endpoint specifico
    private static BookModelAssembler assembler;

    public BookController(BookRepository bookRepository,BookModelAssembler assembler){

        this.repository = bookRepository; this.assembler = assembler;
    }

    // Funzione Get di una collazione : Restituisce la lista dei libri della biblioteca
    // CURL --> curl -v localhost:8080/library
    // POSTMAN --> GET localhost:8080/library
    @GetMapping("/library")
    List<Long> getIDS() {

        // Ritorna tutti gli ID della biblioteca
        List<Long> bookIDS = new ArrayList<Long>();

        for(Book b: repository.findAll())
            bookIDS.add(b.getId());

        return bookIDS;

        // Ritorna un' istanza CollectionModel
        //return CollectionModel.of(books, linkTo(methodOn(BookController.class).all()).withSelfRel());
    }

    //Funzione Post: Aggiunge un nuovo elemento al repository
    // curl -X POST localhost:8080/library -H 'Content-type:application/json' -d '{"title" : "Ulisses Moore","author":"Pierdomenico Baccalario","editor":"Piemme","language":"Italiano"}'
    // POSTMAN -->

    @PostMapping("/library")
    public static ResponseEntity<?> postBook(@RequestBody Book newBook) {

        // VERIFICATO: L'id viene creato nel nuovo libro.
        EntityModel<Book> entityModel = assembler.toModel(repository.save(newBook));

        // La ResponseEntity utilizza il template Rest (Risposta in output)
        // Necessita di passaggio dell'URI
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // Funzione Get che ritorna un singolo elemento, dato il suo indice

    @GetMapping("/library/{id}")
    Book getBook(@PathVariable Long id) {

        // Ricerca nel repostory per id
        Book book = repository.findById(id) //
                .orElseThrow(() -> new IllegalArgumentException());

        return book;
    }

    // Ritorna un libro dato il suo titolo
    @GetMapping("/library/titles/{title}")
    public Book getBookByTitle(@PathVariable String title){

        Book book = repository.findByTitle(title);

        if(book.equals(null))
            throw new IllegalArgumentException();

        return book;
    }

    // Ritorna la lista dei libri dato il loro editore
    @GetMapping("/library/editors/{editor}")
    public List<Book> getBooksByEditor(@PathVariable String editor){

        List<Book> books = new ArrayList<>();

        for(Book b: repository.findAll())
            if(b.getEditor().equals(editor))
                books.add(b);


        if(books.equals(null))
            throw new IllegalArgumentException();

        return books;
    }

    // Ritorna una lista di libri dato il loro autore
    @GetMapping("/library/authors/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author){

        List<Book> books = new ArrayList<>();

        for(Book b: repository.findAll())
            if(b.getAuthor().equals(author))
                books.add(b);


        if(books.equals(null))
            throw new IllegalArgumentException();

        return books;
    }

    // Ritorna una lista di libri data la loro lingua
    @GetMapping("/library/languages/{language}")
    public List<Book> getBooksByLanguage(@PathVariable String language){

        List<Book> books = new ArrayList<>();

        for(Book b: repository.findAll())
            if(b.getLanguage().equals(language))
                books.add(b);


        if(books.equals(null))
            throw new IllegalArgumentException();

        return books;
    }



    // Funzione update di un item: rimpiazza un elemento già esistente con le caratteristiche
    // di uno dato in input, ricerca per id

    // $ curl -X PUT localhost:8080/library/3 -H 'Content-type:application/json' -d '{"title" : "TITOLO MODIFICATO","author":"Pierdomenico Baccalario","editor":"Piemme","language":"Italiano"}'

    @PutMapping("/library/{id}")
    ResponseEntity<?> replaceBook(@RequestBody Book newBook, @PathVariable Long id) {

        // ATTENZIONE: @LongVariable Long id è un input necessario poichè preleva l'id della richiesta in entrata e
        // attraverso quella effettua la ricerca per id

        Book updatedBook = repository.findById(id) //
                .map(book -> {
                    book.setTitle(newBook.getTitle());
                    book.setAuthor(newBook.getAuthor());
                    book.setEditor(newBook.getEditor());
                    book.setLanguage(newBook.getLanguage());
                    return repository.save(book);
                }) //
                .orElseGet(() -> {
                    newBook.setId(id);
                    return repository.save(newBook);
                });

        EntityModel<Book> entityModel = assembler.toModel(updatedBook);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    //Funzione Delete: elimina un elemento dato il suo indice identificatore
    //curl -X DELETE localhost:8080/library/3

    @DeleteMapping("/library/{id}")
    ResponseEntity<?> deleteBook(@PathVariable Long id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    public static BookRepository getRepository(){ return repository;}
    public static BookModelAssembler getModelAssembler() {return assembler;}
}
