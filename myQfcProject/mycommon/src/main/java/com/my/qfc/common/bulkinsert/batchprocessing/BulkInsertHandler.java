package com.my.qfc.common.bulkinsert.batchprocessing;

import java.util.List;

import com.my.qfc.common.bulkinsert.dao.BulkInsertDAO;
import com.my.qfc.common.util.DatabaseUtil;
import com.my.qfc.common.util.ExcelReaderStandalone;
import com.my.qfc.common.vo.UserEntity;

public class BulkInsertHandler {

    private final BulkInsertDAO bulkInsertDAO;
    private final BatchInsertProcessor batchInsertProcessor;
    private final DatabaseUtil databaseUtil;

    public BulkInsertHandler(BulkInsertDAO bulkInsertDAO, BatchInsertProcessor batchInsertProcessor, DatabaseUtil databaseUtil) {
        this.bulkInsertDAO = bulkInsertDAO;
        this.batchInsertProcessor = batchInsertProcessor;
        this.databaseUtil = databaseUtil;
    }

    public void handleBulkInsertFromExcel(String excelFilePath) {
        // Read data from Excel using ExcelReaderStandalone
        ExcelReaderStandalone excelReader = new ExcelReaderStandalone(databaseUtil);

        try {
            excelReader.processExcelFile(excelFilePath);
            List<UserEntity> users = excelReader.getUsers(); // Assuming a method to retrieve users from the ExcelReader

            // Call bulk insert using DAO
            bulkInsertDAO.bulkInsertUsers(users);

            // Call batch insertion using BatchInsertProcessor
            batchInsertProcessor.batchInsertUsers(users);

            // Additional logic if needed
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        }
    }
}
