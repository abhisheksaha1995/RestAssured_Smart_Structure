package resrassuredTests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.hamcrest.Matcher;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;

public class LoginAPI {

	
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
	    public void invalidPasswordTests() {

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
	    
	    @Test
	    public void emptyPasswordTest() {

	        given()
	            .header("Content-Type", "application/x-www-form-urlencoded")
	            .formParam("email", "admin@yopmail.com")
	            .formParam("password", "")
	        .when()
	            .post("https://api.smartstructure.co.in/api/admin/auth/login")
	        .then()
	            .log().all()
	            .statusCode(415);
	    }
	    
	    @Test
	    public void emptyCredentialsTest() {

	        given()
	            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
	            .formParam("email", "")
	            .formParam("password", "")

	        .when()
	            .post("https://api.smartstructure.co.in/api/admin/auth/login")

	        .then()
	            .log().all()
	            .statusCode(400);   // usually validation errors return 400
	    }
		    
	    @Test
	    public void responseTimeTest() {

	    	given()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .formParam("email", "admin@yopmail.com")
            .formParam("password", "Awfis@2026")

        .when()
            .post("https://api.smartstructure.co.in/api/admin/auth/login")

        .then()
            .log().all()
            .time(lessThan(3000L));
    }

    private Matcher<Long> lessThan(long l) {
        return org.hamcrest.Matchers.lessThan(l);
    }
}
	


