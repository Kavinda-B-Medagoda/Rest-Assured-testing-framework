package api.test.userJourney;

import api.test.addAuthenticationToTestCases.SpecBuilder;
import api.test.httpMethods.ApiRequest;
import api.test.requestFactory.ApiRequestFactory;
import api.test.testExecutionOrder.CustomMethodInterceptorForUserJourney;
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
        CustomMethodInterceptorForUserJourney.class
})
public class ApiTestMethodForUserJourney implements ITest {
    private static final Logger logger = LoggerFactory.getLogger(ApiTestMethodForUserJourney.class);
    private final ConfigLoader.TestCase testCase;
    private final int priority;

    public ApiTestMethodForUserJourney(int priority, ConfigLoader.TestCase testCase) {
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
            // Store projectID in context if available

            // Replace path parameter {id} with actual value in the URL if present
            String url = testCase.getUrl();
            if (url.contains("id")) {
                String projectId = (String) context.getAttribute("projectID");
                if (projectId != null) {
                    url = url.replace("{id}", projectId);
                } else {
                    throw new IllegalArgumentException("projectID not found in context");
                }
            }

            // Create RequestSpecification with updated URL
            RequestSpecification requestSpec = SpecBuilder.getRequestSpecification(testCase, context);
            requestSpec.baseUri(url);

            ApiRequest request = ApiRequestFactory.getRequest(testCase.getMethod());
            Response response = request.executeRequest(testCase, requestSpec);

            // Validate response
            Assert.assertEquals(response.getStatusCode(), testCase.getExpectedResponseCode());

            if (testCase.getExpectedResponseBody() != null && !testCase.getExpectedResponseBody().isEmpty()) {
                JSONAssert.assertEquals(testCase.getExpectedResponseBody(), response.getBody().asString(), false);
            }
            if (testCase.getTestName().equals("project_save")) {
                String projectId = response.jsonPath().getString("projectID");
                context.setAttribute("projectID", projectId);
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
