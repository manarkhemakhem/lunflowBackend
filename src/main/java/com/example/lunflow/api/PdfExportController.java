package com.example.lunflow.api;

import com.example.lunflow.Service.Impl.PdfExportService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequestMapping("/export")
public class PdfExportController {

    private final PdfExportService pdfExportService;

    public PdfExportController(PdfExportService pdfExportService) {
        this.pdfExportService = pdfExportService;
    }

    @PostMapping(value = "/export", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> exportToPdf(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("filename") String filename,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        try {
            ByteArrayInputStream pdfStream = pdfExportService.exportPdfFromContent(title, content, images);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfStream.readAllBytes());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}