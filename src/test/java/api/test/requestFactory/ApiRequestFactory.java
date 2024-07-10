package api.test.requestFactory;

import api.test.ApiRequest;
import api.test.httpMethods.*;
import com.kbm.RestAssured.ConfigLoader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiRequestFactory {
    public static ApiRequest getRequest(String method) {
        switch (method.toUpperCase()) {
            case "GET":
                return new GetRequest();
            case "POST":
                return new PostRequest();
            case "PUT":
                return new PutRequest();
            case "DELETE":
                return new DeleteRequest();
            case "PATCH":
                return new PatchRequest();
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }
    }

    public static RequestSpecification getRequestSpecification(ConfigLoader.TestCase testCase) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder()
                .setBaseUri(testCase.getUrl())
                .setContentType(ContentType.JSON);

        if (testCase.getPayload() != null && !testCase.getPayload().isEmpty()) {
            specBuilder.setBody(testCase.getPayload());
        }

        if (testCase.requiresAuthentication()) {
            specBuilder.addHeader("Authorization", "Bearer " + testCase.getAuthToken());
        }

        return specBuilder.build();
    }
}
