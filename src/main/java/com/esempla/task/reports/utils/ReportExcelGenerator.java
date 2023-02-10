package com.esempla.task.reports.utils;

import com.esempla.task.domain.StorageFile;
import com.esempla.task.reports.enums.FileType;
import com.esempla.task.repository.StorageFileRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class ReportExcelGenerator implements ReportGenerator {

    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private final StorageFileRepository storageFileRepository;


    public ReportExcelGenerator(StorageFileRepository storageFileRepository) {
        this.storageFileRepository = storageFileRepository;
        this.workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("StorageFile" + UUID.randomUUID());
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, ReportHeaders.ID, style);
        createCell(row, 1, ReportHeaders.NAME, style);
        createCell(row, 2, ReportHeaders.SIZE, style);
        createCell(row, 3, ReportHeaders.MIME_TYPE, style);
        createCell(row, 4, ReportHeaders.PATH, style);
        createCell(row, 5, ReportHeaders.CREATED_BY, style);
        createCell(row, 6, ReportHeaders.CREATED_DATE, style);

    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else if(valueOfCell instanceof Instant){
            cell.setCellValue(Date.from((Instant) valueOfCell));
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    private void write() {

        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        List<StorageFile> storageFiles = storageFileRepository.findAll();

        for (StorageFile record: storageFiles) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.getId(), style);
            createCell(row, columnCount++, record.getName(), style);
            createCell(row, columnCount++, record.getSize(), style);
            createCell(row, columnCount++, record.getMimeType(), style);
            createCell(row, columnCount++, record.getPath(), style);
            createCell(row, columnCount++, record.getCreatedBy(), style);
            createCell(row, columnCount++, ReportGeneratorUtils.convertDate(record.getCreatedDate().toString()), style);
        }
    }

    @Override
    public byte[] generate() throws IOException {
        writeHeader();
        write();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    public FileType getType() {
        return FileType.XLSX;
    }
}
