package hrytsenko.standalone;

import com.fasterxml.jackson.databind.ObjectMapper;
import hrytsenko.Movie;
import hrytsenko.MovieRepository;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import io.quarkus.arc.properties.IfBuildProperty;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@IfBuildProperty(name = "app.mode", stringValue = "production")
public class ProductionMovieRepository implements MovieRepository {

  private final ObjectMapper MAPPER = new ObjectMapper();

  @DataSource("movies")
  AgroalDataSource dataSource;

  @PostConstruct
  void init() {
    log.info("Starting production repository");
  }

  @SneakyThrows
  @Override
  public List<Movie> findAll() {
    log.info("Find all movies");
    String query = "SELECT * FROM movies";

    try (var connection = dataSource.getConnection();
        var statement = connection.prepareStatement(query)) {
      var results = statement.executeQuery();
      var movies = new ArrayList<Movie>();
      while (results.next()) {
        movies.add(MAPPER.readValue(results.getString("movie"), Movie.class));
      }
      return movies;
    }
  }

  @SneakyThrows
  @Override
  public void createMovie(Movie movie) {
    log.info("Create movie {}", movie);
    String query = "INSERT INTO movies (movie) VALUES (?::jsonb)";

    try (var connection = dataSource.getConnection();
        var statement = connection.prepareStatement(query)) {
      statement.setString(1, MAPPER.writeValueAsString(movie));
      statement.executeUpdate();
    }
  }

  @SneakyThrows
  @Override
  public Optional<Movie> findMovie(String imdb) {
    log.info("Find movie by imdb {}", imdb);
    String query = "SELECT * FROM movies WHERE movie->>'imdb' = (?)";

    try (var connection = dataSource.getConnection();
        var statement = connection.prepareStatement(query)) {
      statement.setString(1, imdb);
      var results = statement.executeQuery();
      if (!results.next()) {
        return Optional.empty();
      }
      return Optional.of(MAPPER.readValue(results.getString("movie"), Movie.class));
    }
  }

  @SneakyThrows
  @Override
  public void deleteMovie(String imdb) {
    log.info("Delete movie by imdb {}", imdb);
    String query = "DELETE FROM movies WHERE movie->>'imdb' = (?)";

    try (var connection = dataSource.getConnection();
        var statement = connection.prepareStatement(query)) {
      statement.setString(1, imdb);
      statement.executeUpdate();
    }
  }

}
