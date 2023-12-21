// src/main/java/com/my/qfc/common/vo/UserRepository.java
package com.my.qfc.common.vo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
