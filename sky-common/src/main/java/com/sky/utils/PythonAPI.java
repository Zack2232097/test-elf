package com.sky.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class PythonAPI {
    private static final String BERT_API_URL = "http://localhost:5000/get_sentence_embedding";

    public static double[] getSentenceEmbedding(String sentence) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = "{\"sentence\": \"" + sentence + "\"}";
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(BERT_API_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());
                //获取embedding为键的值,只有一个, 所以size=1
                JsonNode embeddingNode = root.get("embedding");

                //目前是一个一个sentence进行处理, 所以size=1
                double[] embedding = new double[embeddingNode.size()];
                for (int i = 0; i < embeddingNode.size(); i++) {
                    embedding[i] = embeddingNode.get(i).asDouble();
                }
                return embedding;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
