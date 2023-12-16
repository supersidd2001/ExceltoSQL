package com.my.qfc.common;

import com.my.qfc.common.util.DatabaseUtil;
import com.my.qfc.common.util.ExcelReaderStandalone;

public class App {

	public static void main(String[] args) {

		try {
			DatabaseUtil databaseUtil = new DatabaseUtil();
			ExcelReaderStandalone excelReader = new ExcelReaderStandalone(databaseUtil);
			String excelFilePath = "C:\\Users\\Siddharth Shinde\\Desktop\\Springmaven\\myQfcProject\\myQfcProject\\mycommon\\src\\main\\resources\\success.xlsx";
			excelReader.processExcelFile(excelFilePath);
			int successfulRecordsCount = excelReader.getSuccessfulRecordsCount();
			int errorRecordsCount = excelReader.getErrorRecordsCount();
			int totalRecordsCount = successfulRecordsCount + errorRecordsCount;

			System.out.println("Successful Records Count: " + successfulRecordsCount);
			System.out.println("Error Records Count: " + errorRecordsCount);
			System.out.println("Total Records Count: " + totalRecordsCount);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
