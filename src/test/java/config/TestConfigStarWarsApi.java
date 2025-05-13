package config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;


public class TestConfigStarWarsApi {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://swapi.tech/api/";
//        RestAssured.basePath = "/api/";

    }
}
