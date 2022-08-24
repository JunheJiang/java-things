package com.ljp.spring.batchtest.util;


import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {
	
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String getJsonStringFromEntity(Object object) throws JsonProcessingException{
    	return objectMapper.writeValueAsString(object);
    }
    
    public static  <T extends Object> T getObjectFromJsonString(String json_string, Class klass) throws JsonParseException, JsonMappingException, IOException {  
        return (T) objectMapper.readValue(json_string, klass);  
    }
    
    public static String getJsonStringFromMap(Map map) throws JsonProcessingException{
    	return objectMapper.writeValueAsString(map);
    }
    
    public static  JsonNode getJsonNodeFromJsonString(String json_string) throws JsonParseException, JsonMappingException, IOException {  
        return objectMapper.readTree(json_string);
    }

    
}