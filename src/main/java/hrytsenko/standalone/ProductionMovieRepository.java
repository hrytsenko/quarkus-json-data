package hrytsenko.standalone;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hrytsenko.Movie;
import hrytsenko.MovieRepository;
import io.quarkus.arc.properties.IfBuildProperty;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Slf4j
@ApplicationScoped
@IfBuildProperty(name = "app.mode", stringValue = "production")
public class ProductionMovieRepository implements MovieRepository {

  @PersistenceContext(unitName = "movies")
  EntityManager entityManager;

  @PostConstruct
  void init() {
    log.info("Starting production repository");
  }

  @Override
  public List<Movie> findAll() {
    log.info("Find all movies");
    var query = "SELECT * FROM movies";
    @SuppressWarnings("unchecked")
    List<MovieEntity> entities = entityManager
        .createNativeQuery(query, MovieEntity.class)
        .getResultList();
    return entities.stream()
        .map(MovieEntity::getMovie)
        .toList();
  }

  @Override
  public Optional<Movie> findMovie(String imdb) {
    log.info("Find movie by imdb {}", imdb);
    var query = "SELECT * FROM movies WHERE movie->>'imdb' = :imdb";
    @SuppressWarnings("unchecked")
    List<MovieEntity> entities = entityManager
        .createNativeQuery(query, MovieEntity.class)
        .setParameter("imdb", imdb)
        .getResultList();
    return entities.stream()
        .map(MovieEntity::getMovie)
        .findFirst();
  }

  @Transactional
  @Override
  public void createMovie(Movie movie) {
    log.info("Create movie {}", movie);
    entityManager.persist(MovieEntity.of(movie));
  }

  @Transactional
  @Override
  public void deleteMovie(String imdb) {
    log.info("Delete movie by imdb {}", imdb);
    var query = "DELETE FROM movies WHERE movie->>'imdb' = :imdb";
    entityManager.createNativeQuery(query)
        .setParameter("imdb", imdb)
        .executeUpdate();
  }

  @Entity
  @Table(name = "movies")
  @NoArgsConstructor
  @AllArgsConstructor
  static class MovieEntity {

    static MovieEntity of(Movie movie) {
      return new MovieEntity(movie.imdb(), movie);
    }

    @Getter
    @Id
    private String id;

    @Getter
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "movie", columnDefinition = "jsonb")
    @Convert(converter = MovieConverter.class)
    private Movie movie;

  }

  @Converter
  static class MovieConverter implements AttributeConverter<Movie, JsonNode> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public JsonNode convertToDatabaseColumn(Movie movie) {
      return objectMapper.valueToTree(movie);
    }

    @Override
    public Movie convertToEntityAttribute(JsonNode dbData) {
      return objectMapper.convertValue(dbData, Movie.class);
    }

  }

}
