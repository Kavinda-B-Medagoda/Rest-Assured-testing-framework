//package api.test.parallelExecution;
//
//import api.test.httpMethods.ApiRequest;
//import api.test.addAuthenticationToTestCases.SpecBuilder;
//import api.test.requestFactory.ApiRequestFactory;
//import com.kbm.RestAssured.ConfigLoader;
//import io.qameta.allure.Allure;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//import org.json.JSONException;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.testng.Assert;
//import org.testng.ITestContext;
//import org.testng.annotations.Factory;
//import org.testng.annotations.Test;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//public class ApiTestParallel {
////    this class is for parallel test case execution
//
//    private static final Logger logger = LoggerFactory.getLogger(ApiTestParallel.class);
//    private final ConfigLoader.TestCase testCase;
//
//    public ApiTestParallel(ConfigLoader.TestCase testCase) {
//        this.testCase = testCase;
//    }
//
//    @Factory
//    public static Object[] createInstances() throws IOException {
//        List<ConfigLoader.TestCase> testCases = new ConfigLoader().loadTestCases();
//        return testCases.stream().map(ApiTestParallel::new).toArray();
//    }
//
//    @Test
//    public void runParallelTests(ITestContext context) throws IOException {
//        List<ConfigLoader.TestCase> testCases = new ConfigLoader().loadTestCases();
//        ExecutorService executor = Executors.newFixedThreadPool(testCases.size());
//
//        for (ConfigLoader.TestCase testCase : testCases) {
//            Runnable testTask = new TestTask(testCase, context);
//            executor.submit(testTask);
//        }
//
//        executor.shutdown();
//        try {
//            if (!executor.awaitTermination(60, TimeUnit.MINUTES)) {
//                executor.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            executor.shutdownNow();
//        }
//    }
//
//    private static class TestTask implements Runnable {
//        private final ConfigLoader.TestCase testCase;
//        private final ITestContext context;
//
//        public TestTask(ConfigLoader.TestCase testCase, ITestContext context) {
//            this.testCase = testCase;
//            this.context = context;
//        }
//
//        @Override
//        public void run() {
//            long threadId = Thread.currentThread().getId();
//            logger.info("Executing test: {} on thread {}", testCase.getTestName(), threadId);
//
//            try {
//                ApiRequest request = ApiRequestFactory.getRequest(testCase.getMethod());
//                RequestSpecification requestSpec = SpecBuilder.getRequestSpecification(testCase, context);
//                Response response = request.executeRequest(testCase, requestSpec);
//
//                // Log response details
//                logger.info("Response Status Code: {}", response.getStatusCode());
//                logger.info("Response Body: {}", response.getBody().asString());
//
//                // Validate response
//                if (testCase.getExpectedResponseCode() != response.getStatusCode()) {
//                    logger.error("Expected status code {} but got {}", testCase.getExpectedResponseCode(), response.getStatusCode());
//                    Allure.step("Expected status code " + testCase.getExpectedResponseCode() + " but got " + response.getStatusCode());
//                }
//                Assert.assertEquals(response.getStatusCode(), testCase.getExpectedResponseCode());
//
//                if (testCase.getExpectedResponseBody() != null && !testCase.getExpectedResponseBody().isEmpty()) {
//                    JSONAssert.assertEquals(testCase.getExpectedResponseBody(), response.getBody().asString(), false);
//                }
//
//                Allure.step("Status code: " + response.getStatusCode());
//                logger.info("Test '{}' passed on thread ID: {}", testCase.getTestName(), threadId);
//            } catch (JSONException e) {
//                logger.error("JSON validation failed: {}", testCase.getTestName(), e);
//                Allure.step("Test failed: " + e.getMessage());
//                Assert.fail("JSON validation failed: " + testCase.getTestName(), e);
//            } catch (AssertionError e) {
//                logger.error("Assertion failed: {}", testCase.getTestName(), e);
//                Allure.step("Test failed: " + e.getMessage());
//                Assert.fail("Assertion failed: " + testCase.getTestName(), e);
//            } catch (Exception e) {
//                logger.error("Test case failed: {}", testCase.getTestName(), e);
//                Allure.step("Test failed: " + e.getMessage());
//                Assert.fail("Test case failed: " + testCase.getTestName(), e);
//            }
//        }
//    }
//}
