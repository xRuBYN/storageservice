package com.esempla.task.web.rest;

import com.esempla.task.service.StorageFileService;
import com.esempla.task.service.StorageService;
import com.esempla.task.service.dto.FileRequest;
import com.esempla.task.service.dto.StorageFileResponse;
import com.esempla.task.service.dto.UserReservationResponse;
import io.minio.errors.*;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/file")
public class StorageFileController {

    private final StorageService storageService;

    private final StorageFileService storageFileService;

    public StorageFileController(StorageService storageService, StorageFileService storageFileService) {
        this.storageService = storageService;
        this.storageFileService = storageFileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UserReservationResponse> uploadFile(@RequestParam("file") MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
       return ResponseEntity.ok(storageService.uploadFile(file));
    }


    @GetMapping(value = "/download")
    public ResponseEntity<Object> getFile(@RequestBody FileRequest fileRequest) throws IOException {
        return ResponseEntity.ok()
            .contentType(MediaType.valueOf(fileRequest.getMediaType()))
            .body(IOUtils.toByteArray(storageService.downloadFile(fileRequest)));
    }

    @DeleteMapping("/delete")
    public void deleteFile(@RequestBody FileRequest fileRequest) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        storageService.deleteFile(fileRequest);
    }


    @GetMapping("/view-metadata")
    public ResponseEntity<List<StorageFileResponse>> getAllFiles() {
        return ResponseEntity.ok(storageFileService.getAllFiles());
    }

}
