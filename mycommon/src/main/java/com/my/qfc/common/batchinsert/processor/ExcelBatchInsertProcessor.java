// com.my.qfc.common.batchinsert.processor.ExcelBatchInsertProcessor.java
package com.my.qfc.common.batchinsert.processor;

import java.util.List;

import com.my.qfc.common.util.DatabaseUtil;
import com.my.qfc.common.util.ErrorLogger;
import com.my.qfc.common.util.ExcelReaderStandalone;
import com.my.qfc.common.vo.UserEntity;

public class ExcelBatchInsertProcessor {

	DatabaseUtil databaseUtil;

	public List<UserEntity> processExcelFile(String filePath) {
		try {
			ExcelReaderStandalone excelReader = new ExcelReaderStandalone(databaseUtil);

			// Process the Excel file using the existing ExcelReaderStandalone class
			excelReader.processExcelFile(filePath);

			// Retrieve the list of successfully processed UserEntity objects
			List<UserEntity> users = excelReader.getProcessedUserEntities();

			// Handle errors and log appropriately
			List<ErrorLogger.ErrorDetails> errorDetailsList = ErrorLogger.getErrorDetailsList();
			for (ErrorLogger.ErrorDetails errorDetails : errorDetailsList) {
				// Log or handle errors as needed
				System.err.println("Error Data: " + errorDetails.getErrorData() + ", Error Message: "
						+ errorDetails.getErrorMessage() + ", Row Number: " + errorDetails.getRowNumber()
						+ ", Error Column: " + errorDetails.getErrorColumn() + ", File Name: "
						+ errorDetails.getFileName());
			}

			for (UserEntity user : users) {
				System.out.println(user.toString());
			}

			return users;
		} catch (Exception e) {
			// Log the error and handle exceptions appropriately
			ErrorLogger.logError("Processing Excel", "Error processing Excel file", -1, "N/A", filePath);
			return null;
		}
	}
}
