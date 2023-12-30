package librillo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.util.ArrayList;
import java.util.List;

public class DuplexPrinter {

    // Método público para preparar un documento para impresión dúplex
    public static void prepareForDuplexPrinting(PDDocument originalDocument, PDDocument newDocument, int pagesPerBooklet) {
        int totalNumberOfPages = originalDocument.getNumberOfPages();
        List<Integer> pageOrder = calculatePageOrderForDuplex(totalNumberOfPages, pagesPerBooklet);

        for (Integer pageIndex : pageOrder) {
            PDPage page;
            if (pageIndex >= 0) {
                page = originalDocument.getPage(pageIndex);
                if (needsRotation(pageIndex, pagesPerBooklet, totalNumberOfPages)) {
                    page.setRotation(180);
                }
            } else {
                page = new PDPage();
            }
            newDocument.addPage(page);
        }
    }

    // Método público para determinar si una página necesita rotación
    public static boolean needsRotation(int pageIndex, int pagesPerBooklet, int totalNumberOfPages) {
        // Rotar las páginas impares para impresión dúplex
        return pageIndex % 2 != 0;
    }

    // Método público para rotar una página
    public static void rotatePage(PDPage page, int angle) {
        page.setRotation(angle);
    }

    // Método privado para calcular el orden de las páginas para dúplex
    private static List<Integer> calculatePageOrderForDuplex(int totalNumberOfPages, int pagesPerBooklet) {
        List<Integer> pageOrder = new ArrayList<>();
        int adjustedPageCount = calculateAdjustedPageCount(totalNumberOfPages, pagesPerBooklet);

        for (int i = 0; i < adjustedPageCount; i += pagesPerBooklet) {
            addPagesForDuplex(pageOrder, i, pagesPerBooklet, totalNumberOfPages);
        }

        return pageOrder;
    }

    // Método privado para añadir páginas para impresión dúplex
    private static void addPagesForDuplex(List<Integer> pageOrder, int start, int pagesPerBooklet, int totalNumberOfPages) {
        for (int j = 0; j < pagesPerBooklet / 2; j++) {
            int outerPageIndex = start + pagesPerBooklet - 1 - j;
            pageOrder.add(getPageIndexOrBlank(outerPageIndex, totalNumberOfPages));

            int innerPageIndex = start + j;
            pageOrder.add(getPageIndexOrBlank(innerPageIndex, totalNumberOfPages));
        }
    }

    // Método privado para calcular el número ajustado de páginas
    private static int calculateAdjustedPageCount(int totalNumberOfPages, int pagesPerBooklet) {
        return ((totalNumberOfPages + pagesPerBooklet - 1) / pagesPerBooklet) * pagesPerBooklet;
    }

    // Método privado para obtener el índice de una página o una página en blanco
    private static int getPageIndexOrBlank(int pageIndex, int totalNumberOfPages) {
        return pageIndex < totalNumberOfPages ? pageIndex : -1;
    }
}
