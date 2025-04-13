package com.filecomparator;

public class FileComparisonResult {
    private final String fileName;
    private final String status;
    private final String differences;

    public FileComparisonResult(String fileName, String status, String differences) {
        this.fileName = fileName;
        this.status = status;
        this.differences = differences;
    }

    public String getFileName() {
        return fileName;
    }

    public String getStatus() {
        return status;
    }

    public String getDifferences() {
        return differences;
    }
}