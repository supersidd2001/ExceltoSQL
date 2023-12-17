package com.my.qfc.common.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.my.qfc.common.vo.UserEntity;

public class DatabaseUtil {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);

	private static final int BATCH_SIZE = 20;

	public void insertUser(UserEntity userEntity) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			session.save(userEntity);
			transaction.commit();
		} catch (Exception e) {
		}
	}

	public void updateUser(UserEntity userEntity) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			session.update(userEntity);
			transaction.commit();
		} catch (Exception e) {
		}
	}

	public void batchInsertUsers(List<UserEntity> users) {
		EntityManager entityManager = HibernateUtil.getSessionFactory().createEntityManager();
		EntityTransaction transaction = null;

		try {
			transaction = entityManager.getTransaction();
			transaction.begin();

			int count = 0;

			for (UserEntity user : users) {
				entityManager.persist(user);

				if (++count % BATCH_SIZE == 0) {
					entityManager.flush();
					entityManager.clear();
				}
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			logger.error("Error batch inserting users: {}", e.getMessage(), e);
			// Handle exception
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
	}

}
