package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
@Service
public class pdfTextExtractor {

    public String extractTextFromUrl(String url) {
        try (InputStream input = new URL(url).openStream();
             PDDocument document = PDDocument.load(input)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            if (text == null || text.trim().isEmpty()) {
                return "Erreur : contenu PDF vide ou non lisible.";
            }
            return text;
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de l'extraction du texte : " + e.getMessage();
        }
    }


}
