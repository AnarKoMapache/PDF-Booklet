package librillo;

import java.awt.event.ActionListener;
import javax.swing.*;

public class GuiElements {
    public static JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }

    public static JLabel createLabel(String text) {
        return new JLabel(text);
    }

    public static JProgressBar createProgressBar(int min, int max) {
        JProgressBar progressBar = new JProgressBar(min, max);
        progressBar.setStringPainted(true);
        return progressBar;
    }

    public static JComboBox<String> createComboBox(int start, int end, int step) {
        JComboBox<String> comboBox = new JComboBox<>();
        for (int i = start; i <= end; i += step) {
            comboBox.addItem(String.valueOf(i));
        }
        return comboBox;
    }
}
