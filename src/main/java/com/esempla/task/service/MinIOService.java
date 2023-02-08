package com.esempla.task.service;



import java.io.InputStream;

import com.esempla.task.config.MinioProperties;
import io.minio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MinIOService implements StorageService{

    private final Logger log = LoggerFactory.getLogger(MinIOService.class);
    private final MinioProperties minioProperties;
    private final MinioClient minioClient;

    public MinIOService(MinioProperties minioProperties,
                        MinioClient minioClient) {

        this.minioProperties = minioProperties;

        this.minioClient = minioClient;
    }



    public void uploadFile(String fileName, InputStream inputStream, Long size) throws Exception {
        minioClient.putObject(PutObjectArgs.builder()
            .bucket(minioProperties.getBucketName())
            .object(fileName)
            .stream(inputStream, size, -1)
            .build());

    }

    public InputStream downloadFile(String path) {
        InputStream stream;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(path)
                .build());
        } catch (Exception e) {
            log.debug("Happened error when get list objects from minio: " + e);
            return null;
        }
        return stream;
    }

    public void deleteFile(String path) throws Exception {

        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
            .bucket(minioProperties.getBucketName())
            .object(path)
            .build();
        minioClient.removeObject(removeObjectArgs);
        log.debug("Object " + path + " removed.");

    }

}
