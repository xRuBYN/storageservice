package com.esempla.task.service;

import com.esempla.task.domain.UserReservation;
import com.esempla.task.repository.UserRepository;
import com.esempla.task.repository.UserReservationRepository;
import com.esempla.task.service.dto.StatusRequest;
import com.esempla.task.service.dto.UserReservationRequest;
import com.esempla.task.service.dto.UserReservationResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserReservationService {

    private final UserReservationRepository userReservationRepository;

    private final UserRepository userRepository;

    public UserReservationService(UserReservationRepository userReservationRepository, UserRepository userRepository) {
        this.userReservationRepository = userReservationRepository;
        this.userRepository = userRepository;
    }

    public void addReservationForUser(UserReservationRequest userReservationRequest) {

        UserReservation userReservation = userReservationRepository
            .findUserReservationByUser(userRepository.findOneByLogin(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());

        if(userReservation == null) {
            userReservation = new UserReservation();
            userReservation.setCreatedDate(Instant.now());
            userReservation.setTotalSize(userReservationRequest.getTotalSize());
            userReservation.setUsedSize(0L);
            userReservation.setCreatedBy(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow());
            userReservation.setUser(userRepository.findOneByLogin(com.esempla.task.security.SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());
        } else {
            userReservation.setTotalSize(userReservationRequest.getTotalSize());
        }
        userReservationRepository.save(userReservation);

    }

    public List<UserReservationResponse> getAllReservation() {
        List<UserReservationResponse> userReservationResponseList = userReservationRepository.findAll()
            .stream()
            .map(UserReservation::toUserReservationResponse)
            .collect(Collectors.toList());

        return userReservationResponseList;
    }

    public UserReservationResponse setStatusForReservation(Long id, StatusRequest statusRequest){

        UserReservation userReservation = userReservationRepository.findUserReservationByUserId(id);

        if(userReservation == null) {
            throw new UserReservationNotFoundException("The user with id " + id + "don't have reservation.");
        }

        userReservation.setActivated(statusRequest.getStatus());

        userReservationRepository.save(userReservation);

        return userReservation.toUserReservationResponse();

    }
 }
