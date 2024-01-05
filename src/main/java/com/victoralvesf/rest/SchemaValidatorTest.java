package com.victoralvesf.rest;

import io.restassured.RestAssured;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.Test;
import org.xml.sax.SAXParseException;

public class SchemaValidatorTest {
    @Test
    public void shouldValidateReturnUsingXmlSchema() {
        RestAssured
                .given()
                .when()
                .get("https://restapi.wcaquino.me/usersXML")
                .then()
                .statusCode(200)
                .body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"));
    }

    @Test(expected = SAXParseException.class)
    public void shouldValidateInvalidReturnUsingXmlSchema() {
        RestAssured
                .given()
                .when()
                .get("https://restapi.wcaquino.me/invalidUsersXML")
                .then()
                .statusCode(200)
                .body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"));
    }

    @Test
    public void shouldValidateReturnUsingJsonSchema() {
        RestAssured
                .given()
                .when()
                .get("https://restapi.wcaquino.me/users")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"));
    }
}
