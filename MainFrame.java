package librillo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JProgressBar progressBar;
    private JButton selectFileButton;
    private JButton processButton;
    private JComboBox<String> pagesPerBookletComboBox;
    private DefaultListModel<File> listModel;
    private JList<File> filesList;
    private int pagesPerBooklet = 4; // Valor por defecto
    private JCheckBox duplexCheckBox; // Duplex

    public MainFrame() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setTitle("BookletCreator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10);

        selectFileButton = new JButton("Open PDF File...");
        selectFileButton.addActionListener(this::openPDFFiles);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        add(selectFileButton, constraints);

        listModel = new DefaultListModel<>();
        filesList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(filesList);
        constraints.gridy = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridheight = 3;
        add(listScrollPane, constraints);

        constraints.gridheight = 1;
        constraints.weighty = 0;

        pagesPerBookletComboBox = new JComboBox<>(new String[]{"4", "8", "12", "16", "20", "24"});
        pagesPerBookletComboBox.addActionListener(e -> pagesPerBooklet = Integer.parseInt((String) pagesPerBookletComboBox.getSelectedItem()));
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        add(pagesPerBookletComboBox, constraints);

        processButton = new JButton("Create Booklet");
        processButton.addActionListener(e -> createBooklet());
        processButton.setEnabled(false);
        constraints.gridx = 1;
        add(processButton, constraints);

        duplexCheckBox = new JCheckBox("Print Duplex");
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        add(duplexCheckBox, constraints);

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(280, 20));
        progressBar.setStringPainted(true);
        constraints.gridy = 6;
        add(progressBar, constraints);

        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openPDFFiles(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
        fileChooser.setMultiSelectionEnabled(true);
        int openReturn = fileChooser.showOpenDialog(MainFrame.this);
        
        if (openReturn == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            for (File file : files) {
                listModel.addElement(file);
            }
            processButton.setEnabled(true);
        }
    }

    private void createBooklet() {
        boolean isDuplex = duplexCheckBox.isSelected();
        List<File> filesToRemove = new ArrayList<>();

        for (int i = 0; i < listModel.size(); i++) {
            File file = listModel.getElementAt(i);
            File processedFile = new File(file.getParent(), "processed_" + file.getName());
            // Suponiendo que processPDF y processPDFDuplex ya no lanzan IOException
            PDFProcessor.processPDF(file, pagesPerBooklet, progressBar, processedFile);
            if (isDuplex) {
                PDFProcessor.processPDFDuplex(processedFile, pagesPerBooklet, progressBar, processedFile);
            }
            filesToRemove.add(file);
        }

        for (File fileToRemove : filesToRemove) {
            listModel.removeElement(fileToRemove);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
