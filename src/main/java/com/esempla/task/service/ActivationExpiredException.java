package com.esempla.task.service;

public class ActivationExpiredException extends RuntimeException{
    public ActivationExpiredException(String message) {
        super(message);
    }
}
