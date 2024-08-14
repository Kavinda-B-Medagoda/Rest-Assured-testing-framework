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

import java.util.Map;

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
    public void executeTest(ITestContext context) {
        try {
            // Replace placeholders in URL and payload
            String url = replacePlaceholders(testCase.getUrl(), context);
            String payload = replacePlaceholders(testCase.getPayload(), context);

            // Create RequestSpecification with updated URL and payload
            RequestSpecification requestSpec = SpecBuilder.getRequestSpecification(testCase, context)
                    .baseUri(url)
                    .body(payload);

            // Execute request using ApiRequestFactory
            ApiRequest request = ApiRequestFactory.getRequest(testCase.getMethod());
            Response response = request.executeRequest(testCase, requestSpec);

            // Validate response
            Assert.assertEquals(response.getStatusCode(), testCase.getExpectedResponseCode(),
                    "Expected status code " + testCase.getExpectedResponseCode() + " but got " + response.getStatusCode());

            if (testCase.getExpectedResponseBody() != null && !testCase.getExpectedResponseBody().isEmpty()) {
                JSONAssert.assertEquals(testCase.getExpectedResponseBody(), response.getBody().asString(), false);
            }

            // Save response data if needed
            saveResponseData(testCase, response, context);

            Allure.step("Status code: " + response.getStatusCode());
        } catch (JSONException e) {
            logger.error("JSON validation failed for test: {}", testCase.getTestName(), e);
            Allure.step("Test failed due to JSON validation: " + e.getMessage());
            Assert.fail("JSON validation failed: " + testCase.getTestName(), e);
        } catch (AssertionError e) {
            logger.error("Assertion failed for test: {}", testCase.getTestName(), e);
            Allure.step("Test failed due to assertion error: " + e.getMessage());
            Assert.fail("Assertion failed: " + testCase.getTestName(), e);
        } catch (Exception e) {
            logger.error("Test case failed: {}", testCase.getTestName(), e);
            Allure.step("Test failed due to unexpected error: " + e.getMessage());
            Assert.fail("Test case failed: " + testCase.getTestName(), e);
        }
    }

    @Override
    public String getTestName() {
        return testCase.getTestName() + " with priority " + testCase.getPriority();
    }

    // Helper method to replace placeholders in URL and payload
    private String replacePlaceholders(String text, ITestContext context) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        for (String key : context.getAttributeNames()) {
            String placeholder = "{{" + key + "}}";
            if (text.contains(placeholder)) {
                text = text.replace(placeholder, context.getAttribute(key).toString());
            }
        }

        return text;
    }

    // Helper method to save response data to the context
    private void saveResponseData(ConfigLoader.TestCase testCase, Response response, ITestContext context) {
        if (testCase.getSaveResponse() != null && !testCase.getSaveResponse().isEmpty()) {
            for (Map.Entry<String, String> entry : testCase.getSaveResponse().entrySet()) {
                String jsonPath = entry.getKey();
                String contextKey = entry.getValue();
                String value = response.jsonPath().getString(jsonPath);
                context.setAttribute(contextKey, value);
            }
        }
    }
}
