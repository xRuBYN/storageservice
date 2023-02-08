package com.esempla.task.web.rest;

import com.esempla.task.service.StorageFileService;
import com.esempla.task.service.dto.FileRequest;
import com.esempla.task.service.dto.StorageFileResponse;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/file")
public class StorageFileController {

    private final StorageFileService storageFileService;

    private final Logger log = LoggerFactory.getLogger(UserReservationController.class);
    public StorageFileController(StorageFileService storageFileService) {
        this.storageFileService = storageFileService;
    }

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        log.debug("Upload file with name " + file.getOriginalFilename());
        storageFileService.uploadFile(file);
    }


    @GetMapping(value = "/download/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) throws IOException {
        log.debug("Download a file with id " + id);
        return ResponseEntity.ok()
                .body(IOUtils.toByteArray(storageFileService.downloadFile(id)));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFile(@PathVariable Long id) throws Exception {
        log.debug("Delete a file with id " + id);
        storageFileService.deleteFile(id);
    }


    @GetMapping("/view-metadata")
    public ResponseEntity<List<StorageFileResponse>> getAllFiles() {
        log.debug("Show metadata");
        return ResponseEntity.ok(storageFileService.getAllFiles());
    }

}
