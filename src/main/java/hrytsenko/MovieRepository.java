package hrytsenko;

import java.util.List;
import java.util.Optional;

interface MovieRepository {

  List<Movie> findAll();

  Optional<Movie> findMovie(String imdb);

  void createMovie(Movie movie);

  void updateMovie(Movie movie);

  void deleteMovie(String imdb);

}
