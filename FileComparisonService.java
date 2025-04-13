package com.filecomparator.service;

import com.filecomparator.util.ReportExporter;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileComparisonService {

    public static void compareFolders(File folder1, File folder2, JTextArea outputArea) {
        File[] files1 = folder1.listFiles();
        if (files1 == null) return;

        List<String[]> reportRows = new ArrayList<>();
        String[] headers = {"File Name", "Status", "Difference Content"};

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><head><style>")
                .append("body { background-color: #1e1e1e; color: #e6e6e6; font-family: monospace; }")
                .append(".diff { color: #ff6666; } .match { color: #66ff66; }")
                .append("</style></head><body><h2>File Comparison Report</h2>");

        for (File file1 : files1) {
            File file2 = new File(folder2, file1.getName());
            if (!file2.exists() || !file2.isFile()) continue;

            try {
                List<String> lines1 = Files.readAllLines(file1.toPath());
                List<String> lines2 = Files.readAllLines(file2.toPath());

                StringBuilder diffText = new StringBuilder();
                StringBuilder htmlDiff = new StringBuilder();
                boolean isDifferent = false;

                int maxLines = Math.max(lines1.size(), lines2.size());

                for (int i = 0; i < maxLines; i++) {
                    String l1 = i < lines1.size() ? lines1.get(i) : "";
                    String l2 = i < lines2.size() ? lines2.get(i) : "";

                    if (!l1.equals(l2)) {
                        isDifferent = true;
                        String[] highlighted = highlightDifference(l1, l2);
                        diffText.append("[Line ").append(i + 1).append("]
")
                                .append("F1: ").append(highlighted[0]).append("
")
                                .append("F2: ").append(highlighted[1]).append("

");

                        htmlDiff.append("<pre>[Line ").append(i + 1).append("]<br>")
                                .append("F1: ").append(highlighted[0]).append("<br>")
                                .append("F2: ").append(highlighted[1]).append("<br><br></pre>");
                    }
                }

                if (!isDifferent) {
                    reportRows.add(new String[]{file1.getName(), "MATCH", ""});
                    outputArea.append(file1.getName() + ": MATCH
");
                } else {
                    String diffOutput = diffText.toString();
                    reportRows.add(new String[]{file1.getName(), "DIFFERENT", diffOutput});
                    outputArea.append(file1.getName() + ": DIFFERENT
" + diffOutput);
                    htmlBuilder.append("<h3>").append(file1.getName()).append("</h3>").append(htmlDiff);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        htmlBuilder.append("</body></html>");
        ReportExporter.exportExcelReport(reportRows, headers);
        ReportExporter.exportHtmlReport(htmlBuilder.toString());
    }

    private static String[] highlightDifference(String line1, String line2) {
        StringBuilder l1 = new StringBuilder();
        StringBuilder l2 = new StringBuilder();
        int maxLen = Math.max(line1.length(), line2.length());

        for (int i = 0; i < maxLen; i++) {
            char c1 = i < line1.length() ? line1.charAt(i) : ' ';
            char c2 = i < line2.length() ? line2.charAt(i) : ' ';
            if (c1 == c2) {
                l1.append(c1);
                l2.append(c2);
            } else {
                l1.append("<span class='diff'>").append(c1).append("</span>");
                l2.append("<span class='diff'>").append(c2).append("</span>");
            }
        }

        return new String[]{l1.toString(), l2.toString()};
    }
}
