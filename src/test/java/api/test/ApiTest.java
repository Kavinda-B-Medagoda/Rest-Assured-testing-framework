package api.test;

import api.test.requestFactory.ApiRequestFactory;
import com.kbm.RestAssured.ConfigLoader;
import io.restassured.response.Response;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class ApiTest {
    private static ConfigLoader config;

    static {
        config = new ConfigLoader();
    }

    @Factory
    public Object[] createTests() {
        List<ConfigLoader.TestCase> testCases = config.loadTestCases();
        Object[] tests = new Object[testCases.size()];
        for (int i = 0; i < testCases.size(); i++) {
            tests[i] = new ApiTestMethod(testCases.get(i));
        }
        return tests;
    }

    public static class ApiTestMethod {
        private final ConfigLoader.TestCase testCase;

        public ApiTestMethod(ConfigLoader.TestCase testCase) {
            this.testCase = testCase;
        }

        @Test
        public void testApi() {
            ApiRequest request = ApiRequestFactory.getRequest(testCase.getMethod());
            Response response = request.executeRequest(testCase);
            assertEquals(response.getStatusCode(),testCase.getExpectedResponseCode());
        }
    }
}
