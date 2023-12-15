package com.my.qfc.common.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ErrorLogger {

	private static final List<ErrorDetails> errorDetailsList = new ArrayList<>();
	private static final Logger logger = LoggerFactory.getLogger(ErrorLogger.class);

	public static void logError(String errorData, String errorMessage, int rowNumber, String errorColumn,
			String fileName) {
		ErrorDetails errorDetails = new ErrorDetails(errorData, errorMessage, rowNumber, errorColumn, fileName);
		errorDetailsList.add(errorDetails);

		logger.error("Error Data: {}, Error Message: {}, Row Number: {}, Error Column: {}, File Name: {}",
				errorDetails.getErrorData(), errorDetails.getErrorMessage(), errorDetails.getRowNumber(),
				errorDetails.getErrorColumn(), errorDetails.getFileName());

		// Save error details to an Excel file
		saveErrorLogFile(errorDetails);
	}

	private static void saveErrorLogFile(ErrorDetails errorDetails) {
		XSSFWorkbook errorWorkbook = new XSSFWorkbook();
		Sheet errorSheet = errorWorkbook.createSheet("ErrorDetails");

		// Create header row
		Row headerRow = errorSheet.createRow(0);
		headerRow.createCell(0).setCellValue("Error Data");
		headerRow.createCell(1).setCellValue("Error Message");
		headerRow.createCell(2).setCellValue("Row Number");
		headerRow.createCell(3).setCellValue("Error Column");
		headerRow.createCell(4).setCellValue("File Name");

		// Populate error details
		for (int i = 0; i < errorDetailsList.size(); i++) {
			ErrorDetails details = errorDetailsList.get(i);
			Row row = errorSheet.createRow(i + 1);
			row.createCell(0).setCellValue(details.getErrorData());
			row.createCell(1).setCellValue(details.getErrorMessage());
			row.createCell(2).setCellValue(details.getRowNumber());
			row.createCell(3).setCellValue(details.getErrorColumn());
			row.createCell(4).setCellValue(details.getFileName());
		}

		// Save error log file
		try {
			// Extract the directory path from the input fileName
			Path originalFilePath = Paths.get(errorDetails.getFileName());
			Path directoryPath = originalFilePath.getParent();

			// Create the error log file
			Path errorLogFilePath = directoryPath.resolve(originalFilePath.getFileName().toString() + "_error.xlsx");

			// Create parent directories if they do not exist
			Files.createDirectories(errorLogFilePath.getParent());

			// Write to the error log file
			try (FileOutputStream fileOut = new FileOutputStream(errorLogFilePath.toFile())) {
				errorWorkbook.write(fileOut);
			}

			logger.info("Error log file '{}' created successfully.", errorLogFilePath.toString());
		} catch (IOException e) {
			logger.error("Error creating or writing to the error log file.", e);
		}
	}

	public static List<ErrorDetails> getErrorDetailsList() {
		return errorDetailsList;
	}

	// Internal ErrorDetails class
	private static class ErrorDetails {
		private final String errorData;
		private final String errorMessage;
		private final int rowNumber;
		private final String errorColumn;
		private final String fileName;

		public ErrorDetails(String errorData, String errorMessage, int rowNumber, String errorColumn, String fileName) {
			this.errorData = errorData;
			this.errorMessage = errorMessage;
			this.rowNumber = rowNumber;
			this.errorColumn = errorColumn;
			this.fileName = fileName;
		}

		public String getErrorData() {
			return errorData;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public int getRowNumber() {
			return rowNumber;
		}

		public String getErrorColumn() {
			return errorColumn;
		}

		public String getFileName() {
			return fileName;
		}
	}
}
