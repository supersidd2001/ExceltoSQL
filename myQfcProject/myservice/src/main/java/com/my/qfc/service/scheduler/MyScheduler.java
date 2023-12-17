package com.my.qfc.service.scheduler;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.my.qfc.common.util.ExcelReaderStandalone;
import com.my.qfc.service.util.ApplicationProperties;

@Component
public class MyScheduler {

	private final ApplicationProperties applicationProperties;

	private ExcelReaderStandalone excelReaderStandalone;

	public MyScheduler(ApplicationProperties applicationProperties, ExcelReaderStandalone excelReaderStandalone) {
		this.applicationProperties = applicationProperties;
		this.excelReaderStandalone = excelReaderStandalone;
	}

	@Scheduled(fixedRate = 5000) // Run every 5 seconds
	public void processFiles() {
		System.out.println("MyScheduler initialized with ApplicationProperties.");

		try {
			moveFiles(applicationProperties.getInputFolder(), applicationProperties.getOutputFolder());
			processExcelFiles();
		} catch (IOException e) {
			System.err.println("Error moving files: " + e.getMessage());
		}
	}

	private void moveFiles(String sourceFolder, String destinationFolder) throws IOException {
		Path sourcePath = Paths.get(sourceFolder);
		Path destinationPath = Paths.get(destinationFolder);

		// Create destination folder if it doesn't exist
		if (!Files.exists(destinationPath)) {
			Files.createDirectories(destinationPath);
		}

		// Move files
		Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Path relativePath = sourcePath.relativize(file);
				Path destinationFile = destinationPath.resolve(relativePath);
				Files.move(file, destinationFile, StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	private void processExcelFiles() {
		try (Stream<Path> paths = Files.walk(Paths.get(applicationProperties.getOutputFolder()))) {
			paths.filter(path -> path.toString().endsWith(".xlsx") && !path.toString().endsWith("_error.xlsx"))
					.forEach(this::processExcelFile);
		} catch (IOException e) {
			System.err.println("Error processing Excel files: " + e.getMessage());
		}
	}

	private void processExcelFile(Path filePath) {
		try {
			excelReaderStandalone.processExcelFile(filePath.toString());
		} catch (Exception e) {
			System.err.println("Error processing Excel file '" + filePath + "': " + e.getMessage());
		}
	}
}
