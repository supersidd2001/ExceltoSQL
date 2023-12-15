package com.my.qfc.service.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.my.qfc.common.vo.UserEntity;

@Repository
public class BulkInsertDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void bulkInsertUserEntities(List<UserEntity> userEntities) {
        int batchSize = 50; // Set your desired batch size

        for (int i = 0; i < userEntities.size(); i++) {
            UserEntity userEntity = userEntities.get(i);
            entityManager.persist(userEntity);

            // Flush and clear the session to prevent memory issues in large batches
            if (i % batchSize == 0 && i > 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        // Flush any remaining entities
        entityManager.flush();
        entityManager.clear();
    }
    
    public List<UserEntity> readExcelFile(String filename) throws IOException {
        List<UserEntity> userEntities = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(filename))) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Skip header row
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Assuming the columns are in the order: userid, username, useraddress
                double userid = row.getCell(0).getNumericCellValue();
                String username = row.getCell(1).getStringCellValue();
                String useraddress = row.getCell(2).getStringCellValue();

                UserEntity userEntity = new UserEntity(userid, username, useraddress);
                userEntities.add(userEntity);
            }
        }

        return userEntities;
    }
}
