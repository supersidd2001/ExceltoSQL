// src/main/java/com/my/qfc/scheduler/ExcelFileScheduler.java
package com.my.qfc.service.code.scheduler;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.my.qfc.common.util.ExcelReaderStandalone;
import com.my.qfc.service.code.config.DatabaseService;

@Component
public class ExcelFileScheduler {

	private final DatabaseService databaseService;
	private final ExcelReaderStandalone excelReaderStandalone;

	@Value("${excel.file.path}")
	private String excelFilePath;

	@Value("${excel.output.path}")
	private String processedFolder;

	@Value("${excel.error.path}")
	private String errorFolder;

	public ExcelFileScheduler(DatabaseService databaseService, ExcelReaderStandalone excelReaderStandalone) {
		this.databaseService = databaseService;
		this.excelReaderStandalone = excelReaderStandalone;
	}

	@Scheduled(fixedRate = 10000) // 10 seconds
	public void processExcelFiles() {
		
		System.out.println("Scheduler iteration started");
		
		File folder = new File(excelFilePath);

		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles();

			if (files != null) {
				for (File file : files) {
					if (file.isFile() && file.getName().endsWith(".xlsx")) {
						processFile(file);
					}
				}
			}
		}
	}

	private void processFile(File file) {
		try {
			excelReaderStandalone.processExcelFile(file.getAbsolutePath());
			moveFileToFolder(file, processedFolder);
		} catch (Exception e) {
			moveFileToFolder(file, errorFolder);
			e.printStackTrace(); // Log or print the exception as needed
		}
	}

	private void moveFileToFolder(File file, String destinationFolder) {
		File destination = new File(destinationFolder, file.getName());
		if (file.renameTo(destination)) {
			System.out.println("File moved to " + destinationFolder + ": " + file.getName());
		} else {
			System.err.println("Error moving file to " + destinationFolder + ": " + file.getName());
		}
	}
}
