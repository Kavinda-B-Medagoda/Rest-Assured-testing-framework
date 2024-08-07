package api.test.httpMethods;

import com.kbm.RestAssured.ConfigLoader;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public interface ApiRequest {
    Response executeRequest(ConfigLoader.TestCase testCase, RequestSpecification requestSpec);
}
