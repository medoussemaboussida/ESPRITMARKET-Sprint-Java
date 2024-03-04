package Service;

import entities.Offre;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.util.List;
public class PDFExporterService {

    public boolean exportToPDF(List<Offre> offres, String filePath) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Liste des offres");
                contentStream.endText();

                int y = 650;
                for (Offre offre : offres) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.newLineAtOffset(100, y);
                    contentStream.showText("Nom de l'offre: " + offre.getNomOffre()+" || Description: " + offre.getDescriptionOffre()+" || Date de début: " + offre.getDateDebut()+" || Date de début: " + offre.getDateDebut()+" || Réduction: " + offre.getReduction());
                    contentStream.endText();

                    y -= 100; // Décalage vertical pour la prochaine offre
                    if (y <= 50) {
                        // Si la page est pleine, ajouter une nouvelle page
                        page = new PDPage();
                        document.addPage(page);
                        contentStream.close();
                        contentStream.drawLine(50, 50, 550, 50); // Ligne horizontale de séparation
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(100, 700);
                        contentStream.showText("Liste des offres (suite)");
                        contentStream.endText();
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        y = 650;
                    }
                }
            }
            document.save(new File(filePath));
        }
        return false;
    }
}
