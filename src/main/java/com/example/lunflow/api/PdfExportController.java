package com.example.lunflow.api;
import com.example.lunflow.Service.Impl.PdfExportService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/export")
public class PdfExportController {

    private final PdfExportService pdfExportService;

    public PdfExportController(PdfExportService pdfExportService) {
        this.pdfExportService = pdfExportService;
    }

    @PostMapping(value = "/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportPdfFromContent(
            @RequestParam("content") String content,
            @RequestParam("title") String title,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(defaultValue = "document") String filename) {

        try (InputStream imageStream = (image != null && !image.isEmpty()) ? image.getInputStream() : null) {

            ByteArrayInputStream pdfStream = pdfExportService.exportPdfFromContent(content, title, imageStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + ".pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfStream.readAllBytes());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
