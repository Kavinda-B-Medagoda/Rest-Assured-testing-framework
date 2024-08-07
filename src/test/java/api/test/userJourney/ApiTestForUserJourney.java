package api.test.userJourney;

import com.kbm.RestAssured.ConfigLoader;
import org.testng.annotations.Factory;

import java.util.List;

public class ApiTestForUserJourney {
    //this class is for create test cases array with ApiTestMethod objects
    private static ConfigLoader config;
    private ApiTestMethodForUserJourney[] testMethods;

    static {
        config = new ConfigLoader();
    }

    @Factory
    public Object[] createTests() {
        List<ConfigLoader.TestCase> testCases = config.loadTestCases();
        testMethods = new ApiTestMethodForUserJourney[testCases.size()];

        for (int i = 0; i < testCases.size(); i++) {
            ConfigLoader.TestCase testCase = testCases.get(i);
            ApiTestMethodForUserJourney testMethod = new ApiTestMethodForUserJourney(testCase.getPriority(), testCase);
            testMethods[i] = testMethod;
        }

        return testMethods;
    }
}
