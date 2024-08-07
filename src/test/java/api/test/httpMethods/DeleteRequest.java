package api.test.httpMethods;
import com.kbm.RestAssured.ConfigLoader;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class DeleteRequest implements ApiRequest {
    @Override
    public Response executeRequest(ConfigLoader.TestCase testCase, RequestSpecification requestSpec) {
        try{
            return given().spec(requestSpec)
                    .when().delete(testCase.getUrl())
                    .then().log().all()
                    .extract().response();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
