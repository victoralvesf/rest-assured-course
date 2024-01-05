package com.victoralvesf.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UploadFilesTest {
    @BeforeClass
    public static void beforeClass() {
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.log(LogDetail.ALL);
        RestAssured.requestSpecification = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.log(LogDetail.ALL);
        RestAssured.responseSpecification = resBuilder.build();
    }

    @Test
    public void shouldRequireToUploadFile() {
        given()
                .when()
                .post("https://restapi.wcaquino.me/upload")
                .then()
                .statusCode(404)
                .body("error", is("Arquivo não enviado"));
    }

    @Test
    public void shouldUploadAFile() {
        given()
                .multiPart("arquivo", new File("src/main/resources/users.pdf"))
                .when()
                .post("https://restapi.wcaquino.me/upload")
                .then()
                .statusCode(200)
                .body("name", is("users.pdf"));
    }

    @Test
    public void shouldNotUploadALargeFile() {
        given()
                .multiPart("arquivo", new File("src/main/resources/large_audio.mp3"))
                .when()
                .post("https://restapi.wcaquino.me/upload")
                .then()
                .statusCode(413)
                .time(lessThan(5000L));
    }

    @Test
    public void shouldDownloadAFile() throws IOException {
        RestAssured.responseSpecification.logDetail(LogDetail.HEADERS);

        byte[] image = given()
                .when()
                .get("https://restapi.wcaquino.me/download")
                .then()
                .statusCode(200)
                .extract().asByteArray();

        File parsedImage = new File("src/main/resources/image.jpg");
        OutputStream output = new FileOutputStream(parsedImage);
        output.write(image);
        output.close();

        assertThat(parsedImage.length(), lessThan(100000L));
    }
}
