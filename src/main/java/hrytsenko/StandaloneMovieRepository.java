package hrytsenko;

import static org.dizitart.no2.filters.FluentFilter.where;

import io.quarkus.arc.properties.IfBuildProperty;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.filters.Filter;
import org.dizitart.no2.mapper.jackson.JacksonMapperModule;
import org.dizitart.no2.repository.ObjectRepository;

@Slf4j
@ApplicationScoped
@IfBuildProperty(name = "app.mode", stringValue = "standalone")
class StandaloneMovieRepository implements MovieRepository {

  Nitrite database;

  ObjectRepository<Movie> movies;

  @PostConstruct
  void init() {
    log.info("Starting standalone repository");
    database = Nitrite.builder()
        .loadModule(new JacksonMapperModule())
        .openOrCreate();
    movies = database.getRepository(Movie.class);
  }

  @Override
  public List<Movie> listMovies() {
    log.info("Find all movies");
    return movies.find().toList();
  }

  @Override
  public Optional<Movie> findMovie(String imdb) {
    log.info("Find movie by imdb {}", imdb);
    return Optional.ofNullable(
        movies.find(byImdb(imdb)).firstOrNull());
  }

  @Override
  public void createMovie(Movie movie) {
    log.info("Create movie {}", movie);
    movies.insert(movie);
  }

  @Override
  public void updateMovie(Movie movie) {
    log.info("Update movie {}", movie);
    movies.update(byImdb(movie.imdb()), movie);
  }

  @Override
  public void deleteMovie(String imdb) {
    log.info("Delete movie by imdb {}", imdb);
    movies.remove(byImdb(imdb));
  }

  private static Filter byImdb(String imdb) {
    return where("imdb").eq(imdb);
  }

}
