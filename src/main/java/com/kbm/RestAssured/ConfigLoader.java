package com.kbm.RestAssured;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {
    public List<TestCase> loadTestCases() {
        List<TestCase> testCases = new ArrayList<>();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("testcases.csv");
             CSVReader csvReader = new CSVReader(new InputStreamReader(input))) {
            List<String[]> lines = csvReader.readAll();
            // Skip header
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i);
                String id = parts[0].trim();
                String url = parts[1].trim();
                String method = parts[2].trim();
                String payload = parts[3].trim();
                int expectedResponseCode = Integer.parseInt(parts[4].trim());
                String testName = parts[5].trim();
                String expectedResponseBody = parts[6].trim();
                boolean requiresAuthentication = Boolean.parseBoolean(parts[7].trim());
                String authToken = parts[8].trim();
                testCases.add(new TestCase(id, url, method, payload, expectedResponseCode, testName, expectedResponseBody, requiresAuthentication, authToken));
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return testCases;
    }


    public static class TestCase {
        private String id;
        private String url;
        private String method;
        private String payload;
        private int expectedResponseCode;
        private String testName;
        private String expectedResponseBody;
        private boolean requiresAuthentication;
        private String authToken;

        public TestCase(String id, String url, String method, String payload, int expectedResponseCode, String testName, String expectedResponseBody, boolean requiresAuthentication, String authToken) {
            this.id = id;
            this.url = url;
            this.method = method;
            this.payload = payload;
            this.expectedResponseCode = expectedResponseCode;
            this.testName = testName;
            this.expectedResponseBody = expectedResponseBody;
            this.requiresAuthentication = requiresAuthentication;
            this.authToken = authToken;
        }

        public boolean requiresAuthentication() {
            return requiresAuthentication;
        }

        public String getAuthToken() {
            return authToken;
        }
        public String getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }

        public String getMethod() {
            return method;
        }

        public String getPayload() {
            return payload;
        }

        public int getExpectedResponseCode() {
            return expectedResponseCode;
        }

        public String getTestName() {
            return testName;
        }

        public String getExpectedResponseBody() {
            return expectedResponseBody;
        }
    }
}
