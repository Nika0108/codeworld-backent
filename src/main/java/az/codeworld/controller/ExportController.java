package az.codeworld.controller;

import az.codeworld.util.ExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/applications/excel")
    public ResponseEntity<byte[]> exportAllExcel() throws IOException {
        byte[] file = exportService.exportApplicationsToExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=applications.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @GetMapping("/applications/accepted/excel")
    public ResponseEntity<byte[]> exportAcceptedExcel()
            throws IOException {
        byte[] file =
                exportService.exportAcceptedApplicationsToExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=accepted-applications.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @GetMapping("/applications/word")
    public ResponseEntity<byte[]> exportWord() throws IOException {
        byte[] file = exportService.exportApplicationsToWord();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=applications.docx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}