package com.victoralvesf.rest;

import io.restassured.http.Method;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;

public class HelloWorldTest {

    @Test
    public void shouldReturnDefaultMessage() {
        Response response = request(Method.GET, "https://restapi.wcaquino.me/ola");

        Assert.assertEquals("Ola Mundo!", response.getBody().asString());
        Assert.assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldAlsoReturnDefaultMessage() {
        get("https://restapi.wcaquino.me/ola").then().statusCode(200);
    }

    @Test
    public void shouldAlsoReturnDefaultMessageGherkin() {
        given()
            .when()
            .get("https://restapi.wcaquino.me/ola")
            .then()
            .statusCode(200);
    }

    @Test
    public void hamcrestMatchers() {
        assertThat("Maria", is("Maria"));
        assertThat(128, is(128));
        assertThat(128, isA(Integer.class));
        assertThat(128d, isA(Double.class));
        assertThat(128d, greaterThan(120d));
        assertThat(128d, lessThan(130d));

        List<Integer> testList = Arrays.asList(1,3,5,7,9);

        assertThat(testList, hasSize(5));
        assertThat(testList, contains(1,3,5,7,9));
        assertThat(testList, containsInAnyOrder(3,1,9,7,5));
        assertThat(testList, hasItem(1));
        assertThat(testList, hasItems(1,5));

        assertThat("Maria", is(not("Joao")));
        assertThat("Maria", not("Joao"));
        assertThat("Maria", anyOf(is("Maria"), is("Marta")));
        assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
    }

    @Test
    public void validateApiBody() {
        given()
            .when()
                .get("https://restapi.wcaquino.me/ola")
            .then()
                .statusCode(200)
                .body(is("Ola Mundo!"))
                .body(containsString("Mundo"))
                .body(notNullValue());
    }
}
