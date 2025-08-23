package hrytsenko;

import java.util.List;

public record Movie(
    String imdb,
    String title,
    Integer year,
    List<String> casts) {

}
