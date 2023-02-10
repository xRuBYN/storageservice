package com.esempla.task.reports.utils;

import com.esempla.task.reports.enums.FileType;

import java.io.IOException;

public interface ReportGenerator {
    byte[] generate() throws IOException;

    FileType getType();
}
