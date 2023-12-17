// com.my.qfc.common.batchinsert.dao.ExcelBatchInsertDAO.java
package com.my.qfc.common.batchinsert.dao;

import java.util.List;

import com.my.qfc.common.util.DatabaseUtil;
import com.my.qfc.common.vo.UserEntity;

public class ExcelBatchInsertDAO {

	private final DatabaseUtil databaseUtil;

	public ExcelBatchInsertDAO(DatabaseUtil databaseUtil) {
		this.databaseUtil = databaseUtil;
	}

	// Use the batchInsertUsers method from DatabaseUtil
	public void batchInsertUsers(List<UserEntity> users) {
		databaseUtil.batchInsertUsers(users);
	}
}
