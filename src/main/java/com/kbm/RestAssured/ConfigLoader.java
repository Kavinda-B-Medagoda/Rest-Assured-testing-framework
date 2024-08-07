package com.kbm.RestAssured;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ConfigLoader {
    //this class is for load test cases from csv file and create a list
    public List<TestCase> loadTestCases() {
        List<TestCase> testCases = new ArrayList<>();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("JourneyTestCases.csv");
             CSVReader csvReader = new CSVReader(new InputStreamReader(input))) {
            List<String[]> lines = csvReader.readAll();
            // Debugging output to see the contents of each line
            System.out.println("Total lines read: " + lines.size());

            // Skip header
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i);
                System.out.println("Reading line: " + String.join(",", parts));
                if (parts.length < 9) {
                    System.err.println("Skipping line due to insufficient columns: " + String.join(",", parts));
                    continue;
                }
                try {
                    String id = parts[0].trim();
                    String url = parts[1].trim();
                    String method = parts[2].trim();
                    String payload = parts[3].trim();
                    int expectedResponseCode = Integer.parseInt(parts[4].trim());
                    String testName = parts[5].trim();
                    String expectedResponseBody = parts[6].trim();
                    boolean requiresAuthentication = Boolean.parseBoolean(parts[7].trim());
                    int priority = Integer.parseInt(parts[8].trim());

                    TestCase testCase = new TestCase(id, url, method, payload, expectedResponseCode, testName, expectedResponseBody, requiresAuthentication, priority);
                    testCases.add(testCase);

                    // Debugging output to confirm the TestCase object
                    System.out.println("Added test case: " + testCase);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number in line: " + String.join(",", parts));
                    e.printStackTrace();
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        // Sort test cases by priority
        Collections.sort(testCases, Comparator.comparingInt(TestCase::getPriority));
        System.out.println("Test cases sorted by priority.");
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
        private int priority;

        public TestCase() {
        }

        public TestCase(String id, String url, String method, String payload, int expectedResponseCode, String testName, String expectedResponseBody, boolean requiresAuthentication, int priority) {
            this.id = id;
            this.url = url;
            this.method = method;
            this.payload = payload;
            this.expectedResponseCode = expectedResponseCode;
            this.testName = testName;
            this.expectedResponseBody = expectedResponseBody;
            this.requiresAuthentication = requiresAuthentication;
            this.priority = priority;
        }

        public boolean requiresAuthentication() {
            return requiresAuthentication;
        }

        public void setUrl(String url) {
            this.url = url;
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

        public int getPriority() {
            return priority;
        }

        @Override
        public String toString() {
            return "TestCase{" +
                    "id='" + id + '\'' +
                    ", url='" + url + '\'' +
                    ", method='" + method + '\'' +
                    ", payload='" + payload + '\'' +
                    ", expectedResponseCode=" + expectedResponseCode +
                    ", testName='" + testName + '\'' +
                    ", expectedResponseBody='" + expectedResponseBody + '\'' +
                    ", requiresAuthentication=" + requiresAuthentication +
                    ", priority=" + priority +
                    '}';
        }
    }
}
