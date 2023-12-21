package com.my.qfc.common.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.my.qfc.common.vo.UserEntity;

@Component
public class DatabaseUtil {

	private final SessionFactory sessionFactory;

	@Autowired
	public DatabaseUtil(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void insertUser(UserEntity userEntity) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.save(userEntity);
			session.getTransaction().commit();
		}
	}

	public void updateUser(UserEntity userEntity) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.update(userEntity);
			session.getTransaction().commit();
		}
	}

	// Add other database-related methods as needed
}
