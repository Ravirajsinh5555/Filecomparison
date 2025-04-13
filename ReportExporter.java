package com.filecomparator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportExporter {
    public static void exportReports(List<FileComparisonResult> results) {
        if (results == null || results.isEmpty()) return;

        exportExcel(results);
        exportHtml(results);
    }

    private static void exportExcel(List<FileComparisonResult> results) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Comparison Results");
            Row header = sheet.createRow(0);
            String[] headers = {"File Name", "Status", "Differences"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIndex = 1;
            for (FileComparisonResult res : results) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(res.getFileName());
                row.createCell(1).setCellValue(res.getStatus());
                row.createCell(2).setCellValue(res.getDifferences());
            }

            String fileName = "ComparisonReport_" + timestamp() + ".xlsx";
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exportHtml(List<FileComparisonResult> results) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>")
            .append("body { background-color: #1e1e1e; color: #ccc; font-family: Arial; }")
            .append("table { border-collapse: collapse; width: 100%; }")
            .append("th, td { border: 1px solid #444; padding: 8px; }")
            .append("th { background-color: #333; color: #fff; }")
            .append("</style></head><body><h2>Comparison Results</h2><table>")
            .append("<tr><th>File Name</th><th>Status</th><th>Differences</th></tr>");

        for (FileComparisonResult res : results) {
            html.append("<tr><td>").append(res.getFileName()).append("</td>")
                .append("<td>").append(res.getStatus()).append("</td>")
                .append("<td><pre>").append(res.getDifferences().replaceAll("<", "&lt;")).append("</pre></td></tr>");
        }

        html.append("</table></body></html>");

        try {
            Files.writeString(new File("ComparisonReport_" + timestamp() + ".html").toPath(), html.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String timestamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }
}