package librillo;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFPageNumberer {

    public static void addPageNumbers(File inputFile, File outputFile) throws IOException {
        try (PDDocument document = PDDocument.load(inputFile)) {
            int totalPages = document.getNumberOfPages();

            for (int i = 0; i < totalPages; i++) {
                // Crea un PDPageContentStream para escribir en la página
                PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(i), PDPageContentStream.AppendMode.APPEND, true);

                // Escribe el número de página
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(500, 10); // Ajusta la posición del número de página según sea necesario
                contentStream.showText(Integer.toString(i + 1));
                contentStream.endText();

                contentStream.close();
            }

            document.save(outputFile);
        }
    }
}
