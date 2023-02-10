package com.esempla.task.reports.config;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelConfig {

    @Bean
    public XSSFWorkbook getXSSWorkBook() {
        return new XSSFWorkbook();
    }

}
