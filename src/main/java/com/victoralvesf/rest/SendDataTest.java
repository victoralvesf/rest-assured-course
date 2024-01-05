package com.victoralvesf.rest;

import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class SendDataTest {
    @Test
    public void shouldSendDataOnQueryParamsXml() {
        given()
                .log().all()
                .queryParam("format", "xml")
                .when()
                .get("https://restapi.wcaquino.me/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.XML);
    }

    @Test
    public void shouldSendDataOnQueryParamsJson() {
        given()
                .log().all()
                .queryParam("format", "json")
                .when()
                .get("https://restapi.wcaquino.me/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .contentType(containsString("charset=utf-8"));
    }

    @Test
    public void shouldSendDataOnHeadersJson() {
        given()
                .log().all()
                .accept(ContentType.JSON)
                .when()
                .get("https://restapi.wcaquino.me/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void shouldSendDataOnHeadersXml() {
        given()
                .log().all()
                .accept(ContentType.XML)
                .when()
                .get("https://restapi.wcaquino.me/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.XML);
    }
}
