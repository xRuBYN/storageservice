package com.esempla.task.web.rest.errors;

import org.bouncycastle.pqc.crypto.newhope.NHSecretKeyProcessor;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";

    public static final String ERR_FILE_NOT_FOUND = "File Not Found";

    public static final String UNEXISTING_FILE_TYPE = "UNEXISTING FILE TYPE";

    public static final String ERR_NO_SUFFICIENT_STORAGE = "No sufficient storage.";
    public static final String ERR_INACTIVE_STATUS = "Status is inactive";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.jhipster.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");

    private ErrorConstants() {}
}
