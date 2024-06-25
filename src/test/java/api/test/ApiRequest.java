package api.test;

import com.kbm.RestAssured.ConfigLoader;
import io.restassured.response.Response;

public interface ApiRequest {
    Response executeRequest(ConfigLoader.TestCase testCase);
}
