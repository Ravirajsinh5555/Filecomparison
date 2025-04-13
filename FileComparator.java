package com.filecomparator;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class FileComparator {
    public static List<FileComparisonResult> compareFolders(File folder1, File folder2) {
        List<FileComparisonResult> results = new ArrayList<>();

        Map<String, File> folder1Files = new HashMap<>();
        for (File f : Objects.requireNonNull(folder1.listFiles())) {
            folder1Files.put(f.getName(), f);
        }

        for (File f2 : Objects.requireNonNull(folder2.listFiles())) {
            File f1 = folder1Files.remove(f2.getName());
            if (f1 != null) {
                results.add(compareFiles(f1, f2));
            } else {
                results.add(new FileComparisonResult(f2.getName(), "Missing in Folder1", ""));
            }
        }

        for (String remaining : folder1Files.keySet()) {
            results.add(new FileComparisonResult(remaining, "Missing in Folder2", ""));
        }

        return results;
    }

    private static FileComparisonResult compareFiles(File f1, File f2) {
        try {
            List<String> lines1 = Files.readAllLines(f1.toPath());
            List<String> lines2 = Files.readAllLines(f2.toPath());

            StringBuilder diff = new StringBuilder();
            int max = Math.max(lines1.size(), lines2.size());
            boolean different = false;

            for (int i = 0; i < max; i++) {
                String l1 = i < lines1.size() ? lines1.get(i) : "";
                String l2 = i < lines2.size() ? lines2.get(i) : "";

                if (!l1.equals(l2)) {
                    different = true;
                    diff.append("Line ").append(i + 1).append(":
");
                    diff.append("Folder1: ").append(l1).append("
");
                    diff.append("Folder2: ").append(l2).append("

");
                }
            }

            return new FileComparisonResult(f1.getName(), different ? "Mismatch" : "Match", diff.toString());
        } catch (IOException e) {
            return new FileComparisonResult(f1.getName(), "Error", e.getMessage());
        }
    }
}