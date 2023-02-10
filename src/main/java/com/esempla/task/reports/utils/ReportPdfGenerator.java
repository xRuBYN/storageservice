package com.esempla.task.reports.utils;

import com.esempla.task.domain.StorageFile;
import com.esempla.task.reports.enums.FileType;
import com.esempla.task.repository.StorageFileRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ReportPdfGenerator implements ReportGenerator{

    private final int FONT_SIZE = 6;
    private final StorageFileRepository storageFileRepository;

    public ReportPdfGenerator(StorageFileRepository storageFileRepository) {
        this.storageFileRepository = storageFileRepository;
    }


    @Override
    public byte[] generate() throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfPTable table = new PdfPTable(7);
        List<StorageFile> storageFiles = storageFileRepository.findAll();

        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
            document.open();
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            table.addCell(getNormalCell(ReportHeaders.ID,FONT_SIZE));
            table.addCell(getNormalCell(ReportHeaders.NAME,FONT_SIZE));
            table.addCell(getNormalCell(ReportHeaders.SIZE,FONT_SIZE));
            table.addCell(getNormalCell(ReportHeaders.MIME_TYPE,FONT_SIZE));
            table.addCell(getNormalCell(ReportHeaders.PATH,FONT_SIZE));
            table.addCell(getNormalCell(ReportHeaders.CREATED_BY,FONT_SIZE));
            table.addCell(getNormalCell(ReportHeaders.CREATED_DATE,FONT_SIZE));

            for(StorageFile storageFile: storageFiles) {
                table.addCell(getNormalCell(storageFile.getId().toString(),FONT_SIZE));
                table.addCell(getNormalCell(storageFile.getName(),FONT_SIZE));
                table.addCell(getNormalCell(storageFile.getSize().toString(),FONT_SIZE));
                table.addCell(getNormalCell(storageFile.getMimeType(),FONT_SIZE));
                table.addCell(getNormalCell(storageFile.getPath(),FONT_SIZE));
                table.addCell(getNormalCell(storageFile.getCreatedBy(),FONT_SIZE));
                table.addCell(getNormalCell(ReportGeneratorUtils.convertDate(storageFile.getCreatedDate().toString()),FONT_SIZE));
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        return outputStream.toByteArray();
    }

    public static PdfPCell getNormalCell(String string,float size) {
        Font f  = new Font();
        f.setSize(size);
        PdfPCell cell = new PdfPCell(new Phrase(string, f));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    @Override
    public FileType getType() {
        return FileType.PDF;
    }
}
