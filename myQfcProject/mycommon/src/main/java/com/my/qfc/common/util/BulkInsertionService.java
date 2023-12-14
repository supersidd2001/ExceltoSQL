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

import com.my.qfc.common.vo.UserEntity;
import com.my.qfc.common.vo.UserEntityDAO;

public class BulkInsertionService {

    private final UserEntityDAO userEntityDAO;

    public BulkInsertionService(UserEntityDAO userEntityDAO) {
        this.userEntityDAO = userEntityDAO;
    }

    public void bulkInsertFromExcel(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

            XSSFSheet sheet = workbook.getSheetAt(0);

            List<UserEntity> userEntities = createUserEntitiesFromSheet(sheet);
            userEntityDAO.bulkInsertUserEntities(userEntities);

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your requirements
        }
    }

    private List<UserEntity> createUserEntitiesFromSheet(XSSFSheet sheet) {
        // Assuming your Excel sheet has headers in the first row
        int firstDataRowIndex = 1;

        List<UserEntity> userEntities = new ArrayList<>();

        for (int rowIndex = firstDataRowIndex; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            XSSFRow row = sheet.getRow(rowIndex);

            if (row != null) {
                UserEntity userEntity = createUserEntityFromRow(row);
                userEntities.add(userEntity);
            }
        }

        return userEntities;
    }

    private UserEntity createUserEntityFromRow(Row row) {
        double userId = getNumericCellValue(row.getCell(0));
        String username = getStringCellValue(row.getCell(1));
        String userAddress = getStringCellValue(row.getCell(2));

        UserEntity userEntity = new UserEntity();
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
}
