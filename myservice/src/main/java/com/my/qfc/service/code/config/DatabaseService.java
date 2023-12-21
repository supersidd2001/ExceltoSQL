package com.my.qfc.service.code.config;

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

	public void insertUser(UserEntity userEntity) {
		userRepository.save(userEntity);
	}

	public void updateUser(UserEntity userEntity) {
		userRepository.save(userEntity);
	}

	public UserEntity getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	public Iterable<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}
}
