package com.example.lunflow.Service.Impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PdfExportService {

    public ByteArrayInputStream exportPdfFromContent(String title, String content, MultipartFile[] images) {
        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            // Titre
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titlePara = new Paragraph(title, titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            document.add(titlePara);
            document.add(Chunk.NEWLINE);

            // Contenu
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph contentPara = new Paragraph(content, contentFont);
            document.add(contentPara);
            document.add(Chunk.NEWLINE);

            // Images
            if (images != null) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        Image img = Image.getInstance(image.getBytes());
                        img.scaleToFit(400, 300);
                        img.setAlignment(Image.ALIGN_CENTER);
                        document.add(img);
                        document.add(Chunk.NEWLINE);
                    }
                }
            }

            document.close();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }
}