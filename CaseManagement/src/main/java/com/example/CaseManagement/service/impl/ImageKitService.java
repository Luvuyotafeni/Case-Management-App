package com.example.CaseManagement.service.impl;

import com.example.CaseManagement.entity.DocumentEntity;
import com.example.CaseManagement.repository.DocumentRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${imagekit.url.endpoint}")
    private String imageKitEndpoint;

    @Autowired
    private  DocumentRepository documentRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Uploads a file to ImageKit and returns the URL.
     */
    public ImageUploadResponse uploadFile(MultipartFile file, Long caseId) throws IOException {
        String fileName = "case_" + caseId + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((privateKey + ":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", authHeader);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        body.add("fileName", fileName);
        body.add("folder", "/cases/" + caseId);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://upload.imagekit.io/api/v1/files/upload",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            ImageKitResponse imageKitResponse = objectMapper.readValue(response.getBody(), ImageKitResponse.class);
            return new ImageUploadResponse(imageKitResponse.getUrl(), imageKitResponse.getFileId());
        } else {
            throw new RuntimeException("Failed to upload image: " + response.getBody());
        }
    }

    /**
     * Deletes a file from ImageKit using the file ID.
     */
    public String deleteFile(Long documentId) {
        // Retrieve the document from the database
        DocumentEntity document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));

        String fileId = document.getFileId(); // Get fileId from DB

        // Delete file from ImageKit
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((privateKey + ":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.imagekit.io/v1/files/" + fileId,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );

        documentRepository.delete(document);

        return "File deleted successfully from ImageKit and database";

    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ImageKitResponse {
        @JsonProperty("url")
        private String url;

        @JsonProperty("fileId")
        private String fileId;

        public String getUrl() {
            return url;
        }

        public String getFileId() {
            return fileId;
        }
    }

    public class ImageUploadResponse {
        private String fileUrl;
        private String fileId;

        public ImageUploadResponse(String fileUrl, String fileId) {
            this.fileUrl = fileUrl;
            this.fileId = fileId;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public String getFileId() {
            return fileId;
        }
    }

}
