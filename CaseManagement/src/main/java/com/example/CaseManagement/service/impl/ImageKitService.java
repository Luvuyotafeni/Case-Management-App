package com.example.CaseManagement.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Service
public class ImageKitService {

    @Value("${imagekit.private.key}")
    private String privateKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String uploadFile(MultipartFile file, Long caseId) throws IOException {
        // Generate a unique filename
        String fileName = "case_" + caseId + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Use only the private key for authentication
        String authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((privateKey + ":").getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", authHeader);

        // Set up request body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource()); // Send file directly
        body.add("fileName", fileName);
        body.add("folder", "/cases/" + caseId);

        // Create request entity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send request to ImageKit API
        ResponseEntity<String> response = restTemplate.exchange(
                "https://upload.imagekit.io/api/v1/files/upload",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Parse response
        if (response.getStatusCode() == HttpStatus.OK) {
            ImageKitResponse imageKitResponse = objectMapper.readValue(response.getBody(), ImageKitResponse.class);
            return imageKitResponse.getUrl();
        } else {
            throw new RuntimeException("Failed to upload image: " + response.getBody());
        }
    }

    // Response class for ImageKit
    private static class ImageKitResponse {
        @JsonProperty("url")
        private String url;

        @JsonProperty("fileId") // Handle fileId if necessary
        private String fileId;

        public String getUrl() {
            return url;
        }

        public String getFileId() {
            return fileId;
        }
    }
}
