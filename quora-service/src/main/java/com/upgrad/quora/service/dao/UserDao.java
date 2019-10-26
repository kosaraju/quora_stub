package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {


    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    @OnDelete(action = OnDeleteAction.CASCADE)
    public UserEntity deleteUser(UserEntity userEntity) {
        entityManager.remove(userEntity);
        return userEntity;
    }

    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


    public UserEntity getUserByUUID(final String uuid) {
        try {
            return entityManager.createNamedQuery("userByUUID", UserEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUserByUsername(final String username) {
        try {
            return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }


    public UserAuthEntity getUserByToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void updateUser(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    public void updateUserAuthEntity(final UserAuthEntity updatedUserAuthEntity) {
        entityManager.merge(updatedUserAuthEntity);
    }

}
