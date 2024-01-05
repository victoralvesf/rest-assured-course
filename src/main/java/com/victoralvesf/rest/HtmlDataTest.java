package com.victoralvesf.rest;

import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class HtmlDataTest {
    @Test
    public void shouldDoSearchWithHtml() {
        given()
                .log().all()
                .when()
                .get("https://restapi.wcaquino.me/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.HTML)
                .rootPath("html.body.div.table.tbody")
                .body("tr.size()", is(3))
                .body("tr[1].td[0]", is("2"))
                .body("tr[1].td[1]", is("Maria Joaquina"))
                .body("tr[1].td[2]", is("25"));
    }

    @Test
    public void shouldDoSearchWithHtmlUsingXpath() {
        given()
                .queryParam("format", "clean")
                .when()
                .get("https://restapi.wcaquino.me/v2/users")
                .then()
                .statusCode(200)
                .contentType(ContentType.HTML)
                .body(hasXPath("count(//table/tr)", is("4")))
                .body(hasXPath("//td[text() = 'Maria Joaquina']/../td[3]", is("25")))
        ;
    }
}
