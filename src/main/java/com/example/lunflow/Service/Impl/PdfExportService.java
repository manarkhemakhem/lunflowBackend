package com.example.lunflow.Service.Impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
public class PdfExportService {

    public ByteArrayInputStream exportPdfFromContent(String content, String titleText, InputStream imageStream) {
        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            // Titre
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph(titleText, font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(Chunk.NEWLINE);

            // Ajout de l'image si elle existe
            if (imageStream != null) {
                Image img = Image.getInstance(imageStream.readAllBytes());
                img.scaleToFit(400, 300); // redimensionner l'image
                img.setAlignment(Image.ALIGN_CENTER);
                document.add(img);
                document.add(Chunk.NEWLINE);
            }
            // Contenu texte
            document.add(new Paragraph(content));

            document.close();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

}
