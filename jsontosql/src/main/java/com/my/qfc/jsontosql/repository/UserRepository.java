package com.my.qfc.jsontosql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.qfc.jsontosql.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
