package com.esempla.task.service;

import com.esempla.task.domain.StorageFile;
import com.esempla.task.domain.UserReservation;
import com.esempla.task.repository.StorageFileRepository;
import com.esempla.task.repository.UserRepository;
import com.esempla.task.repository.UserReservationRepository;
import com.esempla.task.service.dto.StorageFileResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageFileService {
    private final StorageFileRepository storageFileRepository;

    private final UserReservationRepository userReservationRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;

    public StorageFileService(StorageFileRepository storageFileRepository, UserReservationRepository userReservationRepository, UserRepository userRepository, StorageService storageService) {
        this.storageFileRepository = storageFileRepository;
        this.userReservationRepository = userReservationRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
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

    public void uploadFile(MultipartFile multipartFile) throws Exception {
        StorageFile storageFile = new StorageFile();
        UserReservation userReservation = userReservationRepository
            .findUserReservationByUser(userRepository.findOneByLogin(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());

        if (checkActivation()) {

            if (checkUsedSize((int) multipartFile.getSize())) {

                storageService.uploadFile(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), multipartFile.getSize());

                storageFile.setName(multipartFile.getOriginalFilename());
                storageFile.setPath(multipartFile.getOriginalFilename());
                storageFile.setSize(multipartFile.getSize());
                storageFile.setMimeType(multipartFile.getContentType());
                storageFile.setCreatedDate(Instant.now());
                storageFile.setCreatedBy(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow());

                saveFile(storageFile);

                userReservation.setUsedSize((userReservation.getUsedSize() + multipartFile.getSize()));

            } else {
                userReservation.setActivated(false);
            }
        } else {
            throw new ActivationExpiredException("You can't upload a file because your status is inactive");
        }
        userReservationRepository.save(userReservation);

    }

    public InputStream downloadFile(String name) {
        if (checkActivation()) {
            StorageFile storageFile = storageFileRepository
                .findStorageFileByName(name);

            if (storageFile == null) {
                throw new FileNotFoundException("File don't exist with type and name");
            }
            return storageService.downloadFile(name);
        }

        throw new ActivationExpiredException("You download delete a file because your status is inactive");
    }

    public void deleteFile(String name) throws Exception {
        UserReservation userReservation = userReservationRepository
            .findUserReservationByUser(userRepository.findOneByLogin(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());

        StorageFile storageFile = storageFileRepository
            .findStorageFileByName(name);
        if(checkActivation()) {
            if (storageFile != null) {

                storageService.deleteFile(name);

                userReservation.setUsedSize((userReservation.getUsedSize() - storageFile.getSize()));
                storageFileRepository.delete(storageFile);
                userReservationRepository.save(userReservation);
            } else {
                throw new FileNotFoundException("File was deleted");
            }
        } else {
            throw new ActivationExpiredException("You can delete a file because your status is inactive");
        }

    }




    private boolean checkActivation() {
        UserReservation userReservation = userReservationRepository
            .findUserReservationByUser(userRepository.findOneByLogin(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());
        return userReservation.isActivated();
    }

    private boolean checkUsedSize(Integer size) {
        UserReservation userReservation = userReservationRepository
            .findUserReservationByUser(userRepository.findOneByLogin(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());

        if (userReservation.getTotalSize() >= userReservation.getUsedSize() + size) {
            return true;
        }
        return false;
    }
}
