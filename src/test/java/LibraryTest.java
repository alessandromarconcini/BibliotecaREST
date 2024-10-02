
import Library.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.BeforeMethod;

import static org.junit.Assert.*;
import static io.restassured.RestAssured.*;


public class LibraryTest {

    // given() --> Crea la richiesta da sottoporre alla REST application
    // when() --> Considera una situazione specifica
    // get()/post()/put()/delete() --> CRUD
    // then() --> ritorna una risposta verificabile
    // statusCode(thisCode) --> Valida che lo statusCode abbia valore thisCode
    // body() --> Considera una parte del corpo di risposta per applicare i passaggi successivi
    // Matchers() --> utilizzato assieme ad altri metodi, consente di attuare asserzioni
    // contentType("application/json") --> Dichiara il formato del contenuto del messaggio http
    // pathParam("path",value) --> mette il valore in {path} ad esempio /library/{path}

   // GET

    @Test
    @Order(1)
    public void getLibraryTest(){

        given().when().get("/library").then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    public void getFirstBookTest(){

        String title = "Le cronache di Narnia";
        String author = "C. S. Lewis";
        String editor = "Arnoldo Mondadori Editore";
        String language = "Italiano";


        given().when().get("library/{id}",1).then().statusCode(200)
                .body("id",Matchers.greaterThan(0))
                .body("title",Matchers.is(title))
                .body("author",Matchers.is(author))
                .body("editor",Matchers.is(editor))
                .body("language",Matchers.is(language));
    }

    @Test
    @Order(3)
    public void getSecondBookTest(){

        String title = "Mistborn";
        String author = "Brandon Sanderson";
        String editor = "Fanucci Editore";
        String language = "Italiano";

        given().when().get("library/{id}",2).then().statusCode(200)
                .body("id",Matchers.greaterThan(0))
                .body("title",Matchers.is(title))
                .body("author",Matchers.is(author))
                .body("editor",Matchers.is(editor))
                .body("language",Matchers.is(language));
    }

    @Test
    @Order(4)
    public void getBookByTitle(){

        given().when().get("library/titles/{title}","Mistborn").then().statusCode(200)
                .body("title",Matchers.is("Mistborn"));
    }

    @Test
    @Order(5)
    public void getBooksByAuthor(){ given().when().get("library/authors/{author}","Brandon Sanderson").then().statusCode(200); }

    @Test
    @Order(6)
    public void getBooksByEditor(){ given().when().get("library/editors/{editor}","Fanucci Editore").then().statusCode(200); }

    @Test
    @Order(7)
    public void getBooksByLanguage(){ given().when().get("library/languages/{language}","Italiano").then().statusCode(200); }

    // POST

    @Test
    public void newBookTest(){


        Gson gson = new GsonBuilder().create();
        Book newBook = new Book("titleTest","authorTest","editorTest","languageTest");
        String jsonBody = gson.toJson(newBook);

        given().contentType("application/json")
                .body(jsonBody)
                .when().post("/library")
                .then().statusCode(201)
                .body("title",Matchers.is(newBook.getTitle()))
                .body("author",Matchers.is(newBook.getAuthor()))
                .body("editor",Matchers.is(newBook.getEditor()))
                .body("language",Matchers.is(newBook.getLanguage()));
    }

    // PUT

    @Test
    @Order(9)
    public void updateBook1Test(){

        /* final String jsonBody =
                "{" +
                        "\"id\":1," +
                        "\"title\":\"test1\"," +
                        "\"author\":\"test1\"," +
                        "\"editor\":\"test1\"," +
                        "\"language\":\"test1\""+
                        "}";

         */

        Gson gson = new GsonBuilder().create();
        Book newBook = new Book("titleTest","authorTest","editorTest","languageTest");
        String jsonBody = gson.toJson(newBook);

        given()
                .pathParam("id",1)
                .contentType("application/json")
                .body(jsonBody)
                .when().put("library/{id}")
                .then().statusCode(201)
                //.body("id",Matchers.is(1)) TODO
                .body("title",Matchers.is(newBook.getTitle()))
                .body("author",Matchers.is(newBook.getAuthor()))
                .body("editor",Matchers.is(newBook.getEditor()))
                .body("language",Matchers.is(newBook.getLanguage()));
    }

    // DELETE

    @Test
    @Order(10)
    public static void delBookTest(){

        given().contentType("application/json")
                .when().delete("library/3").then().statusCode(204);
    }
}