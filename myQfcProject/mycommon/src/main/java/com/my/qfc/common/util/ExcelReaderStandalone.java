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

import com.my.qfc.common.vo.UserEntity;

public class ExcelReaderStandalone {

	private final DatabaseUtil databaseUtil;
	private static int successfulRecordsCount = 0;
	private static int errorRecordsCount = 0;
	private List<UserEntity> users = new ArrayList<>();

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
					UserEntity UserVO = createUserEntityFromRow(row, file.toString());
					processUserEntity(UserVO);
				}
			}

		} catch (IOException e) {
		}
		
		 try (FileInputStream fileInputStream = new FileInputStream(file)) {
	            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
	            XSSFSheet sheet = workbook.getSheetAt(0);

	            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
	                XSSFRow row = sheet.getRow(rowIndex);
	                if (row != null) {
	                    UserEntity userEntity = createUserEntityFromRow(row, file.toString());
	                    users.add(userEntity); // Add the user to the list
	                    processUserEntity(userEntity);
	                }
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

	private UserEntity createUserEntityFromRow(Row row, String fileName) {
		double userId = getNumericCellValue(row.getCell(0), "UserID", fileName);
		String username = getStringCellValue(row.getCell(1), "UserName", fileName);
		String userAddress = getStringCellValue(row.getCell(2), "UserAddress", fileName);

		UserEntity UserVO = new UserEntity();
		UserVO.setUserid(userId);
		UserVO.setUsername(username);
		UserVO.setUseraddress(userAddress);

		return UserVO;
	}

	@SuppressWarnings({ "deprecation", "null" })
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

	@SuppressWarnings({ "deprecation", "null" })
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

	private void processUserEntity(UserEntity uservo) {
		try {
			// Check if the user already exists in the database
			UserEntity existingUser = getUserFromDatabase(uservo.getUserid());

			if (existingUser != null) {
				existingUser.setId(uservo.getId());
				existingUser.setUsername(uservo.getUsername());
				existingUser.setUseraddress(uservo.getUseraddress());
				existingUser.setUserid(uservo.getUserid());
				try {
					databaseUtil.updateUser(existingUser);
					successfulRecordsCount++;
				} catch (Exception e) {
				}
			} else {
				databaseUtil.insertUser(uservo);
				successfulRecordsCount++;
			}
		} catch (Exception e) {
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

	public int getSuccessfulRecordsCount() {
		return successfulRecordsCount;
	}

	public int getErrorRecordsCount() {
		return errorRecordsCount;
	}

	private String extractFilename(String filePath) {
		File file = new File(filePath);
		String originalFilename = file.getName();

		String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");

		return sanitizedFilename;
	}
	
	public List<UserEntity> getUsers() {
        return users;
    }

}