package az.codeworld.util;

import az.codeworld.entity.Application;
import az.codeworld.repository.ApplicationRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExportService {

    private final ApplicationRepository applicationRepository;

    public ExportService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    // Bütün müraciətləri Excel faylına çıxarır
    public byte[] exportApplicationsToExcel() throws IOException {
        List<Application> applications =
                applicationRepository.findAll();

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream =
                     new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Müraciətlər");

            Row header = sheet.createRow(0);
            String[] columns = {
                    "ID",
                    "Müraciət kodu",
                    "Ad",
                    "Soyad",
                    "Telefon",
                    "Email",
                    "Təhsil forması",
                    "İxtisas",
                    "Status",
                    "Proqram",
                    "Motivasiya",
                    "Tarix"
            };

            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowNumber = 1;

            for (Application app : applications) {
                Row row = sheet.createRow(rowNumber++);

                row.createCell(0).setCellValue(
                        app.getId() != null ? app.getId() : 0
                );
                row.createCell(1).setCellValue(
                        safe(app.getApplicationCode())
                );
                row.createCell(2).setCellValue(safe(app.getName()));
                row.createCell(3).setCellValue(safe(app.getSurname()));
                row.createCell(4).setCellValue(safe(app.getPhone()));
                row.createCell(5).setCellValue(safe(app.getEmail()));
                row.createCell(6).setCellValue(
                        app.getEducationType() != null
                                ? app.getEducationType().name() : ""
                );
                row.createCell(7).setCellValue(
                        app.getField() != null
                                ? app.getField().name() : ""
                );
                row.createCell(8).setCellValue(
                        app.getStatus() != null
                                ? app.getStatus().name() : ""
                );
                row.createCell(9).setCellValue(
                        app.getProgram() != null
                                ? safe(app.getProgram().getName()) : ""
                );
                row.createCell(10).setCellValue(
                        safe(app.getMotivation())
                );
                row.createCell(11).setCellValue(
                        app.getCreatedAt() != null
                                ? app.getCreatedAt().toString() : ""
                );
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    // Yalnız qəbul edilmiş müraciətləri Excel-ə çıxarır
    public byte[] exportAcceptedApplicationsToExcel()
            throws IOException {

        List<Application> applications =
                applicationRepository.findAll()
                        .stream()
                        .filter(app -> app.getStatus() != null
                                && app.getStatus().name()
                                .equals("ACCEPTED"))
                        .toList();

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream =
                     new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Qəbul edilənlər");

            Row header = sheet.createRow(0);
            String[] columns = {
                    "ID", "Ad", "Soyad", "Telefon",
                    "Email", "Status", "Proqram"
            };

            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowNumber = 1;

            for (Application app : applications) {
                Row row = sheet.createRow(rowNumber++);

                row.createCell(0).setCellValue(
                        app.getId() != null ? app.getId() : 0
                );
                row.createCell(1).setCellValue(safe(app.getName()));
                row.createCell(2).setCellValue(safe(app.getSurname()));
                row.createCell(3).setCellValue(safe(app.getPhone()));
                row.createCell(4).setCellValue(safe(app.getEmail()));
                row.createCell(5).setCellValue("ACCEPTED");
                row.createCell(6).setCellValue(
                        app.getProgram() != null
                                ? safe(app.getProgram().getName()) : ""
                );
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    // Bütün müraciətləri Word sənədinə çıxarır
    public byte[] exportApplicationsToWord() throws IOException {
        List<Application> applications =
                applicationRepository.findAll();

        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream outputStream =
                     new ByteArrayOutputStream()) {

            XWPFParagraph title = document.createParagraph();
            title.setAlignment(
                    org.apache.poi.xwpf.usermodel
                            .ParagraphAlignment.CENTER
            );

            XWPFRun titleRun = title.createRun();
            titleRun.setText(
                    "CODE WORLD – MÜRACİƏTLƏRİN SİYAHISI"
            );
            titleRun.setBold(true);
            titleRun.setFontSize(16);

            for (int i = 0; i < applications.size(); i++) {
                Application app = applications.get(i);

                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();

                run.setBold(true);
                run.setText((i + 1) + ". Müraciət");

                run.addBreak();
                run.setBold(false);
                run.setText("Müraciət kodu: "
                        + safe(app.getApplicationCode()));
                run.addBreak();
                run.setText("Ad: " + safe(app.getName()));
                run.addBreak();
                run.setText("Soyad: " + safe(app.getSurname()));
                run.addBreak();
                run.setText("Telefon: " + safe(app.getPhone()));
                run.addBreak();
                run.setText("Email: " + safe(app.getEmail()));
                run.addBreak();
                run.setText("Status: "
                        + (app.getStatus() != null
                        ? app.getStatus().name() : ""));
                run.addBreak();
                run.setText("Proqram: "
                        + (app.getProgram() != null
                        ? safe(app.getProgram().getName()) : ""));
                run.addBreak();
            }

            document.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}