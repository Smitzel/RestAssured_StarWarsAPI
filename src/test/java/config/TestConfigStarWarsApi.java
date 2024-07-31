package config;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class TestConfigStarWarsApi {
    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://swapi.dev/api/";
        RestAssured.basePath = "/api/";

    }
}
