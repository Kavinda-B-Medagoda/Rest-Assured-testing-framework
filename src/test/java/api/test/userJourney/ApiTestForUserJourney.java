package api.test.userJourney;

import com.kbm.RestAssured.ConfigLoader;
import org.testng.annotations.Factory;

import java.util.List;

import static com.kbm.RestAssured.AuthConfig.PATH;

public class ApiTestForUserJourney {
    // ConfigLoader instance to load test cases
    private final ConfigLoader config = new ConfigLoader();
    private ApiTestMethodForUserJourney[] testMethods;

    @Factory
    public Object[] createTests() {
        // Load test cases from the configured file path
        List<ConfigLoader.TestCase> testCases = config.loadTestCases(PATH);
        testMethods = new ApiTestMethodForUserJourney[testCases.size()];

        // Create ApiTestMethodForUserJourney instances for each test case
        for (int i = 0; i < testCases.size(); i++) {
            ConfigLoader.TestCase testCase = testCases.get(i);
            ApiTestMethodForUserJourney testMethod = new ApiTestMethodForUserJourney(testCase.getPriority(), testCase);
            testMethods[i] = testMethod;
        }

        return testMethods;
    }
}
