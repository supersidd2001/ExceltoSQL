package com.my.qfc.service.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import com.my.qfc.common.util.ErrorLogger;
import com.my.qfc.common.vo.UserEntity;
import com.my.qfc.service.util.ReadProperties;

@Component
public class ExcelProcessor {

	public List<UserEntity> readExcel(String filePath) throws IOException {
		List<UserEntity> userEntities = new ArrayList<>();

		try (Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))) {
			Sheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet

			Iterator<Row> rowIterator = sheet.iterator();
			int rowNum = 0;

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				rowNum++;

				// Skip header row
				if (rowNum == 1) {
					continue;
				}

				try {
					// Assuming the columns are in the order: userid, username, useraddress
					double userid = row.getCell(0).getNumericCellValue();
					String username = row.getCell(1).getStringCellValue();
					String useraddress = row.getCell(2).getStringCellValue();

					UserEntity userEntity = new UserEntity(userid, username, useraddress);
					userEntities.add(userEntity);
				} catch (Exception e) {
					// Log error and continue to the next row
					handleExcelError(filePath, "Error processing row", rowNum, e.getMessage());
				}
			}
		}

		return userEntities;
	}
	
	public static List<String> getNewFiles(String inputPath, String archivePath) {
        try {
            List<Path> inputFiles = getFilesInDirectory(inputPath);
            List<Path> archiveFiles = getFilesInDirectory(archivePath);

            // Identify new files by comparing timestamps
            return inputFiles.stream()
                    .filter(file -> isNewFile(file, archiveFiles))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            // Handle or log the exception
            e.printStackTrace();
            return List.of();
        }
    }

    private static List<Path> getFilesInDirectory(String directoryPath) throws IOException {
        try (Stream<Path> walk = Files.walk(Paths.get(directoryPath))) {
            return walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
    }

    private static boolean isNewFile(Path inputFile, List<Path> archiveFiles) {
        String inputFileName = inputFile.getFileName().toString();

        // Check if the file is not present in the archive or if the last modified timestamp is newer
        return archiveFiles.stream()
                .noneMatch(archiveFile -> archiveFile.getFileName().toString().equals(inputFileName) &&
                        inputFile.toFile().lastModified() > archiveFile.toFile().lastModified());
    }


	private void handleExcelError(String filePath, String errorMessage, int rowNum, String errorDetails) {
		// Log the error using ErrorLogger
		ErrorLogger.logError(filePath, errorMessage, rowNum, "Excel Processing", errorDetails);
	}
	
	public static void moveFile(String filename, String sourcePath) {
        Path source = Paths.get(sourcePath, filename);
        String destinationPath = ReadProperties.getArchivePath();
		Path destination = Paths.get(destinationPath , filename);

        try {
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved successfully: " + destination.toString());
        } catch (IOException e) {
            // Handle or log the exception
            System.out.println("Error moving file: " + filename);
            e.printStackTrace();
        }
    }
}
