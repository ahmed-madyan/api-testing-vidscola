import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


import static io.restassured.RestAssured.given;

public class TodoListTest {

    private String API_URL = "https://jsonplaceholder.typicode.com/todos/";

    @Test
    public void assertThatListFirstTaskHasTitle(){
        String endPointURL = String.format("%s%s",this.API_URL,"1");
        Response todoResponse = given()
                .contentType(ContentType.JSON)
                .get(endPointURL)
                .then()
                .statusCode(200)
                .extract().response();
        JsonPath jsonResponse = todoResponse.body().jsonPath();
        String actualString = jsonResponse.getString("title");
        String expectedString = "delectus aut autem";
        assertEquals(expectedString, actualString);
    }
}