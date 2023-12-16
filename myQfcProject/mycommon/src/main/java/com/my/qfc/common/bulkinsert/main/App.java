package com.my.qfc.common.bulkinsert.main;

import com.my.qfc.common.bulkinsert.batchprocessing.BatchInsertProcessor;
import com.my.qfc.common.bulkinsert.batchprocessing.BulkInsertHandler;
import com.my.qfc.common.bulkinsert.dao.BulkInsertDAO;
import com.my.qfc.common.util.DatabaseUtil;

public class App {

    public static void main(String[] args) {
        // Instantiate necessary components
        BulkInsertDAO bulkInsertDAO = new BulkInsertDAO();
        DatabaseUtil databaseUtil = new DatabaseUtil(); // Adjust instantiation based on your needs
        BatchInsertProcessor batchInsertProcessor = new BatchInsertProcessor();
        BulkInsertHandler bulkInsertHandler = new BulkInsertHandler(bulkInsertDAO, batchInsertProcessor, databaseUtil);

        // Provide the path to your Excel file
        String excelFilePath = "C:\\Users\\Siddharth Shinde\\Desktop\\Springmaven\\myQfcProject\\myQfcProject\\mycommon\\src\\main\\resources\\success.xlsx";

        // Handle bulk insert from Excel
        bulkInsertHandler.handleBulkInsertFromExcel(excelFilePath);

        // Optionally, you can print or use the results
        System.out.println("Bulk insert complete");
    }
}
