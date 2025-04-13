package com.filecomparator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResultViewer extends JFrame {
    public ResultViewer(List<FileComparisonResult> results) {
        setTitle("Comparison Results");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"File Name", "Status", "Differences"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setForeground(Color.WHITE);
        table.setBackground(Color.DARK_GRAY);
        table.setSelectionBackground(Color.GRAY);

        for (FileComparisonResult res : results) {
            Object[] row = {res.getFileName(), res.getStatus(), res.getDifferences()};
            model.addRow(row);
        }

        add(new JScrollPane(table));
        setVisible(true);
    }
}