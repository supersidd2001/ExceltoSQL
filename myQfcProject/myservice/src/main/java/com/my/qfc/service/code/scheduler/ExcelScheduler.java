package com.my.qfc.service.code.scheduler;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.my.qfc.service.code.service.UserService;

@Component
public class ExcelScheduler {

	@Value("${input.folder}")
	private String inputFolder;

	@Value("${output.folder}")
	private String outputFolder;

	UserService userService;
	
	@Autowired 
    public ExcelScheduler(UserService userService) {
        this.userService = userService;
    }

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
			Files.walk(sourcePath).filter(path -> path.toString().endsWith(".xlsx")).forEach(file -> {
				try {
					Path relativePath = sourcePath.relativize(file);
					Path destinationFile = destinationPath.resolve(relativePath);
					Files.move(file, destinationFile, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(destinationPath, "*.xlsx")) {
				for (Path path : directoryStream) {
					userService.processExcelFiles(List.of(path.toString()));
				}
			}

			System.out.println("Files moved successfully.");
		} catch (IOException e) {
			System.err.println("Error moving files: " + e.getMessage());
		}
	}
}
