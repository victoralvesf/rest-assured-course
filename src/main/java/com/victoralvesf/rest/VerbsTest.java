package com.victoralvesf.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class VerbsTest {
    @BeforeClass
    public static void beforeClass() {
        RestAssured.baseURI = "https://restapi.wcaquino.me";

        RequestSpecBuilder reqSpec = new RequestSpecBuilder();
        reqSpec.log(LogDetail.ALL);
        RestAssured.requestSpecification = reqSpec.build();

        ResponseSpecBuilder resSpec = new ResponseSpecBuilder();
        resSpec.log(LogDetail.ALL);
        RestAssured.responseSpecification = resSpec.build();
    }

    @Test
    public void shouldCreateAnUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Joaquin\", \"age\": 60 }")
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Joaquin"))
                .body("age", is(60));
    }

    @Test
    public void shouldCreateAnUserWithMap() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Iasmin");
        user.put("age", 5);

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Iasmin"))
                .body("age", is(5));
    }

    @Test
    public void shouldCreateAnUserWithObject() {
        User user = new User("Kiko", 26);
        user.setSalary(100d);

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Kiko"))
                .body("age", is(26))
                .body("salary", is(100));
    }

    @Test
    public void shouldCreateAnUserWithDeserializedObject() {
        User user = new User("Usuario deserializado", 30);

        User result = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(User.class);

        Assert.assertEquals("Usuario deserializado", result.getName());
        assertThat(result.getAge(), is(30));
        assertThat(result.getId(), notNullValue());
    }

    @Test
    public void shouldNotCreateAnUserWithoutName() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"age\": 31, \"salary\": 6800 }")
                .when()
                .post("/users")
                .then()
                .statusCode(400)
                .body("error", is("Name é um atributo obrigatório"));
    }

    @Test
    public void shouldCreateAnUserUsingXml() {
        given()
                .contentType(ContentType.XML)
                .body("<user><name>Joaquin</name><age>55</age></user>")
                .when()
                .post("/usersXML")
                .then()
                .statusCode(201)
                .rootPath("user")
                .body("@id", is(notNullValue()))
                .body("name", is("Joaquin"))
                .body("age", is("55"));
    }

    @Test
    public void shouldCreateAnUserUsingXmlAndObject() {
        User user = new User("Usuario XML", 70);

        given()
                .contentType(ContentType.XML)
                .body(user)
                .when()
                .post("/usersXML")
                .then()
                .statusCode(201)
                .rootPath("user")
                .body("@id", is(notNullValue()))
                .body("name", is("Usuario XML"))
                .body("age", is("70"));
    }

    @Test
    public void shouldCreateAnUserUsingXmlAndDeserializedObject() {
        User user = new User("Usuario XML Desserializado", 52);

        User result = given()
                .contentType(ContentType.XML)
                .body(user)
                .when()
                .post("/usersXML")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(User.class);

        System.out.println(result);

        assertThat(result.getId(), is(notNullValue()));
        assertThat(result.getName(), is("Usuario XML Desserializado"));
        assertThat(result.getAge(), is(52));
        assertThat(result.getSalary(), is(nullValue()));
    }

    @Test
    public void shouldChangeAnUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Usuario foi alterado\", \"age\": 41 }")
                .when()
                .put("/users/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Usuario foi alterado"))
                .body("age", is(41))
                .body("salary", is(1234.5678f));
    }

    @Test
    public void shouldUseParamsInTheUri() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Usuario foi alterado\", \"age\": 41 }")
                .when()
                .put("/{entity}/{id}", "users", "1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Usuario foi alterado"))
                .body("age", is(41))
                .body("salary", is(1234.5678f));
    }

    @Test
    public void shouldAlsoUseParamsInTheUri() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("entity", "users")
                .pathParam("id", 1)
                .body("{ \"name\": \"Usuario foi alterado\", \"age\": 41 }")
                .when()
                .put("/{entity}/{id}")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Usuario foi alterado"))
                .body("age", is(41))
                .body("salary", is(1234.5678f));
    }

    @Test
    public void shouldRemoveAnUser() {
        given()
                .when()
                .delete("/users/1")
                .then()
                .statusCode(204);
    }


    @Test
    public void shouldNotRemoveANonExistentUser() {
        given()
                .when()
                .delete("/users/100")
                .then()
                .statusCode(400)
                .body("error", is("Registro inexistente"));
    }
}
