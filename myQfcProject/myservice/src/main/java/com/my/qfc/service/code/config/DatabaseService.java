package com.my.qfc.service.code.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.qfc.common.vo.UserEntity;
import com.my.qfc.common.vo.UserRepository;

@Service
public class DatabaseService {

	private final UserRepository userRepository;

	@Autowired
	public DatabaseService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void batchInsertUsers(List<UserEntity> users) {
		userRepository.saveAll(users);
	}
}
