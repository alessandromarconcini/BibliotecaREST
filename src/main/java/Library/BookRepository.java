package Library;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// Definisce un repository di tipo jpa (Utilizzato come base di dati)
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    public Book findByTitle(String title);
    public boolean existsBookByTitle(String title);
}