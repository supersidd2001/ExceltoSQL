// ExcelReaderStandalone.java
package com.my.qfc.common.util;
//ExcelReaderStandalone.java continued...

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.my.qfc.common.vo.UserVO;

public class ExcelReaderStandalone {

	private final DatabaseUtil databaseUtil;
	private int successfulRecordsCount = 0;
	private int errorRecordsCount = 0;

	public ExcelReaderStandalone(DatabaseUtil databaseUtil) {
		this.databaseUtil = databaseUtil;
	}

	public void processExcelFile(String filePath) {
		try (FileInputStream fileInputStream = new FileInputStream(new File(
				"C:\\Users\\Siddharth Shinde\\Desktop\\Springmaven\\myQfcProject\\myQfcProject\\mycommon\\src\\main\\resources\\success.xlsx"));
				XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

			XSSFSheet sheet = workbook.getSheetAt(0);

			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				XSSFRow row = sheet.getRow(rowIndex);
				if (row != null) {
					UserVO userEntity = createUserEntityFromRow(row);
					processUserEntity(userEntity);
				}
			}

		} catch (IOException e) {
			ErrorLogger.logError("Error reading Excel file: " + e.getMessage(), "ExcelReaderStandalone");
		}
	}

	private UserVO createUserEntityFromRow(Row row) {
		double userId = getNumericCellValue(row.getCell(0));
		String username = getStringCellValue(row.getCell(1));
		String userAddress = getStringCellValue(row.getCell(2));

		UserVO userEntity = new UserVO();
		userEntity.setUserid(userId);
		userEntity.setUsername(username);
		userEntity.setUseraddress(userAddress);

		return userEntity;
	}

	@SuppressWarnings({ "deprecation" })
	private double getNumericCellValue(Cell cell) {
		if (cell != null) {
			cell.setCellType(CellType.NUMERIC);
			return cell.getNumericCellValue();
		}
		return 0; // Default value
	}

	@SuppressWarnings({ "deprecation" })
	private String getStringCellValue(Cell cell) {
		if (cell != null) {
			cell.setCellType(CellType.STRING);
			return cell.getStringCellValue();
		}
		return null; // Default value
	}

	private void processUserEntity(UserVO userEntity) {
		try {
			// Check if the user already exists in the database
			UserVO existingUser = getUserFromDatabase(userEntity.getUserid());

			if (existingUser != null) {
				// Update existing user
				existingUser.setUsername(userEntity.getUsername());
				existingUser.setUseraddress(userEntity.getUseraddress());
				try {
					databaseUtil.updateUser(existingUser);
					successfulRecordsCount++;
				} catch (Exception e) {
					ErrorLogger.logError(e.getMessage(), "ExcelReaderStandalone");
				}
			} else {
				databaseUtil.insertUser(userEntity);
			}
		} catch (Exception e) {
			ErrorLogger.logError(e.getMessage(), "ExcelReaderStandalone");
		}
	}

	private UserVO getUserFromDatabase(double userId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM UserVO WHERE userId = :userId";
			Query<UserVO> query = session.createQuery(hql, UserVO.class);
			query.setParameter("userId", userId);

			UserVO user = query.uniqueResult();
			return user;
		} catch (Exception e) {
			ErrorLogger.logError(e.getMessage(), "ExcelReaderStandalone");
			return null;
		}
	}

	public int getSuccessfulRecordsCount() {
		return successfulRecordsCount;
	}

	public int getErrorRecordsCount() {
		return errorRecordsCount;
	}
}
