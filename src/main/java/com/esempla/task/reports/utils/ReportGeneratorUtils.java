package com.esempla.task.reports.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportGeneratorUtils {

    private static final String EXPECTED_DATE_FORMAT = "dd/MM/yyyy";

    private static final String CURRENT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'";

    public static String convertDate(String inputDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat(EXPECTED_DATE_FORMAT);
        try {
            Date date = new SimpleDateFormat(CURRENT_DATE_FORMAT).parse(inputDate);
            return dateFormat.format(date);
        } catch (ParseException e) {
            throw new RuntimeException();
        }
    }

}
