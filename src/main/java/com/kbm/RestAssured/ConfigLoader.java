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
                String url = parts[0].trim();
                String method = parts[1].trim();
                String payload = parts[2].trim();
                int expectedResponseCode = Integer.parseInt(parts[3].trim());
                testCases.add(new TestCase(url, method, payload, expectedResponseCode));
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return testCases;
    }

    public static class TestCase {
        private String url;
        private String method;
        private String payload;
        private int expectedResponseCode;

        public TestCase(String url, String method, String payload, int expectedResponseCode) {
            this.url = url;
            this.method = method;
            this.payload = payload;
            this.expectedResponseCode = expectedResponseCode;
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
    }
}
