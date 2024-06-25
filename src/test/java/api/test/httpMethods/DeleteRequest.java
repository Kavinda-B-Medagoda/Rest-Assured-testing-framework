package api.test.httpMethods;
import api.test.ApiRequest;
import com.kbm.RestAssured.ConfigLoader;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteRequest implements ApiRequest {
    @Override
    public Response executeRequest(ConfigLoader.TestCase testCase) {
        try{
            return given()
                    .when().delete(testCase.getUrl())
                    .then().log().all()
                    .extract().response();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
