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
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

@Service
public class MinIOService implements StorageService{

    private final StorageFileService storageFileService;
    private final MinioProperties minioProperties;
    private final UserReservationRepository userReservationRepository;
    private final UserRepository userRepository;
    private final StorageFileRepository storageFileRepository;
    private final MinioClient minioClient;
    public MinIOService(StorageFileService storageFileService, MinioProperties minioProperties,
                        UserReservationRepository userReservationRepository,
                        UserRepository userRepository,
                        StorageFileRepository storageFileRepository,
                        MinioClient minioClient) {
        this.storageFileService = storageFileService;
        this.minioProperties = minioProperties;
        this.userReservationRepository = userReservationRepository;
        this.userRepository = userRepository;
        this.storageFileRepository = storageFileRepository;
        this.minioClient = minioClient;
    }



    public UserReservationResponse uploadFile(MultipartFile multipartFile) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        StorageFile storageFile = new StorageFile();
        UserReservation userReservation = userReservationRepository
            .findUserReservationByUser(userRepository.findOneByLogin(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());

        if (checkActivation()) {

            if (checkUsedSize((int) multipartFile.getSize())) {

                minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(multipartFile.getOriginalFilename())
                    .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                    .build());

                storageFile.setName(multipartFile.getOriginalFilename());
                storageFile.setPath(multipartFile.getOriginalFilename());
                storageFile.setSize(multipartFile.getSize());
                storageFile.setMimeType(multipartFile.getContentType());
                storageFile.setCreatedDate(Instant.now());
                storageFile.setCreatedBy(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow());

                storageFileService.saveFile(storageFile);

                userReservation.setUsedSize((userReservation.getUsedSize() + multipartFile.getSize()));

            } else {
                userReservation.setActivated(false);
            }
        } else {
            throw new ActivationExpiredException("You can't upload a file because your status is inactive");
        }
        userReservationRepository.save(userReservation);

        return userReservation.toUserReservationResponse();
    }

    public InputStream downloadFile(FileRequest fileRequest) {
        if (checkActivation()) {
            InputStream stream;

            StorageFile storageFile = storageFileRepository
                .findStorageFileByNameAndMimeType(fileRequest.getFileName(), fileRequest.getMediaType());

            if(storageFile == null) {
                throw new FileNotFoundException("File don't exist with type and name");
            }

            try {
                stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(fileRequest.getFileName())
                    .build());
            } catch (Exception e) {
                System.out.println("Happened error when get list objects from minio: " + e);
                return null;
            }

            return stream;
        }
        throw new ActivationExpiredException("You download delete a file because your status is inactive");
    }

    public void deleteFile(FileRequest fileRequest) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        UserReservation userReservation = userReservationRepository
            .findUserReservationByUser(userRepository.findOneByLogin(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());

        StorageFile storageFile = storageFileRepository
            .findStorageFileByNameAndMimeType(fileRequest.getFileName(), fileRequest.getMediaType());
        if(checkActivation()) {
            if (storageFile != null) {

                RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(fileRequest.getFileName())
                    .build();

                minioClient.removeObject(removeObjectArgs);
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



    @Deprecated
    private void WriteToMinIO(FileRequest fileRequest)
        throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException {
        try {
            MinioClient minioClient = MinioClient.builder().endpoint(minioProperties.getEndPoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey()).build();

            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            }


            String fileToUpload = fileRequest.getFilePath() + fileRequest.getFileName();
            UploadObjectArgs args = UploadObjectArgs.builder().bucket(minioProperties.getBucketName()).object(fileRequest.getFileName())
                .filename(fileToUpload).build();

            UserReservation userReservation = userReservationRepository
                .findUserReservationByUser(userRepository.findOneByLogin(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());

            if (checkActivation() && checkUsedSize((int) args.objectSize())) {
                minioClient.uploadObject(args);

                StorageFile storageFile = new StorageFile();

                storageFile.setName(fileRequest.getFileName());
                storageFile.setPath(fileRequest.getFilePath());
                storageFile.setSize(args.objectSize());
                storageFile.setMimeType(args.contentType());
                storageFile.setCreatedDate(Instant.now());
                storageFile.setCreatedBy(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow());
                storageFileService.saveFile(storageFile);

                userReservation.setUsedSize((userReservation.getUsedSize() + args.objectSize()));

            } else {
                userReservation.setActivated(false);
            }

            userReservationRepository.save(userReservation);


            System.out.println("objectSize" + String.valueOf(args.objectSize()));
            System.out.println("contentType" + args.contentType());
            System.out.println("partSize" + args.partSize());
            System.out.println("userMetadata" + args.userMetadata());


            System.out.println(fileToUpload + " successfully uploaded to:");
            System.out.println("   container: " + minioProperties.getBucketName());
            System.out.println("   blob: " + fileRequest.getFileName());
            System.out.println();
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }


    @Deprecated
    private void ReadFromMinIO(FileRequest fileRequest)
        throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException {
        try {

            MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getEndPoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();

            String downloadedFile = fileRequest.getFilePath() + fileRequest.getFileName();
            DownloadObjectArgs args = DownloadObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(fileRequest.getFileName())
                .filename(downloadedFile).build();

            if (checkActivation()) {
                minioClient.downloadObject(args);
            }
            System.out.println("Downloaded file to ");
            System.out.println(" " + downloadedFile);
            System.out.println();
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }


}
