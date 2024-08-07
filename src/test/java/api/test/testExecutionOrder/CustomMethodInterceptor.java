package api.test.testExecutionOrder;

import api.test.ApiTestMethod;
import com.kbm.RestAssured.ConfigLoader;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.Comparator;
import java.util.List;

public class CustomMethodInterceptor implements IMethodInterceptor {
//this class is for run test cases according to the order that defined
    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        // Sort methods based on the priority defined in the ConfigLoader.TestCase
        methods.sort(Comparator.comparingInt(m -> {
            Object instance = m.getInstance();
            if (instance instanceof ApiTestMethod) {
                ConfigLoader.TestCase testCase = ((ApiTestMethod) instance).getTestCase();
                return testCase.getPriority();
            }
            return Integer.MAX_VALUE; // Default to the lowest priority if no match
        }));
        return methods;
    }
}
