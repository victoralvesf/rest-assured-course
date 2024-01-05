package com.victoralvesf.rest;

import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class UserXmlTest {
    @Test
    public void shouldValidateXml() {
        given()
                .when()
                .get("https://restapi.wcaquino.me/usersXML/3")
                .then()
                .statusCode(200)
                .rootPath("user")
                .body("name", is("Ana Julia"))
                .body("@id", is("3"))
                .rootPath("user.filhos")
                .body("name.size()", is(2))
                .body("name", hasItems("Zezinho", "Luizinho"))
                .detachRootPath("filhos")
                .body("filhos.name", hasItem("Luizinho"))
                .appendRootPath("filhos")
                .body("name[1]", is("Luizinho"));
    }

    @Test
    public void shouldValidateAdvancedXml() {
        given()
                .when()
                .get("https://restapi.wcaquino.me/usersXML")
                .then()
                .statusCode(200)
                .rootPath("users.user")
                .body("size()", is(3))
                .body("findAll{it.age.toInteger() <= 25}.size()", is(2))
                .body("@id", hasItems("1", "2", "3"))
                .body("find{it.age == 25}.name", is("Maria Joaquina"))
                .body("findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
                .body("salary.find{it != null}.toDouble()", is(1234.5678d))
                .body("age.collect{it.toInteger() * 2}", hasItems(60, 50, 40))
                .body("name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"));
    }

    @Test
    public void shouldValidateAdvancedXmlWithJava() {
        ArrayList<String> users = given()
                .when()
                .get("https://restapi.wcaquino.me/usersXML")
                .then()
                .statusCode(200)
                .extract().path("users.user.name.collect{it.toString().toUpperCase()}.findAll{it.toString().contains('N')}");

        Assert.assertEquals(2, users.size());
        Assert.assertEquals("MARIA JOAQUINA", users.get(0));
        Assert.assertEquals("ANA JULIA", users.get(1));
    }

    @Test
    public void shouldValidateXmlWithXPath() {
        given()
                .when()
                .get("https://restapi.wcaquino.me/usersXML")
                .then()
                .statusCode(200)
                .body(hasXPath("count(/users/user)", is("3")))
                .body(hasXPath("/users/user/name[contains(text(), 'Maria')]", is("Maria Joaquina")))
                .body(hasXPath("//user[@id = '1']/name", is("João da Silva")))
                .body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
                .body(hasXPath("//name[text() = 'Ana Julia']/../filhos", allOf(containsString("Zezinho"), containsString("Luizinho"))))
                .body(hasXPath("/users/user/name", is("João da Silva")))
                .body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
                .body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
                .body(hasXPath("count(/users/user/name[contains(., 'n')])", is("2")))
                .body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
                .body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
                .body(hasXPath("//user[age > 20][age < 30]/name", is("Maria Joaquina")))
        ;
    }
}
