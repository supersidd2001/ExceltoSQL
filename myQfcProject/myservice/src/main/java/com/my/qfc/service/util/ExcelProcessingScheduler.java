package com.my.qfc.service.util;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.my.qfc.common.util.ErrorLogger;
import com.my.qfc.common.util.ExcelReaderStandalone;
import com.my.qfc.service.dao.ExcelProcessor;

@Component
public class ExcelProcessingScheduler {

    @Autowired
    private ExcelReaderStandalone excelReaderStandalone; // or Autowire your BulkInsertService

    @Scheduled(fixedRate = 600000) // Run every 10 minutes
    public void processExcelFiles() {
        String inputPath = ReadProperties.getInputPath();
        String archivePath = ReadProperties.getArchivePath();
        String errorPath = ReadProperties.getErrorPath();

        List<String> filesToProcess = ExcelProcessor.getNewFiles(inputPath, archivePath);

        for (String file : filesToProcess) {
            try {
                // Process the Excel file using ExcelReaderStandalone or BulkInsertService
                excelReaderStandalone.processExcelFile(file);

                // Move the processed file to the archive folder
                ExcelProcessor.moveFile(file, archivePath);

            } catch (Exception e) {
                // Log the error and move the file to the error folder
                ErrorLogger.logError("Error processing file", e.getMessage(), 0, "N/A", file);
                moveFile(file, errorPath);
            }
        }
    }
}
