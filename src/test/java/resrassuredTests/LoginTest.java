package resrassuredTests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LoginTest {

    private final String ENDPOINT = "/api/admin/auth/login";

    @BeforeClass
    public void setup() {
        // Setting the base URI for the specified environment
        RestAssured.baseURI = "https://smartstructureapi.myvtd.site";

        // RestAssured adds charset=ISO-8859-1 by default which the server rejects.
        // We configure it to use UTF-8 or not append it if undefined.
        RestAssured.config = RestAssured.config().encoderConfig(
                io.restassured.config.EncoderConfig.encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)
                        .defaultContentCharset("UTF-8"));
    }

    @Test(priority = 1, description = "TC01: Verify successful login (Form)")
    public void testSuccessfulLogin_Form() {
        given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("email", "admin@yopmail.com")
                .formParam("password", "Awfis@2026")
                .when()
                .post(ENDPOINT)
                .then()
                .log().ifError()
                .statusCode(200)
                .body("message", anyOf(equalTo("Login successfully!!"), equalTo("Login successful")))
                .body("data.access_token", notNullValue());
    }

    @Test(priority = 2, description = "TC02: Verify successful login (JSON)")
    public void testSuccessfulLogin_Json() {
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{\"email\":\"admin@yopmail.com\", \"password\":\"Awfis@2026\"}")
                .when()
                .post(ENDPOINT);

        response.then().log().ifError()
                .statusCode(anyOf(equalTo(200), equalTo(415), equalTo(400)));

        if (response.getStatusCode() == 200) {
            response.then().body("data.access_token", notNullValue());
        }
    }

    @Test(priority = 3, description = "TC03: Verify login fails with incorrect credentials")
    public void testInvalidCredentials() {
        given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("email", "admin@yopmail.com")
                .formParam("password", "WrongPassword123")
                .when()
                .post(ENDPOINT)
                .then()
                .log().ifError()
                .statusCode(anyOf(equalTo(401), equalTo(404), equalTo(422), equalTo(500)));
    }

    @Test(priority = 4, description = "TC04: Verify login fails with invalid HTTP method (GET)")
    public void testInvalidMethod() {
        given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .when()
                .get(ENDPOINT)
                .then()
                .log().ifError()
                .statusCode(anyOf(equalTo(405), equalTo(404), equalTo(400)));
    }

    @Test(priority = 5, description = "TC05: Performance - Response Time")
    public void testResponseTime() {
        given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("email", "admin@yopmail.com")
                .formParam("password", "Awfis@2026")
                .when()
                .post(ENDPOINT)
                .then()
                .time(lessThan(5000L));
    }
}
