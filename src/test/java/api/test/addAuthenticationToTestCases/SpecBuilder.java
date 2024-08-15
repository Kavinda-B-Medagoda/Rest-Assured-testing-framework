package api.test.addAuthenticationToTestCases;

import com.kbm.RestAssured.ConfigLoader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestContext;

import static com.kbm.RestAssured.AuthConfig.*;

public class SpecBuilder {
    //this class is for setting authentications to the test cases
    public static RequestSpecification getRequestSpecification(ConfigLoader.TestCase testCase,ITestContext context) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder()
                .setBaseUri(testCase.getUrl())
                .setContentType(ContentType.JSON);

        if (testCase.getPayload() != null && !testCase.getPayload().isEmpty()) {
            specBuilder.setBody(testCase.getPayload());
        }

        if (testCase.requiresAuthentication()) {
            String token = AuthTokenUtil.getAuthToken(AUTH_URL, USERNAME, PASSWORD);
            specBuilder.addHeader("Authorization", "Bearer " + token);
        }

        return specBuilder.build();
    }
}
