package com.sky.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


public class PythonAPI {
    private static final String BERT_API_URL = "http://localhost:5000/getDIR";
    //用户上传问题就直接提取embedding进行保存
    // 将List<只有question_id和embedding的对象>传给python,
    // python将这些embedding进行聚类, 然后簇中使用HNSW建立DIR
    public static double[] getSentenceEmbedding(String sentence, int id) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = "{\"sentence\": \"" + sentence + "\", \"question_id\": " + id + "}";

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(BERT_API_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());
                //返回一个列表
                JsonNode embeddingNode = root.get("similar_elements");
                //大小为5
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
