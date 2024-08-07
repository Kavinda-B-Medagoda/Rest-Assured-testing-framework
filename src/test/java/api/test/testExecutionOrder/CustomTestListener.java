package api.test.testExecutionOrder;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class CustomTestListener implements ITestListener {
//this class is for logging some messages related to the test cases when run test cases
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Starting test: " + result.getName() + " on thread " + Thread.currentThread().getId());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test succeeded: " + result.getName() + " on thread " + Thread.currentThread().getId());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test failed: " + result.getName() + " on thread " + Thread.currentThread().getId());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test skipped: " + result.getName() + " on thread " + Thread.currentThread().getId());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not used
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting suite: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finished suite: " + context.getName());
    }
}
