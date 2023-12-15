package com.my.qfc.service.dao;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.my.qfc.common.vo.UserEntity;

@Service
public class BulkInsertService {

    private final BulkInsertDAO userEntityDAO;

    public BulkInsertService(BulkInsertDAO userEntityDAO) {
        this.userEntityDAO = userEntityDAO;
    }

    public void bulkInsertFromExcel(String filePath) throws IOException {
        // Your logic to read and process the Excel file using Apache POI
        // Example:
         ExcelProcessor excelProcessor = new ExcelProcessor();
         List<UserEntity> userEntities = excelProcessor.readExcel(filePath);

        // Perform any required data processing or validation

        // Perform bulk insert into the database
        userEntityDAO.bulkInsertUserEntities(userEntities);
    }
}
