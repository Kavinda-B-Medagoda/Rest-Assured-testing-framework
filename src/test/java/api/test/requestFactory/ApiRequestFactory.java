package api.test.requestFactory;

import api.test.ApiRequest;
import api.test.httpMethods.DeleteRequest;
import api.test.httpMethods.GetRequest;
import api.test.httpMethods.PostRequest;
import api.test.httpMethods.PutRequest;

public class ApiRequestFactory {
    public static ApiRequest getRequest(String method){
        switch (method.toUpperCase()){
            case "GET":
                return new GetRequest();
            case "POST":
                return new PostRequest();
            case "PUT":
                return new PutRequest();
            case "DELETE":
                return new DeleteRequest();
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }
    }
}
