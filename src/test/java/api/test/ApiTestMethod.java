package api.test;

import api.test.addAuthenticationToTestCases.SpecBuilder;
import api.test.httpMethods.ApiRequest;
import api.test.requestFactory.ApiRequestFactory;
import api.test.testExecutionOrder.CustomMethodInterceptor;
import api.test.testExecutionOrder.CustomTestListener;
import com.kbm.RestAssured.ConfigLoader;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listeners({
        CustomTestListener.class,
        CustomMethodInterceptor.class
})
public class ApiTestMethod implements ITest {
//    this class is for set a proper name for the test case and for ad execution of test case
    private static final Logger logger = LoggerFactory.getLogger(ApiTestMethod.class);

    private final ConfigLoader.TestCase testCase;
    private final int priority;

    public ApiTestMethod(int priority, ConfigLoader.TestCase testCase) {
        this.priority = priority;
        this.testCase = testCase;
    }

    public ConfigLoader.TestCase getTestCase() {
        return testCase;
    }

    public int getPriority() {
        return priority;
    }

    @Test
    public void executeTest(ITestContext context) throws JSONException {
        long threadId = Thread.currentThread().getId();
        logger.info("Executing test: {} with priority {} on thread {}", testCase.getTestName(), priority, threadId);
        try {
            ApiRequest request = ApiRequestFactory.getRequest(testCase.getMethod());
            RequestSpecification requestSpec = SpecBuilder.getRequestSpecification(testCase,context);
            Response response = request.executeRequest(testCase, requestSpec);

            // Validate response
            Assert.assertEquals(response.getStatusCode(), testCase.getExpectedResponseCode());

            if (testCase.getExpectedResponseBody() != null && !testCase.getExpectedResponseBody().isEmpty()) {
                JSONAssert.assertEquals(testCase.getExpectedResponseBody(), response.getBody().asString(), false);
            }

            Allure.step("Status code: " + response.getStatusCode());
        } catch (JSONException e) {
            logger.error("JSON validation failed: {}", testCase.getTestName(), e);
            Allure.step("Test failed: " + e.getMessage());
            Assert.fail("JSON validation failed: " + testCase.getTestName(), e);
        } catch (AssertionError e) {
            logger.error("Assertion failed: {}", testCase.getTestName(), e);
            Allure.step("Test failed: " + e.getMessage());
            Assert.fail("Assertion failed: " + testCase.getTestName(), e);
        } catch (Exception e) {
            logger.error("Test case failed: {}", testCase.getTestName(), e);
            Allure.step("Test failed: " + e.getMessage());
            Assert.fail("Test case failed: " + testCase.getTestName(), e);
        }
    }

    @Override
    public String getTestName() {
        return testCase.getTestName() + " with priority " + testCase.getPriority();
    }
}
