package com.my.qfc.common.bulkinsert.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.my.qfc.common.util.HibernateUtil;
import com.my.qfc.common.vo.UserEntity;
import com.my.qfc.common.vo.UserVO;

public class BulkInsertDAO {

	public void bulkInsertUsers(List<UserEntity> users) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();

			for (UserEntity user : users) {
				session.save(user);
			}
			transaction.commit();
		} catch (Exception e) {
		}
	}
}
