package com.task_manager.task_service.services;

import java.io.InputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class FileStorageService {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String key) {
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));
            return key;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public InputStream downloadFile(String key) {
        try {
            return s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }

    public URL getFileUrl(String key) {
        return null;
    }

    public InputStream getTaskAttachment(Long userId, String fileName) {
        String key = "tasks/" + userId + "/" + fileName;
        return downloadFile(key);
    }
}
