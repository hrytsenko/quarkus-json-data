package hrytsenko;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import hrytsenko.MovieTest.MovieTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@QuarkusTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestProfile(MovieTestProfile.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovieTest {

  @Container
  @SuppressWarnings("resource")
  public static PostgreSQLContainer<?> PG = new PostgreSQLContainer<>("postgres")
      .withDatabaseName("movies")
      .withUsername("admin")
      .withPassword("s3cr3t");

  public static class MovieTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
      PG.start();

      return Map.of(
          "app.database.movies.url", PG.getJdbcUrl(),
          "app.database.movies.username", PG.getUsername(),
          "app.database.movies.password", PG.getPassword());
    }

  }

  @BeforeAll
  public void init() {
    Flyway.configure()
        .dataSource(PG.getJdbcUrl(), PG.getUsername(), PG.getPassword())
        .locations("filesystem:./migrations")
        .load()
        .migrate();
  }

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

    var movies = given()
        .when().get("/movies")
        .then().statusCode(200)
        .extract().asString();

    assertThatJson(movies)
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

    var movie = given()
        .when().get("/movies/0084787")
        .then().statusCode(200)
        .extract().asString();

    assertThatJson(movie)
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

    var movies = given()
        .when().get("/movies")
        .then().statusCode(200)
        .extract().asString();

    assertThatJson(movies)
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

    var movie = given()
        .when().get("/movies/0084787")
        .then().statusCode(200)
        .extract().asString();

    assertThatJson(movie)
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

    var movies = given()
        .when().get("/movies")
        .then().statusCode(200)
        .extract().asString();

    assertThatJson(movies)
        .isEqualTo("""
            []
            """);

    given()
        .when().get("/movies/0084787")
        .then().statusCode(404);
  }

}
