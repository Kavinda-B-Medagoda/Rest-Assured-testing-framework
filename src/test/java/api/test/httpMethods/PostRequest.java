package api.test.httpMethods;

import com.kbm.RestAssured.ConfigLoader;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class PostRequest implements ApiRequest {
    @Override
    public Response executeRequest(ConfigLoader.TestCase testCase, RequestSpecification requestSpec) {
       try{
           return given().spec(requestSpec).header("Content-Type", "application/json")
                   .body(testCase.getPayload())
                   .when()
                   .post(testCase.getUrl())
                   .then()
                   .log().all()
                   .extract().response();
       }catch (Exception e){
           e.printStackTrace();
           throw e;
       }
    }
}
