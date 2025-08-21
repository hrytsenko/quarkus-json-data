package hrytsenko.standalone;

import static org.dizitart.no2.filters.FluentFilter.where;

import hrytsenko.Movie;
import hrytsenko.MovieRepository;
import io.quarkus.arc.properties.IfBuildProperty;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.common.mapper.JacksonMapperModule;
import org.dizitart.no2.filters.NitriteFilter;
import org.dizitart.no2.repository.ObjectRepository;

@Slf4j
@ApplicationScoped
@IfBuildProperty(name = "app.mode", stringValue = "standalone")
class StandaloneMovieRepository implements MovieRepository {

  Nitrite database;

  ObjectRepository<Movie> movies;

  @PostConstruct
  void init() {
    log.info("Starting in standalone mode");
    database = Nitrite.builder()
        .loadModule(new JacksonMapperModule())
        .openOrCreate();
    movies = database.getRepository(Movie.class);
  }

  @Override
  public List<Movie> findAll() {
    return movies.find().toList();
  }

  @Override
  public Optional<Movie> findMovie(String imdb) {
    return Optional.ofNullable(
        movies.find(byImdb(imdb)).firstOrNull());
  }

  @Override
  public void createMovie(Movie movie) {
    movies.insert(movie);
  }

  @Override
  public void deleteMovie(String imdb) {
    movies.remove(byImdb(imdb));
  }

  private static NitriteFilter byImdb(String imdb) {
    return where("imdb").eq(imdb);
  }

}
