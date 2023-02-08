package com.esempla.task.service;

import com.esempla.task.domain.StorageFile;
import com.esempla.task.domain.UserReservation;
import com.esempla.task.repository.StorageFileRepository;
import com.esempla.task.repository.UserRepository;
import com.esempla.task.repository.UserReservationRepository;
import com.esempla.task.service.dto.StorageFileResponse;
import com.esempla.task.utils.StorageFileServiceUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StorageFileService {
    private final StorageFileRepository storageFileRepository;
    private final UserReservationRepository userReservationRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;

    private final StorageFileServiceUtils storageFileServiceUtils;

    public StorageFileService(StorageFileRepository storageFileRepository, UserReservationRepository userReservationRepository, UserRepository userRepository, StorageService storageService, StorageFileServiceUtils storageFileServiceUtils) {
        this.storageFileRepository = storageFileRepository;
        this.userReservationRepository = userReservationRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
        this.storageFileServiceUtils = storageFileServiceUtils;
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

        String userLogin = com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow();

        UserReservation userReservation = userReservationRepository
            .findUserReservationByUser(userRepository.findOneByLogin(userLogin).orElseThrow());

        if (storageFileServiceUtils.checkActivation()) {

            if (storageFileServiceUtils.checkUsedSize(multipartFile.getSize())) {

                String path = storageFileServiceUtils
                    .generateUniqPath(multipartFile.getOriginalFilename(), userLogin);

                storageService
                    .uploadFile(path, multipartFile.getInputStream(), multipartFile.getSize());

                storageFile.setName(multipartFile.getOriginalFilename());
                storageFile.setPath(path);
                storageFile.setSize(multipartFile.getSize());
                storageFile.setMimeType(multipartFile.getContentType());
                storageFile.setCreatedDate(Instant.now());
                storageFile.setCreatedBy(userLogin);

                saveFile(storageFile);

                userReservation.setUsedSize((userReservation.getUsedSize() + multipartFile.getSize()));

            } else {
                throw new StorageSizeException();
            }
        } else {
            throw new ActivationExpiredException("You can't upload a file because your status is inactive");
        }
        userReservationRepository.save(userReservation);

    }

    public InputStream downloadFile(Long id) {
        if (storageFileServiceUtils.checkActivation()) {
            StorageFile storageFile = storageFileRepository
                .findStorageFileById(id);

            if (Objects.isNull(storageFile)) {
                throw new FileNotFoundException("File don't exist with type and name");
            }
            return storageService.downloadFile(storageFile.getPath());
        }

        throw new ActivationExpiredException("You download delete a file because your status is inactive");
    }

    public void deleteFile(Long id) throws Exception {
        UserReservation userReservation = userReservationRepository
            .findUserReservationByUser(userRepository.findOneByLogin(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());

        StorageFile storageFile = storageFileRepository
            .findStorageFileById(id);
        if(storageFileServiceUtils.checkActivation()) {
            if (Objects.nonNull(storageFile)) {

                storageService.deleteFile(storageFile.getPath());

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

}
