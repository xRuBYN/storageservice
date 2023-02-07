package com.esempla.task.service;

public class UserReservationNotFoundException extends RuntimeException {
    public UserReservationNotFoundException(String message) {
        super(message);
    }
}
