package com.esempla.task.reports.services;


import com.esempla.task.reports.enums.FileType;
import com.esempla.task.reports.exception.FileTypeNotFoundException;
import com.esempla.task.reports.utils.ReportGenerator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private Map<FileType, ReportGenerator> map;
    public ReportService(List<ReportGenerator> reportGenerators) {
        map = reportGenerators.stream().collect(Collectors.toMap(ReportGenerator::getType, Function.identity()));
    }

    public ReportGenerator buildReportGenerator(FileType fileType) {
        ReportGenerator reportGenerator = map.get(fileType);
        if(Objects.isNull(reportGenerator)) {
            throw new FileTypeNotFoundException();
        }
        System.out.println(map.size());
        return reportGenerator;
    }
}
