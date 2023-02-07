package com.esempla.task.service;

import com.esempla.task.domain.StorageFile;
import com.esempla.task.repository.StorageFileRepository;
import com.esempla.task.service.dto.StorageFileResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageFileService {
    private final StorageFileRepository storageFileRepository;

    public StorageFileService(StorageFileRepository storageFileRepository) {
        this.storageFileRepository = storageFileRepository;
    }

    public void saveFile(StorageFile storageFile) {
        storageFileRepository.save(storageFile);
    }


    public List<StorageFileResponse> getAllFiles() {
        String login = com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow();
        List<StorageFileResponse> storageFileResponses = storageFileRepository.findAllByCreatedBy(login)
            .stream()
            .map(StorageFile::toStorageFileResponse)
            .collect(Collectors.toList());

        return storageFileResponses;
    }
}
