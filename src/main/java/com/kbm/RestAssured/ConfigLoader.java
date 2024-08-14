package com.kbm.RestAssured;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ConfigLoader {
    // This class is for loading test cases from JSON or YAML files and creating a list

    // Method to load test cases from either a JSON or YAML file
    public List<TestCase> loadTestCases(String fileName) {
        List<TestCase> testCases = new ArrayList<>();

        if (fileName.endsWith(".json")) {
            testCases = loadJsonTestCases(fileName);
        } else if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
            testCases = loadYamlTestCases(fileName);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileName);
        }

        Collections.sort(testCases, Comparator.comparingInt(TestCase::getPriority));
        System.out.println("Test cases sorted by priority.");
        return testCases;
    }

    // Method to load test cases from a JSON file using Jackson
    private List<TestCase> loadJsonTestCases(String fileName) {
        List<TestCase> testCases = new ArrayList<>();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.err.println("Could not find the file: " + fileName);
                return Collections.emptyList();
            }

            ObjectMapper objectMapper = new ObjectMapper();
            testCases = objectMapper.readValue(input, new TypeReference<List<TestCase>>() {});

            for (TestCase testCase : testCases) {
                System.out.println("Added test case: " + testCase);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testCases;
    }

    // Method to load test cases from a YAML file using SnakeYAML
    private List<TestCase> loadYamlTestCases(String fileName) {
        List<TestCase> testCases = new ArrayList<>();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.err.println("Could not find the file: " + fileName);
                return Collections.emptyList();
            }

            Yaml yaml = new Yaml();
            List<Map<String, Object>> yamlData = yaml.load(input);

            // Map YAML data to TestCase objects
            ObjectMapper objectMapper = new ObjectMapper();
            testCases = objectMapper.convertValue(yamlData, new TypeReference<List<TestCase>>() {});

            for (TestCase testCase : testCases) {
                System.out.println("Added test case: " + testCase);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testCases;
    }

    // TestCase class to store information about each test case
    public static class TestCase {
        private int id;
        private String url;
        private String method;
        @JsonDeserialize(using = JsonAsStringDeserializer.class)
        private String payload;
        private int expectedResponseCode;
        private String testName;

        @JsonDeserialize(using = JsonAsStringDeserializer.class)
        private String expectedResponseBody;
        private boolean requiresAuthentication;
        private String authToken; // Added authToken field
        private int priority;
        private Map<String, String> saveResponse; // Added saveResponse field

        public TestCase() {
        }

        // Getters and Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        public int getExpectedResponseCode() {
            return expectedResponseCode;
        }

        public void setExpectedResponseCode(int expectedResponseCode) {
            this.expectedResponseCode = expectedResponseCode;
        }

        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }

        public String getExpectedResponseBody() {
            return expectedResponseBody;
        }

        public void setExpectedResponseBody(String expectedResponseBody) {
            this.expectedResponseBody = expectedResponseBody;
        }

        public boolean requiresAuthentication() {
            return requiresAuthentication;
        }

        public void setRequiresAuthentication(boolean requiresAuthentication) {
            this.requiresAuthentication = requiresAuthentication;
        }

        public String getAuthToken() { // New getter for authToken
            return authToken;
        }

        public void setAuthToken(String authToken) { // New setter for authToken
            this.authToken = authToken;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public Map<String, String> getSaveResponse() { // New getter for saveResponse
            return saveResponse;
        }

        public void setSaveResponse(Map<String, String> saveResponse) { // New setter for saveResponse
            this.saveResponse = saveResponse;
        }

        @Override
        public String toString() {
            return "TestCase{" +
                    "id=" + id +
                    ", url='" + url + '\'' +
                    ", method='" + method + '\'' +
                    ", payload='" + payload + '\'' +
                    ", expectedResponseCode=" + expectedResponseCode +
                    ", testName='" + testName + '\'' +
                    ", expectedResponseBody='" + expectedResponseBody + '\'' +
                    ", requiresAuthentication=" + requiresAuthentication +
                    ", authToken='" + authToken + '\'' +
                    ", priority=" + priority +
                    ", saveResponse=" + saveResponse +
                    '}';
        }
    }
}
