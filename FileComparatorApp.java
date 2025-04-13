package com.filecomparator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

public class FileComparatorApp {
    private JFrame frame;
    private JTextField folder1Field;
    private JTextField folder2Field;
    private List<FileComparisonResult> comparisonResults;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileComparatorApp::new);
    }

    public FileComparatorApp() {
        frame = new JFrame("File Comparator Utility - Dark Mode");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 200);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        JPanel panel = new JPanel(new GridLayout(3, 3, 10, 10));
        panel.setBackground(Color.DARK_GRAY);

        folder1Field = new JTextField();
        folder2Field = new JTextField();
        JButton browse1 = new JButton("Browse...");
        JButton browse2 = new JButton("Browse...");
        JButton compareBtn = new JButton("Compare");
        JButton exportBtn = new JButton("Export Reports");

        panel.add(new JLabel("Folder 1")).setForeground(Color.WHITE);
        panel.add(folder1Field);
        panel.add(browse1);
        panel.add(new JLabel("Folder 2")).setForeground(Color.WHITE);
        panel.add(folder2Field);
        panel.add(browse2);
        panel.add(new JLabel(""));
        panel.add(compareBtn);
        panel.add(exportBtn);

        browse1.addActionListener(e -> chooseFolder(folder1Field));
        browse2.addActionListener(e -> chooseFolder(folder2Field));
        compareBtn.addActionListener(this::performComparison);
        exportBtn.addActionListener(e -> ReportExporter.exportReports(comparisonResults));

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void chooseFolder(JTextField field) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ret = chooser.showOpenDialog(frame);
        if (ret == JFileChooser.APPROVE_OPTION) {
            field.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void performComparison(ActionEvent e) {
        File folder1 = new File(folder1Field.getText());
        File folder2 = new File(folder2Field.getText());

        if (!folder1.exists() || !folder2.exists()) {
            JOptionPane.showMessageDialog(frame, "Both folders must exist.");
            return;
        }

        comparisonResults = FileComparator.compareFolders(folder1, folder2);
        new ResultViewer(comparisonResults);
    }
}