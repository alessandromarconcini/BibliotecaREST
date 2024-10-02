package Library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    private static final File file = new File("~/librarydb");

    @Bean
    CommandLineRunner initDatabase(BookRepository bookRepository) {

        // Inizializzazione con alcuni elementi
        return args -> {

            if(!bookRepository.existsBookByTitle("Le cronache di Narnia"))
                bookRepository.save(new Book("Le cronache di Narnia", "C. S. Lewis","Arnoldo Mondadori Editore","Italiano"));
            if(!bookRepository.existsBookByTitle("Mistborn"))
                bookRepository.save(new Book("Mistborn", "Brandon Sanderson","Fanucci Editore","Italiano"));

            bookRepository.findAll().forEach(book -> log.info("Preloaded " + book));
        };
    }

    @Bean
    public DataSource getDataSource() {

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:file:"+file.getPath());
        dataSourceBuilder.username("library");
        dataSourceBuilder.password("lib");


        return dataSourceBuilder.build();
    }
}