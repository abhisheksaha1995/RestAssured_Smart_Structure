package resrassuredTests;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;


public class BanenerAPI {
	
	@Test
	  public void getBanners_StatusCode() {

        given()
            .log().all()
        .when()
            .get("https://smartstructureapi.myvtd.site/api/admin/banners")
        .then()
            .log().all()
            .statusCode(401);
    }
}


