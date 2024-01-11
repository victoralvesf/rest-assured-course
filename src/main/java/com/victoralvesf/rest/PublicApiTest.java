package com.victoralvesf.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PublicApiTest {
    @BeforeClass
    public static void beforeClass() {
        RestAssured.baseURI = "https://swapi.dev/api/";

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.log(LogDetail.ALL);
        RestAssured.requestSpecification = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.log(LogDetail.ALL);
        RestAssured.responseSpecification = resBuilder.build();
    }

    @Test
    public void shouldAccessStarWarsApi() {
        given()
                .when()
                .get("/people/1")
                .then()
                .statusCode(200)
                .body("name", is("Luke Skywalker"))
                .body("height", is("172"))
                .body("mass", is("77"));
    }
}
