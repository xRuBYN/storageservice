package com.esempla.task.service;

import com.esempla.task.web.rest.errors.BadRequestAlertException;
import com.esempla.task.web.rest.errors.ErrorConstants;

public class StorageSizeException extends BadRequestAlertException {
    public StorageSizeException() {
        super(ErrorConstants.ERR_NO_SUFFICIENT_STORAGE, "No storage", "storage");
    }
}
