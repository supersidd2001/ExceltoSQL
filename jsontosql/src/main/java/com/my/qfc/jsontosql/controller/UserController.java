package com.my.qfc.jsontosql.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.qfc.jsontosql.entity.UserEntity;
import com.my.qfc.jsontosql.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping
	public ResponseEntity<String> createUser(@RequestBody UserEntity userEntity) {
		try {
			userRepository.save(userEntity);
			return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Error creating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
