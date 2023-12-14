package com.my.qfc.common.vo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
public class UserEntityDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void bulkInsertUserEntities(List<UserEntity> userEntities) {
        for (UserEntity userEntity : userEntities) {
            entityManager.persist(userEntity);
        }
    }
}
