package hrytsenko;

import java.util.List;

record Movie(
    String imdb,
    String title,
    Integer year,
    List<String> casts) {

}
