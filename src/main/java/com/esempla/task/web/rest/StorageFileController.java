package com.esempla.task.web.rest;

import com.esempla.task.service.StorageFileService;
import com.esempla.task.service.dto.FileRequest;
import com.esempla.task.service.dto.StorageFileResponse;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/file")
public class StorageFileController {

    private final StorageFileService storageFileService;

    public StorageFileController(StorageFileService storageFileService) {
        this.storageFileService = storageFileService;
    }

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        storageFileService.uploadFile(file);
    }


    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> getFile(@RequestBody FileRequest fileRequest) throws IOException {
        return ResponseEntity.ok()
                .body(IOUtils.toByteArray(storageFileService.downloadFile(fileRequest.getFileName())));
    }

    @DeleteMapping("/delete")
    public void deleteFile(@RequestBody FileRequest fileRequest) throws Exception {
        storageFileService.deleteFile(fileRequest.getFileName());
    }


    @GetMapping("/view-metadata")
    public ResponseEntity<List<StorageFileResponse>> getAllFiles() {
        return ResponseEntity.ok(storageFileService.getAllFiles());
    }

}
