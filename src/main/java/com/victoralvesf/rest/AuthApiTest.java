package com.victoralvesf.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthApiTest {
    static Dotenv dotenv = new Dotenv();
    static String APPID = dotenv.get("OPEN_WEATHER_APPID");

    @BeforeClass
    public static void beforeClass() {
        RestAssured.baseURI = "https://api.openweathermap.org/data/2.5";

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.addQueryParam("appid", APPID);
        reqBuilder.addQueryParam("units", "metric");
        reqBuilder.log(LogDetail.ALL);
        RestAssured.requestSpecification = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.log(LogDetail.ALL);
        RestAssured.responseSpecification = resBuilder.build();
    }

    @Test
    public void shouldGetFortalezaWeather() {
        given()
                .queryParam("q", "Fortaleza,BR")
                .when()
                .get("/weather")
                .then()
                .statusCode(200)
                .body("name", is("Fortaleza"))
                .body("id", is(6320062))
                .body("weather", notNullValue())
                .body("main", hasKey("temp"))
                .body("main", hasKey("feels_like"))
                .body("main", hasKey("humidity"));
    }
}

