import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

public class MoviesTest {

    private final String API_URL = "https://movie-database-imdb-alternative.p.rapidapi.com/";
    private final String API_HOST = "movie-database-imdb-alternative.p.rapidapi.com";
    private final String API_KEY = "21e6f4f25dmsh8bbed3c079a43acp14bd7bjsnb34a3bcca5f8";
    private static final String API_MOVIE = "https://movie-database-imdb-alternative.p.rapidapi.com/?r=json";
    private List<HashMap<String, String>> movies;
    private static Response response;

    @Test
    public void assertThatSearchApiReturnsMatchingResults() {
        String movieName = "Avengers";
        response = given()
                .contentType(ContentType.JSON)
                .header("X-RapidAPI-Host", this.API_HOST)
                .header("X-RapidAPI-Key", this.API_KEY)
                .when()
                .get(String.format("%s?s=%s", API_URL, movieName))
                .then()
                .statusCode(200)
                .extract().response();
        movies = response.getBody().jsonPath().getList("Search");
        HashMap<String, String> firstMovie = movies.get(0);
        assertTrue(firstMovie.get("Title").contains(movieName));
    }

    @Test
    public void assertThatPulpFictionMovieRuntimeStoredCorrectly() {
        String expectedPulpFictionMovieRuntime = "154 min";
        String pulpFictionMovieName = "Pulp Fiction";
        String id;
        response = given()
                .contentType(ContentType.JSON)
                .header("X-RapidAPI-Host", this.API_HOST)
                .header("X-RapidAPI-Key", this.API_KEY)
                .when()
                .get(String.format("%s?s=%s", API_URL, pulpFictionMovieName))
                .then()
                .statusCode(200)
                .extract().response();
        movies = response.getBody().jsonPath().getList("Search");
        id = getID(pulpFictionMovieName);
        Assert.assertNotNull(id);
        response = given()
                .contentType(ContentType.JSON)
                .header("X-RapidAPI-Host", this.API_HOST)
                .header("X-RapidAPI-Key", this.API_KEY)
                .when()
                .queryParam("i", id)
                .get(API_MOVIE)
                .then()
                .statusCode(200)
                .extract().response();
        Assert.assertEquals(response.getBody().jsonPath().getString("Runtime"), expectedPulpFictionMovieRuntime);
    }

    private String getID(String movieName) {
        for (HashMap<String, String> movie : movies)
            if (movie.containsValue(movieName))
                return movie.get("imdbID");
        return null;
    }
}