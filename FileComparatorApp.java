package com.filecomparator;

import com.filecomparator.ui.FileComparatorFrame;

import javax.swing.*;

public class FileComparatorApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileComparatorFrame frame = new FileComparatorFrame();
            frame.setVisible(true);
        });
    }
}
