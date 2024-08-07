package api.test.addAuthenticationToTestCases;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AuthTokenUtil {
    private static String token;

    public static String getAuthToken(String authUrl, String username, String password) {
        if (token == null || isTokenExpired(token)) {
            token = generateNewToken(authUrl, username, password);
        }
        return token;
    }

    private static String generateNewToken(String authUrl, String username, String password) {
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}")
                .post(authUrl);

        return response.jsonPath().getString("token");
    }

    private static boolean isTokenExpired(String token) {
        return false;
    }
}
