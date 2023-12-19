package com.my.qfc.service.code.scheduler;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExcelScheduler {

	@Value("${input.folder}")
	private String inputFolder;

	@Value("${output.folder}")
	private String outputFolder;

	@Scheduled(fixedRate = 5000) // Run every 5 seconds
	public void moveFiles() {
		System.out.println("Moving .xlsx files from input to output folder...");

		try {
			Path sourcePath = Paths.get(inputFolder);
			Path destinationPath = Paths.get(outputFolder);

			// Create destination folder if it doesn't exist
			if (!Files.exists(destinationPath)) {
				Files.createDirectories(destinationPath);
			}

			// Move files
			try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(sourcePath, "*.xlsx")) {
				for (Path file : directoryStream) {
					Path relativePath = sourcePath.relativize(file);
					Path destinationFile = destinationPath.resolve(relativePath);
					Files.move(file, destinationFile, StandardCopyOption.REPLACE_EXISTING);
					System.out.printf("\nMoved %s to %s", file.toString(), destinationFile.toString());
				}
			}

			System.out.println("\nFiles moved successfully.");
		} catch (IOException e) {
			System.err.println("Error moving files: " + e.getMessage());
		}
	}
}
