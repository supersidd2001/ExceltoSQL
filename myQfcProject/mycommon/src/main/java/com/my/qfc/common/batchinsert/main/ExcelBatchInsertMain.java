// com.my.qfc.common.batchinsert.main.ExcelBatchInsertMain.java
package com.my.qfc.common.batchinsert.main;

import java.util.List;

import com.my.qfc.common.batchinsert.dao.ExcelBatchInsertDAO;
import com.my.qfc.common.batchinsert.processor.ExcelBatchInsertProcessor;
import com.my.qfc.common.util.DatabaseUtil;
import com.my.qfc.common.util.ErrorLogger;
import com.my.qfc.common.util.ExcelReaderStandalone;
import com.my.qfc.common.vo.UserEntity;

public class ExcelBatchInsertMain {

	public static void main(String[] args) {
		try {
			DatabaseUtil databaseUtil = new DatabaseUtil();
			ExcelBatchInsertDAO batchInsertDAO = new ExcelBatchInsertDAO(databaseUtil);
			ExcelBatchInsertProcessor batchInsertProcessor = new ExcelBatchInsertProcessor();

			String excelFilePath = "C:/Users/Siddharth Shinde/Desktop/Springmaven/myQfcProject/myQfcProject/mycommon/src/main/resources/Book1.xlsx";
			List<UserEntity> users = batchInsertProcessor.processExcelFile(excelFilePath);

			ExcelReaderStandalone excelReader = new ExcelReaderStandalone(databaseUtil);
			// Process the Excel file using the existing ExcelReaderStandalone class
			excelReader.processExcelFile(excelFilePath);

			// Log the number of processed users for debugging
			System.out.println("Number of Processed Users: " + users.size());

			// Handle errors and log appropriately
			List<ErrorLogger.ErrorDetails> errorDetailsList = ErrorLogger.getErrorDetailsList();
			for (ErrorLogger.ErrorDetails errorDetails : errorDetailsList) {
				// Log or handle errors as needed
				System.err.println("Error Data: " + errorDetails.getErrorData() + ", Error Message: "
						+ errorDetails.getErrorMessage() + ", Row Number: " + errorDetails.getRowNumber()
						+ ", Error Column: " + errorDetails.getErrorColumn() + ", File Name: "
						+ errorDetails.getFileName());
			}

			if (users != null && !users.isEmpty()) {
				batchInsertDAO.batchInsertUsers(users); // Using the new bulkInsert method
				System.out.println("Bulk insertion successful.");

			} else {
				System.out.println("No records to insert or error reading Excel file.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
