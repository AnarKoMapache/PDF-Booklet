package librillo;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileChooserModule {
    public static List<File> chooseFiles(JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        List<File> selectedFiles = new ArrayList<>();
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            for (File selectedFile : fileChooser.getSelectedFiles()) {
                if (!selectedFiles.contains(selectedFile)) {
                    selectedFiles.add(selectedFile);
                }
            }
        }
        return selectedFiles;
    }
}
