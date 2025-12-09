package hrytsenko;

import jakarta.inject.Inject;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/movies")
class MovieResource {

  @Inject
  MovieRepository repository;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Movie> listMovies() {
    return repository.listMovies();
  }

  @GET
  @Path("/{imdb}")
  @Produces(MediaType.APPLICATION_JSON)
  public Movie getMovie(@PathParam("imdb") String imdb) {
    return repository.findMovie(imdb)
        .orElseThrow(() -> new ClientErrorException("Movie not found", Response.Status.NOT_FOUND));
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public void createMovie(Movie movie) {
    if (repository.findMovie(movie.imdb()).isPresent()) {
      throw new ClientErrorException("Movie already exists", Response.Status.CONFLICT);
    }
    repository.createMovie(movie);
  }

  @PUT
  @Path("/{imdb}")
  @Consumes(MediaType.APPLICATION_JSON)
  public void updateMovie(@PathParam("imdb") String imdb, Movie movie) {
    if (!imdb.equals(movie.imdb())) {
      throw new ClientErrorException("Movie mismatch", Response.Status.BAD_REQUEST);
    }
    if (repository.findMovie(imdb).isEmpty()) {
      throw new ClientErrorException("Movie not found", Response.Status.NOT_FOUND);
    }
    repository.updateMovie(movie);
  }

  @DELETE
  @Path("/{imdb}")
  public void deleteMovie(@PathParam("imdb") String imdb) {
    repository.deleteMovie(imdb);
  }

}
