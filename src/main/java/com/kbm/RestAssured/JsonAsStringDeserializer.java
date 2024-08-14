package com.kbm.RestAssured;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonAsStringDeserializer extends JsonDeserializer<String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        // Convert the JSON structure to a JSON string
        return objectMapper.writeValueAsString(p.readValueAsTree());
    }
}

