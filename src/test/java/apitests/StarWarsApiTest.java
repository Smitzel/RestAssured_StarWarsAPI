package apitests;

import config.TestConfigStarWarsApi;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


public class StarWarsApiTest extends TestConfigStarWarsApi {

    @Test()
    public void printStatusCode() {
        int statusCode =
                given()
                        .log().all()
                        .get(baseURI)
                        .getStatusCode();
        System.out.println("The response status is " + statusCode);
    }

    @Test()
    public void getAllEndpoints() {
        given()
                .when()
                .get(baseURI)
                .then()
                .assertThat()
                .statusCode(200)
                .contentType("application/json")
                .body(containsString("people"))
                .body(containsString("planets"))
                .body(containsString("films"))
                .body(containsString("species"))
                .body(containsString("vehicles"))
                .body(containsString("starships"))
                .body("size()", is(6));
//                .log().all();
    }

    @Test()
    public void getFirstFilm() {
        given()
                .when()
                .get(baseURI + "films/")
                .then()
//                    .log().all()
                .assertThat().body("result.properties.title[0]", equalTo("A New Hope"));

    }


    @Test()
    public void getFilmIdAndName() {
        String episodeId = get(baseURI + "films/").jsonPath().getString("result.properties.episode_id[2]");
        System.out.println(episodeId);

        String episodeName = get(baseURI + "films/" + episodeId).jsonPath().getString("result.properties.title");
        assertEquals("Revenge of the Sith", episodeName);
        System.out.println(episodeName);
    }

    @Test()
    public void getCountFilms() {
        Response response = get(baseURI + "films/")
                .then()
                .extract().response();

        int titleCount = response.jsonPath().getList("result.properties.title").size();
        assertEquals(6, titleCount);
    }

    @Test()
    public void getListOfFilms() {
        List<String> filmList = get(baseURI + "films/").jsonPath().getList("results.title");
        System.out.println(filmList);
    }

    @Test()
    public void getManufacturerFromStarship() {
        // Get the response body
        Response response = get(baseURI + "starships/");
// And get the manufacturer from the response.
        String manufacturer = response.path("results.find { it.name == 'CR90 corvette' }.manufacturer");
        System.out.println(manufacturer);
    }

    @Test()
    public void getAllStarshipsAbove500() {
        // Get the response body as a String
        Response response = get(baseURI + "starships/");
// And get the starships with >= 500 passengers from the response.
        List<String> starShips = response.path("results.findAll { it.passengers >= '500' }.name");
        System.out.println(starShips);
    }

    @Test()
    public void getAllPeopleWithNoHair() {
        // Get the response body
        Response response = get(baseURI + "people/");
// And get the bald people from the response.
        List<String> baldPeople = response.path("results.findAll { it.hair_color == 'n/a' }.name");
        System.out.println(baldPeople);
    }

    @Test()
    public void validateResponseToJSONSchema() {
        get(baseURI + "people/1")
                .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("PeopleJsonSchema.json"));
    }

    @Test()
    public void captureResponseTime() {
        long responseTime = get(baseURI).time();
        System.out.println("Response time in ms: " + responseTime);
    }

    @Test()
    public void assertOnResponseTime() {
        get(baseURI)
                .then()
                .time(lessThan(5000L));
    }

}