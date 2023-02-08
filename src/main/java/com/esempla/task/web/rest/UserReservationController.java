package com.esempla.task.web.rest;

import com.esempla.task.service.UserReservationService;
import com.esempla.task.service.dto.StatusRequest;
import com.esempla.task.service.dto.UserReservationRequest;
import com.esempla.task.service.dto.UserReservationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class UserReservationController {

    private final UserReservationService userReservationService;

    private final Logger log = LoggerFactory.getLogger(UserReservationController.class);


    public UserReservationController(UserReservationService userReservationService) {
        this.userReservationService = userReservationService;
    }

    @PostMapping("/reservation/add-reservation")
    public ResponseEntity<Void> createUserReservation(@RequestBody UserReservationRequest userReservationRequest) {
        log.debug("Added reservation");
        userReservationService.addReservationForUser(userReservationRequest);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/admin/reservation/get-all-reservation")
    public ResponseEntity<List<UserReservationResponse>> getAllReservation() {

        log.debug("Get all reservation");

        List<UserReservationResponse> list = userReservationService.getAllReservation();

        return ResponseEntity.ok(list);
    }
    @PostMapping("/admin/reservation/{id}")
    public ResponseEntity<UserReservationResponse> setStatusForReservation(@RequestBody StatusRequest statusRequest,@PathVariable Long id) {

        log.debug("Change status in " + statusRequest.getStatus() + "for user id " + id);

        UserReservationResponse userReservationResponse = userReservationService.setStatusForReservation(id,statusRequest);

        return ResponseEntity.ok(userReservationResponse);

    }




}
