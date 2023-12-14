package com.my.qfc.common.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.my.qfc.common.vo.UserVO;

public class DatabaseUtil {

	public void insertUser(UserVO userEntity) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			session.save(userEntity);
			transaction.commit();
		} catch (Exception e) {
		}
	}

	public void updateUser(UserVO userEntity) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			session.update(userEntity);
			transaction.commit();
		} catch (Exception e) {
		}
	}
}
