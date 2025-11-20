package hrytsenko;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovieTest {

  @Test
  @Order(1)
  public void createMovie() {
    given()
        .body("""
            {
              "imdb": "0084787",
              "title": "The Thing",
              "year": 1982,
              "casts": []
            }
            """)
        .contentType(MediaType.APPLICATION_JSON)
        .when().post("/movies")
        .then().statusCode(204);

    var actualMovies = given()
        .when().get("/movies")
        .then().statusCode(200)
        .extract().asString();

    assertThatJson(actualMovies)
        .isEqualTo("""
            [
              {
                "imdb": "0084787",
                "title": "The Thing",
                "year": 1982,
                "casts": []
              }
            ]
            """);

    var actualMovie = given()
        .when().get("/movies/0084787")
        .then().statusCode(200)
        .extract().asString();

    assertThatJson(actualMovie)
        .isEqualTo("""
            {
              "imdb": "0084787",
              "title": "The Thing",
              "year": 1982,
              "casts": []
            }
            """);
  }

  @Test
  @Order(2)
  public void updateMovie() {
    given()
        .body("""
            {
              "imdb": "0084787",
              "title": "The Thing",
              "year": 1982,
              "casts": [
                "Kurt Russell",
                "Wilford Brimley"
              ]
            }
            """)
        .contentType(MediaType.APPLICATION_JSON)
        .when().put("/movies/0084787")
        .then().statusCode(204);

    var actualMovies = given()
        .when().get("/movies")
        .then().statusCode(200)
        .extract().asString();

    assertThatJson(actualMovies)
        .isEqualTo("""
            [
              {
                "imdb": "0084787",
                "title": "The Thing",
                "year": 1982,
                "casts": [
                  "Kurt Russell",
                  "Wilford Brimley"
                ]
              }
            ]
            """);

    var actualMovie = given()
        .when().get("/movies/0084787")
        .then().statusCode(200)
        .extract().asString();

    assertThatJson(actualMovie)
        .isEqualTo("""
            {
              "imdb": "0084787",
              "title": "The Thing",
              "year": 1982,
              "casts": [
                "Kurt Russell",
                "Wilford Brimley"
              ]
            }
            """);
  }

  @Test
  @Order(3)
  public void deleteMovie() {
    given()
        .when().delete("/movies/0084787")
        .then().statusCode(204);

    var actualMovies = given()
        .when().get("/movies")
        .then().statusCode(200)
        .extract().asString();

    assertThatJson(actualMovies)
        .isEqualTo("""
            []
            """);

    given()
        .when().get("/movies/0084787")
        .then().statusCode(404);
  }

}
