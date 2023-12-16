package com.my.qfc.common.bulkinsert.batchprocessing;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.my.qfc.common.util.HibernateUtil;
import com.my.qfc.common.vo.UserEntity;

public class BatchInsertProcessor {

	public void batchInsertUsers(List<UserEntity> users) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();

			int batchSize = 20; // You can adjust the batch size based on your requirements
			int count = 0;

			for (UserEntity user : users) {
				session.save(user);

				if (++count % batchSize == 0) {
					session.flush();
					session.clear();
				}
			}

			transaction.commit();
		} catch (Exception e) {
		}
	}
}
