package com.my.qfc.service.scheduler;

import java.io.File;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.my.qfc.common.util.DatabaseUtil;
import com.my.qfc.common.util.ExcelReaderStandalone;

@Component
public class ExcelFileScheduler {
	DatabaseUtil databaseUtil;

    @Value("${input.folder.path}")
    private String inputFolderPath;

    @Value("${processed.folder.path}")
    private String processedFolderPath;

    @Value("${error.folder.path}")
    private String errorFolderPath;

    @Scheduled(fixedRate = 1000) // 10 minutes in milliseconds
    public void processExcelFiles() {
    	System.out.println("starts");
        File inputFolder = new File(inputFolderPath);

        if (inputFolder.exists() && inputFolder.isDirectory()) {
            File[] files = inputFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xlsx"));

            if (files != null && files.length > 0) {
                Arrays.stream(files).forEach(this::processFile);
            }
        }
    }

    private void processFile(File file) {
        try {
            ExcelReaderStandalone excelReader = new ExcelReaderStandalone(databaseUtil);
            System.out.println("processing :"+ file.toString());
            excelReader.processExcelFile(file.getAbsolutePath());

            moveFileToFolder(file, processedFolderPath);
        } catch (Exception e) {
            createErrorFile(file);
        }
    }

    private void moveFileToFolder(File file, String destinationFolder) {
        File destination = new File(destinationFolder);

        if (!destination.exists()) {
            destination.mkdirs();
        }

        File newFile = new File(destination, file.getName());
        file.renameTo(newFile);
    }

    private void createErrorFile(File file) {
        System.out.println("Error File created");
    }
}
