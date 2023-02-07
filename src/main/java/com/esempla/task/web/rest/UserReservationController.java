package com.esempla.task.web.rest;

import com.esempla.task.service.UserReservationService;
import com.esempla.task.service.dto.StatusRequest;
import com.esempla.task.service.dto.UserReservationRequest;
import com.esempla.task.service.dto.UserReservationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class UserReservationController {

    private final UserReservationService userReservationService;

    public UserReservationController(UserReservationService userReservationService) {
        this.userReservationService = userReservationService;
    }

    @PostMapping("/reservation/add-reservation")
    public ResponseEntity<Void> createUserReservation(@RequestBody UserReservationRequest userReservationRequest){
        userReservationService.addReservationForUser(userReservationRequest);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/admin/reservation/get-all-reservation")
    public ResponseEntity<List<UserReservationResponse>> getAllReservation() {

        List<UserReservationResponse> list = userReservationService.getAllReservation();

        return ResponseEntity.ok(list);
    }
    @PostMapping("/admin/reservation/{id}")
    public ResponseEntity<UserReservationResponse> setStatusForReservation(@RequestBody StatusRequest statusRequest,@PathVariable Long id) {

        UserReservationResponse userReservationResponse = userReservationService.setStatusForReservation(id,statusRequest);

        return ResponseEntity.ok(userReservationResponse);

    }




}
