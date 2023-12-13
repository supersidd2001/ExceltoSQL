package com.my.qfc.common.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.time.LocalDateTime;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ErrorLogger {

	private static final String ERROR_FILE_PATH = "C:\\Users\\Siddharth Shinde\\Desktop\\Springmaven\\myQfcProject\\myQfcProject\\mycommon\\src\\main\\resources\\error.xlsx";

	public static void logError(String errorMessage, String program) {
		int lineNumber = getLineNumber();
		ErrorRecord errorRecord = new ErrorRecord(errorMessage, lineNumber, program);
		writeErrorRecord(errorRecord);
	}

	public static void logErrors(List<ErrorRecord> errorRecords) {
		writeErrorRecords(errorRecords);
	}

	private static int getLineNumber() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		return stackTrace[2].getLineNumber();
	}

	private static void writeErrorRecord(ErrorRecord errorRecord) {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("ErrorRecords");

			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("Error Message");
			headerRow.createCell(1).setCellValue("Line Number");
			headerRow.createCell(2).setCellValue("Program");

			Row row = sheet.createRow(1);
			row.createCell(0).setCellValue(errorRecord.getErrorMessage());
			row.createCell(1).setCellValue(errorRecord.getLineNumber());
			row.createCell(2).setCellValue(errorRecord.getProgram());

			// Write the workbook to the file
			try (FileOutputStream fileOut = new FileOutputStream(ERROR_FILE_PATH)) {
				workbook.write(fileOut);
			}
		} catch (IOException e) {
			// Handle exception or log it
			e.printStackTrace();
		}
	}

	private static void writeErrorRecords(List<ErrorRecord> errorRecords) {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("ErrorRecords");

			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("Error Message");
			headerRow.createCell(1).setCellValue("Line Number");
			headerRow.createCell(2).setCellValue("Program");

			int rowNum = 1;
			for (ErrorRecord errorRecord : errorRecords) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(errorRecord.getErrorMessage());
				row.createCell(1).setCellValue(errorRecord.getLineNumber());
				row.createCell(2).setCellValue(errorRecord.getProgram());
			}

			try (FileOutputStream fileOut = new FileOutputStream(ERROR_FILE_PATH)) {
				workbook.write(fileOut);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class ErrorRecord {
		private String errorMessage;
		private int lineNumber;
		private String program;

		public ErrorRecord(String errorMessage, int lineNumber, String program) {
			this.errorMessage = errorMessage;
			this.lineNumber = lineNumber;
			this.program = program;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public int getLineNumber() {
			return lineNumber;
		}

		public String getProgram() {
			return program;
		}
	}
}
