package com.esempla.task.reports.utils;

import com.esempla.task.domain.StorageFile;
import com.esempla.task.reports.enums.FileType;
import com.esempla.task.repository.StorageFileRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@Component
public class ReportCsvGenerator implements ReportGenerator{

    private final StorageFileRepository storageFileRepository;

    public ReportCsvGenerator(StorageFileRepository storageFileRepository) {
        this.storageFileRepository = storageFileRepository;
    }

    @Override
    public byte[] generate() throws IOException {
        List<StorageFile> storageFiles = storageFileRepository.findAll();
        try(ByteArrayOutputStream output = new ByteArrayOutputStream();
            CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), CSVFormat.DEFAULT)){

            printer.printRecord(ReportHeaders.ID,ReportHeaders.NAME,ReportHeaders.SIZE,ReportHeaders.MIME_TYPE, ReportHeaders.PATH, ReportHeaders.CREATED_BY, ReportHeaders.CREATED_DATE);

            for (StorageFile record : storageFiles) {
                printer.printRecord(record.getId(), record.getName(), record.getSize(), record.getMimeType(),
                    record.getPath(), record.getCreatedBy(), ReportGeneratorUtils.convertDate(record.getCreatedDate().toString()));
            }
            printer.flush();
            return output.toByteArray();
        }
    }

    @Override
    public FileType getType() {
        return FileType.CSV;
    }

}
