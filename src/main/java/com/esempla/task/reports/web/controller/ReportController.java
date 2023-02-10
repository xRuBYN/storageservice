package com.esempla.task.reports.web.controller;

import com.esempla.task.reports.enums.FileType;
import com.esempla.task.reports.services.ReportService;
import com.esempla.task.reports.utils.ReportCsvGenerator;
import com.esempla.task.reports.utils.ReportExcelGenerator;
import com.esempla.task.reports.utils.ReportPdfGenerator;
import com.sun.mail.iap.ByteArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    private final Logger log = LoggerFactory.getLogger(ReportController.class);

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/export/{type}")
    public ResponseEntity<byte[]> getReport(@PathVariable FileType type) throws IOException {
        log.debug("Export file with type: " + type);
        return ResponseEntity.ok(reportService.buildReportGenerator(type).generate());

    }





}
