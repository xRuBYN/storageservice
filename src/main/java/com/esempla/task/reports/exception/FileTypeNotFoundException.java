package com.esempla.task.reports.exception;

import com.esempla.task.web.rest.errors.BadRequestAlertException;

import static com.esempla.task.web.rest.errors.ErrorConstants.UNEXISTING_FILE_TYPE;

public class FileTypeNotFoundException extends BadRequestAlertException {
    public FileTypeNotFoundException() {
        super(UNEXISTING_FILE_TYPE, "fileType", "notfound");
    }
}
