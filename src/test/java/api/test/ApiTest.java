package api.test;

import api.test.requestFactory.ApiRequestFactory;
import com.kbm.RestAssured.ConfigLoader;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.ITest;
import org.testng.annotations.DataProvider;
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

    public static class ApiTestMethod implements ITest {
        private final ConfigLoader.TestCase testCase;

        public ApiTestMethod(ConfigLoader.TestCase testCase) {
            this.testCase = testCase;
        }

        @DataProvider(name = "testData")
        public Object[][] getTestData() {
            return new Object[][]{{testCase}};
        }

        @Test(dataProvider = "testData")
        public void testApi(ConfigLoader.TestCase testCase) throws JSONException {
            try{

            }catch (Exception e){
                Allure.step("Test failed: " + e.getMessage());
                throw new AssertionError("Test case failed: " + testCase.getTestName(), e);
            }
            ApiRequest request = ApiRequestFactory.getRequest(testCase.getMethod());
            RequestSpecification requestSpec = ApiRequestFactory.getRequestSpecification(testCase);
            Response response = request.executeRequest(testCase, requestSpec);
            assertEquals(response.getStatusCode(), testCase.getExpectedResponseCode());

            if (testCase.getExpectedResponseBody() != null && !testCase.getExpectedResponseBody().isEmpty()) {
                JSONAssert.assertEquals(testCase.getExpectedResponseBody(), response.getBody().asString(), false);
            }

            Allure.step("Status code: " + response.getStatusCode());
        }

        @Override
        public String getTestName() {
            return testCase.getTestName();
        }

        @Override
        public String toString() {
            return testCase.getTestName();
        }
    }
}
