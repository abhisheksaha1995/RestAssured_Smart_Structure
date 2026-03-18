package resrassuredTests;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import io.restassured.config.EncoderConfig;

public class Logout_GET_Request {

    @Test
    public void logoutRequest() {

        // Fix charset issue
        RestAssured.config = RestAssured.config()
                .encoderConfig(EncoderConfig.encoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false));

        // LOGIN API
        Response loginResponse =
        given()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .formParam("email", "admin@yopmail.com")
            .formParam("password", "Awfis@2026")
        .when()
            .post("https://api.smartstructure.co.in/api/admin/auth/login");

        loginResponse.prettyPrint();

        String token = loginResponse.jsonPath().getString("data.access_token");

        System.out.println("Access Token: " + token);

        Assert.assertNotNull(token, "Token not generated. Login failed.");

        // LOGOUT API
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("https://api.smartstructure.co.in/api/admin/auth/logout")
        .then()
            .log().all()
            .statusCode(200)
            .body("message", equalTo("Logout successfully!!"));
    }
    @Test
    public void successfulLogin() {

        given()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .formParam("email", "admin@yopmail.com")
            .formParam("password", "Awfis@2026")
        .when()
            .post("https://api.smartstructure.co.in/api/admin/auth/login")
        .then()
            .log().all()
            .statusCode(200)
            .body("message", equalTo("Login successfully!!"))
            .body("data.access_token", notNullValue());
    }
    
    @Test
    public void invalidPasswordTest() {

        RestAssured.config = RestAssured.config()
        .encoderConfig(EncoderConfig.encoderConfig()
        .appendDefaultContentCharsetToContentTypeIfUndefined(false));

        given()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .formParam("email", "admin@yopmail.com")
            .formParam("password", "wrong123")
        .when()
            .post("https://api.smartstructure.co.in/api/admin/auth/login")
        .then()
            .log().all()
            .statusCode(404);
    }
    @Test
    public void invalidEmailTest() {

        given()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .formParam("email", "wrong@yopmail.com")
            .formParam("password", "Awfis@2026")
        .when()
            .post("https://api.smartstructure.co.in/api/admin/auth/login")
        .then()
            .log().all()
            .statusCode(415);
    }
    
    @Test
    public void emptyEmailTest() {

        given()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .formParam("email", "")
            .formParam("password", "Awfis@2026")
        .when()
            .post("https://api.smartstructure.co.in/api/admin/auth/login")
        .then()
            .log().all()
            .statusCode(415);
    }
}

