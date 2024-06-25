package api.test;

import com.kbm.RestAssured.ConfigLoader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class ApiTest {
    private static ConfigLoader config;

    @BeforeClass
    public void setup() {
        config = new ConfigLoader();
    }

    @DataProvider(name = "testCases")
    public Iterator<Object[]> getTestCases() {
        List<ConfigLoader.TestCase> testCases = config.loadTestCases();
        return testCases.stream().map(testCase -> new Object[]{testCase}).iterator();
    }

    @Test(dataProvider = "testCases")
    public void testApi(ConfigLoader.TestCase testCase) {
        Response response = null;

        io.restassured.specification.RequestSpecification request = given().header("Content-Type", "application/json");

        switch (testCase.getMethod().toUpperCase()) {
            case "POST":
                response = request.body(testCase.getPayload())
                        .when()
                        .post(testCase.getUrl())
                        .then()
                        .log().all()
                        .extract().response();
                break;
            case "GET":
                response = request.when()
                        .get(testCase.getUrl())
                        .then()
                        .log().all()
                        .extract().response();
                break;
            case "PUT":
                response = request.body(testCase.getPayload())
                        .when()
                        .put(testCase.getUrl())
                        .then()
                        .log().all()
                        .extract().response();
                break;
            case "DELETE":
                response = request.when()
                        .delete(testCase.getUrl())
                        .then()
                        .log().all()
                        .extract().response();
                break;
            case "PATCH":
                response = request.body(testCase.getPayload())
                        .when()
                        .patch(testCase.getUrl())
                        .then()
                        .log().all()
                        .extract().response();
                break;
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + testCase.getMethod());
        }

        assertEquals(testCase.getExpectedResponseCode(), response.getStatusCode());
    }
}
