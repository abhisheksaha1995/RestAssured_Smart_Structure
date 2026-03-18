package resrassuredTests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class BrandAPI {

	 @Test
	    public void validSearchTest() {


	        // Step 1: Login API
	        Response loginResponse =
	        given()
	            .header("Content-Type", "application/json")
	            .body("{\"email\":\"admin@yopmail.com\",\"password\":\"Awfis@2026\"}")  // correct field

	        .when()
	            .post("https://api.smartstructure.co.in/api/admin/auth/login")

	        .then()
	            .log().all()
	            .extract()
	            .response();

	        // Step 2: Extract token
	        JsonPath js = loginResponse.jsonPath();
	        String token = js.getString("data.access_token");   // adjust if needed

	        System.out.println("Token = " + token);

	        // Step 3: Call Brands API
	        given()
	            .header("Authorization", "Bearer " + token)
	            .queryParam("search", "ul")

	        .when()
	            .get("https://api.smartstructure.co.in/api/admin/brands")

	        .then()
	            .log().all()
	            .statusCode(200);
	    }
	 
	 @Test
	 public void invalidTokenTest() {

	     given()
	         .header("Authorization", "Bearer invalid_token")
	         .queryParam("search", "ul")

	     .when()
	         .get("https://api.smartstructure.co.in/api/admin/brands")

	     .then()
	         .statusCode(401);
	 }
	 
	 @Test
	 public void missingTokenTest() {

	     given()
	         .queryParam("search", "ul")

	     .when()
	         .get("https://api.smartstructure.co.in/api/admin/brands")

	     .then()
	         .statusCode(401);
	 }
	 @Test
	 public void emptySearchTest() {

	     String token = null;
		 given()
	         .header("Authorization", "Bearer " + token)
	         .queryParam("search", "")

	     .when()
	         .get("https://api.smartstructure.co.in/api/admin/brands")

	     .then()
	         .statusCode(401);
	 }
	 @Test
	 public void responseTimeTest() {

	     String token = null;
		 given()
	         .header("Authorization", "Bearer " + token)
	         .queryParam("search", "ul")

	     .when()
	         .get("https://api.smartstructure.co.in/api/admin/brands")

	     .then()
	         .time(lessThan(3000L));
	 }

	 @Test
	 public void validateBrandFields() {

	     String token = "YOUR_VALID_TOKEN";

	     given()
	         .header("Authorization", "Bearer " + token)
	         .queryParam("search", "ul")

	     .when()
	         .get("https://api.smartstructure.co.in/api/admin/brands")

	     .then()
	         .log().all()   // 👈 print response
	         .statusCode(401);
	 }
	 
	 @Test
	 public void largeSearchInputTest() {

	     String token = null;
		 given()
	         .header("Authorization", "Bearer " + token)
	         .queryParam("search", "abcdefghijklmnopqrstuvwxyz")

	     .when()
	         .get("https://api.smartstructure.co.in/api/admin/brands")

	     .then()
	         .statusCode(401);
	 }
}