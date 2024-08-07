package api.test.httpMethods;

import com.kbm.RestAssured.ConfigLoader;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class GetRequest implements ApiRequest {

    @Override
    public Response executeRequest(ConfigLoader.TestCase testCase, RequestSpecification requestSpec) {
       try{
           return given().spec(requestSpec).header("Content-type","application/json")
                   .when().get(testCase.getUrl())
                   .then().log().all()
                   .extract().response();
       }catch (Exception e){
           e.printStackTrace();
           throw e;
       }
    }
}
