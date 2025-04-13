package com.filecomparator.ui;

import com.filecomparator.service.FileComparisonService;
import com.filecomparator.util.ReportExporter;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileComparatorFrame extends JFrame {
    private final JTextField folder1Field;
    private final JTextField folder2Field;
    private final JTextArea resultArea;

    public FileComparatorFrame() {
        setTitle("File Comparator Utility");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        UIManager.put("control", new Color(40, 40, 40));
        UIManager.put("info", new Color(40, 40, 40));
        UIManager.put("nimbusBase", new Color(18, 30, 49));
        UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
        UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
        UIManager.put("nimbusFocus", new Color(115, 164, 209));
        UIManager.put("nimbusGreen", new Color(176, 179, 50));
        UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
        UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
        UIManager.put("nimbusOrange", new Color(191, 98, 4));
        UIManager.put("nimbusRed", new Color(169, 46, 34));
        UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
        UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
        UIManager.put("text", new Color(230, 230, 230));
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        JPanel inputPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        inputPanel.setBackground(Color.DARK_GRAY);

        folder1Field = new JTextField();
        folder2Field = new JTextField();

        JButton browse1 = new JButton("Browse Folder 1");
        JButton browse2 = new JButton("Browse Folder 2");
        JButton compare = new JButton("Compare");

        browse1.addActionListener(e -> selectFolder(folder1Field));
        browse2.addActionListener(e -> selectFolder(folder2Field));

        compare.addActionListener(e -> {
            String path1 = folder1Field.getText();
            String path2 = folder2Field.getText();
            if (path1.isEmpty() || path2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select both folders.");
                return;
            }
            resultArea.setText("Comparing files...
");
            FileComparisonService.compareFolders(new File(path1), new File(path2), resultArea);
        });

        inputPanel.add(new JLabel("Folder 1:", SwingConstants.RIGHT));
        inputPanel.add(folder1Field);
        inputPanel.add(browse1);
        inputPanel.add(new JLabel("Folder 2:", SwingConstants.RIGHT));
        inputPanel.add(folder2Field);
        inputPanel.add(browse2);
        inputPanel.add(new JLabel());
        inputPanel.add(compare);

        resultArea = new JTextArea();
        resultArea.setBackground(Color.BLACK);
        resultArea.setForeground(Color.GREEN);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void selectFolder(JTextField targetField) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            targetField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }
}
