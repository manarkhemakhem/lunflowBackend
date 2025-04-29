package com.example.lunflow.api;
import com.example.lunflow.Service.PdfExportService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayInputStream;
import java.util.Map;
@RestController
@RequestMapping("/export")
public class PdfExportController {

    private final PdfExportService pdfExportService;

    public PdfExportController(PdfExportService pdfExportService) {
        this.pdfExportService = pdfExportService;
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportPdfFromContent(
            @RequestBody Map<String, String> payload,
            @RequestParam(defaultValue = "document") String filename) {

        String content = payload.get("content");
        String title = payload.getOrDefault("title", "Titre par défaut");

        ByteArrayInputStream pdfStream = pdfExportService.exportPdfFromContent(content, title);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfStream.readAllBytes());
    }

}
