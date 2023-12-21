package com.my.qfc.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.my.qfc.common.vo.UserEntity;

@Component
public class ExcelReaderStandalone {

	private final DatabaseUtil databaseUtil;
	private static int successfulRecordsCount = 0;
	private static int errorRecordsCount = 0;

	public ExcelReaderStandalone(DatabaseUtil databaseUtil) {
		this.databaseUtil = databaseUtil;
	}

	public void processExcelFile(String filePath) {
		File file = new File(filePath);
		String filename = extractFilename(filePath);
		System.setProperty("filename", filename);
		System.setProperty("logback.configurationFile", "logback.xml");

		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);

			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				XSSFRow row = sheet.getRow(rowIndex);
				if (row != null) {
					try {
						UserEntity UserEntity = createUserEntityFromRow(row, file.toString());
						processUserEntity(UserEntity);
					} catch (Exception e) {
						ErrorLogger.logError("Processing Excel Record", "Error processing Excel record", rowIndex,
								"N/A", filePath);
					}
				}
			}
		} catch (IOException e) {
			// Handle IOException appropriately
			ErrorLogger.logError("Reading Excel File", "Error reading Excel file", -1, "N/A", filePath);
		}
	}

	private UserEntity createUserEntityFromRow(Row row, String fileName) {
		double userId = getNumericCellValue(row.getCell(0), "UserID", fileName);
		String username = getStringCellValue(row.getCell(1), "UserName", fileName);
		String userAddress = getStringCellValue(row.getCell(2), "UserAddress", fileName);

		UserEntity UserEntity = new UserEntity();
		UserEntity.setUserid(userId);
		UserEntity.setUsername(username);
		UserEntity.setUseraddress(userAddress);

		return UserEntity;
	}

	@SuppressWarnings({ "null" })
	private double getNumericCellValue(Cell cell, String columnName, String fileName) {
		if (cell != null) {
			if (cell.getCellType() == CellType.NUMERIC) {
				return cell.getNumericCellValue();
			} else {
				ErrorLogger.logError(cell.toString(), "Invalid numeric cell type", cell.getRowIndex(), columnName,
						fileName);
				errorRecordsCount++;
			}
		} else {
			ErrorLogger.logError(cell.toString(), "Numeric cell is null", -1, columnName, fileName);
			errorRecordsCount++;
		}
		return (Double) null; // Default value
	}

	@SuppressWarnings({ "null" })
	private String getStringCellValue(Cell cell, String columnName, String fileName) {
		if (cell != null) {
			if (cell.getCellType() == CellType.STRING) {
				return cell.getStringCellValue();
			} else {
				ErrorLogger.logError(cell.toString(), "Invalid string cell type", cell.getRowIndex(), columnName,
						fileName);
				errorRecordsCount++;
			}
		} else {
			ErrorLogger.logError(cell.toString(), "String cell is null", -1, columnName, fileName);
			errorRecordsCount++;
		}
		return null; // Default value
	}

	private void processUserEntity(UserEntity userEntity) {
		try {
			// Check if the user already exists in the database
			UserEntity existingUser = getUserFromDatabase(userEntity.getUserid());

			if (existingUser != null) {
				// Update existing user
				existingUser.setUsername(userEntity.getUsername());
				existingUser.setUseraddress(userEntity.getUseraddress());
				try {
					databaseUtil.updateUser(existingUser);
					processedUserEntities.add(existingUser);
					successfulRecordsCount++;
				} catch (Exception e) {
				}
			} else {
				databaseUtil.insertUser(userEntity);
				processedUserEntities.add(userEntity);
				successfulRecordsCount++;
			}
		} catch (Exception e) {
			errorRecordsCount++;
		}
	}

	private UserEntity getUserFromDatabase(double userId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM UserEntity WHERE userId = :userId";
			Query<UserEntity> query = session.createQuery(hql, UserEntity.class);
			query.setParameter("userId", userId);

			UserEntity user = query.uniqueResult();
			return user;
		} catch (Exception e) {
			return null;
		}
	}

	private final List<UserEntity> processedUserEntities = new ArrayList<>();

	public List<UserEntity> getProcessedUserEntities() {
		return processedUserEntities;
	}

	public int getSuccessfulRecordsCount() {
		return successfulRecordsCount - 1;
	}

	public int getErrorRecordsCount() {
		return errorRecordsCount;
	}

	private String extractFilename(String filePath) {
		File file = new File(filePath);
		return file.getName();
	}
}
