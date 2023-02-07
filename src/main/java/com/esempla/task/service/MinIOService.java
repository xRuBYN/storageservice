package com.esempla.task.service;


import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import com.esempla.task.config.MinioProperties;
import com.esempla.task.domain.StorageFile;
import com.esempla.task.domain.UserReservation;
import com.esempla.task.repository.StorageFileRepository;
import com.esempla.task.repository.UserRepository;
import com.esempla.task.repository.UserReservationRepository;
import com.esempla.task.service.dto.FileRequest;
import com.esempla.task.service.dto.UserReservationResponse;
import io.minio.*;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

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

    public InputStream downloadFile(String name) {
        InputStream stream;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(name)
                .build());
        } catch (Exception e) {
            log.debug("Happened error when get list objects from minio: " + e);
            return null;
        }
        return stream;
    }

    public void deleteFile(String name) throws Exception {

        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
            .bucket(minioProperties.getBucketName())
            .object(name)
            .build();
        minioClient.removeObject(removeObjectArgs);
        log.debug("Object " + name + " removed.");

    }

}
