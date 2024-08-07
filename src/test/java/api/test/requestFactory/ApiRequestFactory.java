package api.test.requestFactory;

import api.test.httpMethods.ApiRequest;
import api.test.httpMethods.*;

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


}
