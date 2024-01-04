package com.victoralvesf.rest;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserJsonTest {
    @Test
    public void validateUsersJsonApi() {
        given()
                .when()
                .get("https://restapi.wcaquino.me/users/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("João da Silva"))
                .body("age", is(30))
                .body("salary", is(1234.5678F));
    }

    @Test
    public void validateUsersJsonApiMultiLevel() {
        given()
                .when()
                .get("https://restapi.wcaquino.me/users/2")
                .then()
                .statusCode(200)
                .body("id", is(2))
                .body("name", is("Maria Joaquina"))
                .body("endereco.rua", is("Rua dos bobos"))
                .body("endereco.numero", is(0))
                .body("age", is(25))
                .body("salary", is(2500));
    }

    @Test
    public void validateUsersJsonApiWithList() {
        given()
                .when()
                .get("https://restapi.wcaquino.me/users/3")
                .then()
                .statusCode(200)
                .body("id", is(3))
                .body("name", is("Ana Júlia"))
                .body("age", is(20))
                .body("filhos", hasSize(2))
                .body("filhos[0].name", is("Zezinho"))
                .body("filhos[1].name", is("Luizinho"))
                .body("filhos.name", hasSize(2))
                .body("filhos.name", hasItems("Zezinho", "Luizinho"));
    }

    @Test
    public void shouldReturnNotFoundUserError() {
        given()
                .when()
                .get("https://restapi.wcaquino.me/users/4")
                .then()
                .statusCode(404)
                .body("error", is("Usuário inexistente"));
    }

    @Test
    public void shouldValidateUsersList() {
        given()
                .when()
                .get("https://restapi.wcaquino.me/users")
                .then()
                .statusCode(200)
                .body("$", hasSize(3))
                .body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
                .body("age[1]", is(25))
                .body("filhos[2].name", hasSize(2))
                .body("filhos[2].name", hasItems("Zezinho", "Luizinho"));
    }
}
