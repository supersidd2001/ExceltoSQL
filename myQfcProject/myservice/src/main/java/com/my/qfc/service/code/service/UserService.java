package com.my.qfc.service.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.my.qfc.common.util.DatabaseUtil;
import com.my.qfc.common.util.ErrorLogger;
import com.my.qfc.common.util.ExcelReaderStandalone;
import com.my.qfc.common.vo.UserEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class UserService {

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${batch.size}")
	private int batchSize;

	private final DatabaseUtil databaseUtil;

	public UserService(DatabaseUtil databaseUtil) {
		this.databaseUtil = databaseUtil;
	}

	public void processExcelFiles(List<String> filePaths) {
		ExcelReaderStandalone excelReader = new ExcelReaderStandalone(databaseUtil);

		for (String filePath : filePaths) {
			try {
				excelReader.processExcelFile(filePath);
				System.out.println("processed : " + filePath);
			} catch (Exception e) {
				ErrorLogger.logError("Processing Excel", "Error processing Excel file", -1, "N/A", filePath);
			}
		}

		List<UserEntity> users = excelReader.getProcessedUserEntities();
		batchInsertUsers(users);
	}

	@Transactional
	public void batchInsertUsers(List<UserEntity> users) {
		int count = 0;

		for (UserEntity user : users) {
			entityManager.persist(user);

			// Flush and clear after a certain number of inserts
			if (++count % batchSize == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}
	}
}
