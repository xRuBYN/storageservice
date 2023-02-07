package com.esempla.task.service;

import com.esempla.task.service.dto.FileRequest;
import com.esempla.task.service.dto.UserReservationResponse;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface StorageService {

    void uploadFile(String fileName, InputStream inputStream, Long size) throws Exception;

    InputStream downloadFile(String name);

    void deleteFile(String name) throws Exception;

}
