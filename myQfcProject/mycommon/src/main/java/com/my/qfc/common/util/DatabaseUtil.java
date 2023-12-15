package com.my.qfc.common.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.my.qfc.common.vo.UserEntity;

public class DatabaseUtil {

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
}
