package com.esempla.task.utils;

import com.esempla.task.domain.UserReservation;
import com.esempla.task.repository.UserRepository;
import com.esempla.task.repository.UserReservationRepository;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.UUID;

@Component
public class StorageFileServiceUtils {

    private final UserReservationRepository userReservationRepository;

    private final UserRepository userRepository;


    public StorageFileServiceUtils(UserReservationRepository userReservationRepository, UserRepository userRepository) {
        this.userReservationRepository = userReservationRepository;
        this.userRepository = userRepository;
    }


    public boolean checkActivation() {
        UserReservation userReservation = userReservationRepository
            .findUserReservationByUser(userRepository.findOneByLogin(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());
        return userReservation.isActivated();
    }

    public boolean checkUsedSize(Long size) {
        UserReservation userReservation = userReservationRepository
            .findUserReservationByUser(userRepository.findOneByLogin(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());

        if (userReservation.getTotalSize() >= userReservation.getUsedSize() + size) {
            return true;
        }
        return false;
    }

    public String generateUniqPath(String createdBy) {
        StringBuilder stringBuilder = new StringBuilder(createdBy);
        stringBuilder.append(File.separator).append(UUID.randomUUID());
        return String.valueOf(stringBuilder);
    }

}
