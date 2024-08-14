package api.test;

import com.kbm.RestAssured.ConfigLoader;
import org.testng.annotations.Factory;

import java.util.List;

import static com.kbm.RestAssured.AuthConfig.PATH;

public class ApiTest {
//this class is for create test cases array with ApiTestMethod objects
    private static ConfigLoader config;
    private ApiTestMethod[] testMethods;

    static {
        config = new ConfigLoader();
    }

    @Factory
    public Object[] createTests() {
        List<ConfigLoader.TestCase> testCases = config.loadTestCases(PATH);
        testMethods = new ApiTestMethod[testCases.size()];

        for (int i = 0; i < testCases.size(); i++) {
            ConfigLoader.TestCase testCase = testCases.get(i);
            ApiTestMethod testMethod = new ApiTestMethod(testCase.getPriority(), testCase);
            testMethods[i] = testMethod;
        }

        return testMethods;
    }
}
