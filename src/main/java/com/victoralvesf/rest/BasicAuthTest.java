package com.victoralvesf.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BasicAuthTest {
    static Dotenv dotenv = new Dotenv();
    static String USER = dotenv.get("BASIC_AUTH_USER");
    static String PASSWORD = dotenv.get("BASIC_AUTH_PASSWORD");


    @BeforeClass
    public static void beforeClass() {
        RestAssured.baseURI = "https://restapi.wcaquino.me";

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.log(LogDetail.ALL);
        RestAssured.requestSpecification = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.log(LogDetail.ALL);
        RestAssured.responseSpecification = resBuilder.build();
    }

    @Test
    public void shouldBeUnauthorized() {
        given()
                .when()
                .get("/basicauth")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldMakeBasicAuth() {
        given()
                .auth()
                .basic(USER, PASSWORD)
                .when()
                .get("/basicauth")
                .then()
                .statusCode(200)
                .body("status", is("logado"));
    }

    @Test
    public void shouldMakePreemptiveBasicAuth() {
        given()
                .auth().preemptive().basic(USER, PASSWORD)
                .when()
                .get("/basicauth2")
                .then()
                .statusCode(200)
                .body("status", is("logado"));
    }
}
