package com.victoralvesf.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class JwtAuthTest {
    static Dotenv dotenv = new Dotenv();
    static String USER_EMAIL = dotenv.get("JWT_AUTH_USER");
    static String USER_PASSWORD = dotenv.get("JWT_AUTH_PASSWORD");

    @BeforeClass
    public static void beforeClass() {
        RestAssured.baseURI = "https://barrigarest.wcaquino.me";

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.log(LogDetail.ALL);
        RestAssured.requestSpecification = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.log(LogDetail.ALL);
        RestAssured.responseSpecification = resBuilder.build();
    }

    @Test
    public void shouldAuthenticateWithJwtToken() {
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", USER_EMAIL);
        login.put("senha", USER_PASSWORD);

        String token = given()
                .contentType(ContentType.JSON)
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", String.format("JWT %s", token))
                .when()
                .get("/contas")
                .then()
                .statusCode(200)
                .body("nome", hasItem("Conta para movimentacoes"));
    }
}
