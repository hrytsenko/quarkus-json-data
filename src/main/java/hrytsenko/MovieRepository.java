package hrytsenko;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {

  List<Movie> findAll();

  void createMovie(Movie movie);

  Optional<Movie> findMovie(String imdb);

  void deleteMovie(String imdb);

}
