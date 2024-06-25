package api.test.httpMethods;

import api.test.ApiRequest;
import com.kbm.RestAssured.ConfigLoader;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;


public class GetRequest implements ApiRequest {

    @Override
    public Response executeRequest(ConfigLoader.TestCase testCase) {
       try{
           return given().header("Content-type","application/json")
                   .when().get(testCase.getUrl())
                   .then().log().all()
                   .extract().response();
       }catch (Exception e){
           e.printStackTrace();
           throw e;
       }
    }
}
