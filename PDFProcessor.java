package librillo;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFProcessor {

    // Método principal para procesar el PDF y crear un booklet
	public static void processPDF(File selectedFile, int pagesPerBooklet, JProgressBar progressBar, File outputFile) {
        SwingUtilities.invokeLater(() -> progressBar.setValue(0));

        try (PDDocument document = PDDocument.load(selectedFile)) {
            int totalNumberOfPages = document.getNumberOfPages();
            
            // Obtener el tamaño de la primera página para usarlo en las páginas en blanco
            PDRectangle pageSize = document.getPage(0).getMediaBox();

            List<Integer> pageOrder;
            switch (pagesPerBooklet) {
                case 4:
                    pageOrder = calculateBookletPageOrderForFour(totalNumberOfPages);
                    break;
                case 8:
                    pageOrder = calculateBookletPageOrderForEight(totalNumberOfPages);
                    break;
                case 12:
                    pageOrder = calculateBookletPageOrderForTwelve(totalNumberOfPages);
                    break;
                case 16:
                    pageOrder = calculateBookletPageOrderForSixteen(totalNumberOfPages);
                    break;
                case 20:
                    pageOrder = calculateBookletPageOrderForTwenty(totalNumberOfPages);
                    break;
                case 24:
                    pageOrder = calculateBookletPageOrderForTwentyFour(totalNumberOfPages);
                    break;
                default:
                    pageOrder = calculateBookletPageOrder(totalNumberOfPages, pagesPerBooklet);
                    break;
            }

            List<PDPage> reorderedPages = new ArrayList<>();
            for (Integer pageIndex : pageOrder) {
                if (pageIndex >= 0 && pageIndex < totalNumberOfPages) {
                    reorderedPages.add(document.getPage(pageIndex));
                } else {
                    // Crear y agregar una página en blanco del mismo tamaño
                    PDPage blankPage = new PDPage(pageSize);
                    reorderedPages.add(blankPage);
                }
            }

            File processedFolder = new File(selectedFile.getParentFile(), "ProcessedPDFs");
            if (!processedFolder.exists()) {
                processedFolder.mkdir();
            }

            try (PDDocument newDocument = new PDDocument()) {
                for (PDPage page : reorderedPages) {
                    newDocument.addPage(page);
                }

                newDocument.save(outputFile.getAbsolutePath());

                Toolkit.getDefaultToolkit().beep();
                SwingUtilities.invokeLater(() -> progressBar.setValue(100));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Métodos específicos para cada tamaño de folleto
    //Calcula de 4, 4-1-2-3
    private static List<Integer> calculateBookletPageOrderForFour(int totalNumberOfPages) {
        List<Integer> newOrder = new ArrayList<>();

        // Extender el total de páginas al próximo múltiplo de 4
        int adjustedPageCount = totalNumberOfPages;
        if (adjustedPageCount % 4 != 0) {
            adjustedPageCount = ((totalNumberOfPages / 4) + 1) * 4;
        }

        for (int i = 0; i < adjustedPageCount; i += 4) {
            // Añadir los índices de las páginas en el orden 4-1-2-3
            int page4Index = (i + 3 < totalNumberOfPages) ? i + 3 : -1; // Página 4 o una página en blanco
            int page1Index = (i < totalNumberOfPages) ? i : -1;         // Página 1 o una página en blanco
            int page2Index = (i + 1 < totalNumberOfPages) ? i + 1 : -1; // Página 2 o una página en blanco
            int page3Index = (i + 2 < totalNumberOfPages) ? i + 2 : -1; // Página 3 o una página en blanco

            newOrder.add(page4Index); // Página 4 o una página en blanco
            newOrder.add(page1Index); // Página 1 o una página en blanco
            newOrder.add(page2Index); // Página 2 o una página en blanco
            newOrder.add(page3Index); // Página 3 o una página en blanco
        }
        
        System.out.println("Orden de las páginas para folleto de 4 páginas: " + newOrder);
        return newOrder;
    }
    
    //Calcula de 8, 8-1-2-7-6-3-4-5
    private static List<Integer> calculateBookletPageOrderForEight(int totalNumberOfPages) {
        List<Integer> newOrder = new ArrayList<>();

        // Asegurarse de que el número total de páginas es un múltiplo de 8
        int adjustedPageCount = totalNumberOfPages;
        if (adjustedPageCount % 8 != 0) {
            adjustedPageCount = ((totalNumberOfPages / 8) + 1) * 8;
        }

        for (int i = 0; i < adjustedPageCount; i += 8) {
            // Añadir los índices de las páginas en el orden necesario para un folleto de 8 páginas
            // Si falta una página, se añade -1 (página en blanco)
            newOrder.add((i + 7 < totalNumberOfPages) ? i + 7 : -1); // Página 8
            newOrder.add((i < totalNumberOfPages) ? i : -1);         // Página 1
            newOrder.add((i + 1 < totalNumberOfPages) ? i + 1 : -1); // Página 2
            newOrder.add((i + 6 < totalNumberOfPages) ? i + 6 : -1); // Página 7
            newOrder.add((i + 5 < totalNumberOfPages) ? i + 5 : -1); // Página 6
            newOrder.add((i + 2 < totalNumberOfPages) ? i + 2 : -1); // Página 3
            newOrder.add((i + 3 < totalNumberOfPages) ? i + 3 : -1); // Página 4
            newOrder.add((i + 4 < totalNumberOfPages) ? i + 4 : -1); // Página 5
        }
        
        System.out.println("Orden de las páginas para folleto de 8 páginas: " + newOrder);
        return newOrder;
    }
    
    //Calcula de 12, 12-1-2-11-10-3-4-9-8-5-6-7
    private static List<Integer> calculateBookletPageOrderForTwelve(int totalNumberOfPages) {
        List<Integer> newOrder = new ArrayList<>();

        // Asegurarse de que el número total de páginas es al menos 12 o ajustar con páginas en blanco
        int adjustedPageCount = (totalNumberOfPages < 12) ? 12 : totalNumberOfPages;

        for (int i = 0; i < adjustedPageCount; i += 12) {
            // Añadir los índices de las páginas en el orden necesario para un folleto de 12 páginas
            int[] pageIndices = {
                i + 11, i,      // Hoja 1, Cara A
                i + 1, i + 10,  // Hoja 1, Cara B
                i + 9, i + 2,   // Hoja 2, Cara A
                i + 3, i + 8,   // Hoja 2, Cara B
                i + 7, i + 4,   // Hoja 3, Cara A
                i + 5, i + 6    // Hoja 3, Cara B
            };

            for (int pageIndex : pageIndices) {
                if (pageIndex < totalNumberOfPages) {
                    newOrder.add(pageIndex);
                } else {
                    newOrder.add(-1); // Añadir página en blanco si es necesario
                }
            }
        }

        return newOrder;
    }
    
    //Calcula de 16, 16-1-2-15-14-3-4-13-12-5-6-11-10-7-8-9
    private static List<Integer> calculateBookletPageOrderForSixteen(int totalNumberOfPages) {
        List<Integer> newOrder = new ArrayList<>();

        // Asegurarse de que el número total de páginas sea al menos 16 o ajustarlo con páginas en blanco
        int adjustedPageCount = Math.max(totalNumberOfPages, 16);

        for (int i = 0; i < adjustedPageCount; i += 16) {
            int[] pageIndices = {
                i + 15, i,       // Hoja 1, Cara A
                i + 1, i + 14,   // Hoja 1, Cara B
                i + 13, i + 2,   // Hoja 2, Cara A
                i + 3, i + 12,   // Hoja 2, Cara B
                i + 11, i + 4,   // Hoja 3, Cara A
                i + 5, i + 10,   // Hoja 3, Cara B
                i + 9, i + 6,    // Hoja 4, Cara A
                i + 7, i + 8     // Hoja 4, Cara B
            };

            for (int pageIndex : pageIndices) {
                if (pageIndex < totalNumberOfPages) {
                    newOrder.add(pageIndex);
                } else {
                    newOrder.add(-1); // Añadir una página en blanco si es necesario
                }
            }
        }

        return newOrder;
    }
    
  //Calcula de 20, 20-1-2-19-18-3-4-17-16-5-6-15-14-7-8-13-12-9-10-11
    private static List<Integer> calculateBookletPageOrderForTwenty(int totalNumberOfPages) {
        List<Integer> newOrder = new ArrayList<>();

        // Asegurarse de que el número total de páginas sea al menos 20 o ajustarlo con páginas en blanco
        int adjustedPageCount = (totalNumberOfPages < 20) ? 20 : totalNumberOfPages;

        for (int i = 0; i < adjustedPageCount; i += 20) {
            int[] pageIndices = {
                i + 19, i,       // Hoja 1, Cara A: Página 20, Página 1
                i + 1, i + 18,   // Hoja 1, Cara B: Página 2, Página 19
                i + 17, i + 2,   // Hoja 2, Cara A: Página 18, Página 3
                i + 3, i + 16,   // Hoja 2, Cara B: Página 4, Página 17
                i + 15, i + 4,   // Hoja 3, Cara A: Página 16, Página 5
                i + 5, i + 14,   // Hoja 3, Cara B: Página 6, Página 15
                i + 13, i + 6,   // Hoja 4, Cara A: Página 14, Página 7
                i + 7, i + 12,   // Hoja 4, Cara B: Página 8, Página 13
                i + 11, i + 8,   // Hoja 5, Cara A: Página 12, Página 9
                i + 9, i + 10    // Hoja 5, Cara B: Página 10, Página 11
            };

            for (int pageIndex : pageIndices) {
                if (pageIndex < totalNumberOfPages) {
                    newOrder.add(pageIndex);
                } else {
                    newOrder.add(-1); // Añadir una página en blanco si es necesario
                }
            }
        }

        return newOrder;
    }
    
    //Calcula de 24, 24-1-2-23-22-3-4-21-20-5-6-19-18-7-8-17-16-9-10-15-14-11-12-13
    private static List<Integer> calculateBookletPageOrderForTwentyFour(int totalNumberOfPages) {
        List<Integer> newOrder = new ArrayList<>();

        // Asegurarse de que el número total de páginas sea al menos 24 o ajustarlo con páginas en blanco
        int adjustedPageCount = (totalNumberOfPages < 24) ? 24 : totalNumberOfPages;

        for (int i = 0; i < adjustedPageCount; i += 24) {
            int[] pageIndices = {
                i + 23, i,       // Hoja 1, Cara A: Página 24, Página 1
                i + 1, i + 22,   // Hoja 1, Cara B: Página 2, Página 23
                i + 21, i + 2,   // Hoja 2, Cara A: Página 22, Página 3
                i + 3, i + 20,   // Hoja 2, Cara B: Página 4, Página 21
                i + 19, i + 4,   // Hoja 3, Cara A: Página 20, Página 5
                i + 5, i + 18,   // Hoja 3, Cara B: Página 6, Página 19
                i + 17, i + 6,   // Hoja 4, Cara A: Página 18, Página 7
                i + 7, i + 16,   // Hoja 4, Cara B: Página 8, Página 17
                i + 15, i + 8,   // Hoja 5, Cara A: Página 16, Página 9
                i + 9, i + 14,   // Hoja 5, Cara B: Página 10, Página 15
                i + 13, i + 10,  // Hoja 6, Cara A: Página 14, Página 11
                i + 11, i + 12   // Hoja 6, Cara B: Página 12, Página 13
            };

            for (int pageIndex : pageIndices) {
                if (pageIndex < totalNumberOfPages) {
                    newOrder.add(pageIndex);
                } else {
                    newOrder.add(-1); // Añadir una página en blanco si es necesario
                }
            }
        }

        return newOrder;
    }


    private static List<Integer> calculateBookletPageOrder(int totalNumberOfPages, int pagesPerBooklet) {
        List<Integer> newOrder = new ArrayList<>();

        // Redondear hacia arriba al múltiplo más cercano de pagesPerBooklet, solo si es necesario
        int bookletSize = totalNumberOfPages / pagesPerBooklet * pagesPerBooklet;
        if (totalNumberOfPages % pagesPerBooklet != 0) {
            bookletSize += pagesPerBooklet;
        }

        for (int start = 0; start < bookletSize; start += pagesPerBooklet) {
            int end = Math.min(start + pagesPerBooklet - 1, totalNumberOfPages - 1);

            // Agregar páginas en el orden correcto para el booklet
            for (int i = 0; i < pagesPerBooklet / 2; i++) {
                // Verificar si la página está dentro del rango total de páginas
                if (end - i < totalNumberOfPages) {
                    newOrder.add(end - i);
                }

                if (start + i < totalNumberOfPages) {
                    newOrder.add(start + i);
                }
            }
        }

        return newOrder;
    }
	
	// Método para procesar el PDF y prepararlo para impresión dúplex
    public static void processPDFDuplex(File selectedFile, int pagesPerBooklet, JProgressBar progressBar, File outputFile) {
        SwingUtilities.invokeLater(() -> progressBar.setValue(0));

        try (PDDocument originalDocument = PDDocument.load(selectedFile)) {
            int totalNumberOfPages = originalDocument.getNumberOfPages();

            try (PDDocument newDocument = new PDDocument()) {
                for (int pageIndex = 0; pageIndex < totalNumberOfPages; pageIndex++) {
                    PDPage page = originalDocument.getPage(pageIndex);
                    if (DuplexPrinter.needsRotation(pageIndex, pagesPerBooklet, totalNumberOfPages)) {
                        page.setRotation(180);
                    }
                    newDocument.addPage(page);
                }

                File processedFolder = new File(selectedFile.getParentFile(), "ProcessedPDFs");
                if (!processedFolder.exists()) {
                    processedFolder.mkdir();
                }
                newDocument.save(outputFile.getAbsolutePath());

                Toolkit.getDefaultToolkit().beep();
                SwingUtilities.invokeLater(() -> progressBar.setValue(100));
            }
        } catch (IOException e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                progressBar.setValue(0);
                JOptionPane.showMessageDialog(null, "Error processing the document: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            });
        }
    }
    // Método para actualizar la barra de progreso durante el procesamiento
    @SuppressWarnings("unused")
	private static void updateProgressBar(int current, int total, JProgressBar progressBar) {
        SwingUtilities.invokeLater(() -> progressBar.setValue((int) (((double) current / total) * 100)));
    }
}
