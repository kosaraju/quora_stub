package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {


  @PersistenceContext
  private EntityManager entityManager;

  /**
   * @param userEntity
   * @return
   */
  public UserEntity createUser(UserEntity userEntity) {
    entityManager.persist(userEntity);
    return userEntity;
  }

  /**
   * @param userEntity
   * @return
   */
  @OnDelete(action = OnDeleteAction.CASCADE)
  public UserEntity deleteUser(UserEntity userEntity) {
    entityManager.remove(userEntity);
    return userEntity;
  }

  /**
   * @param email
   * @return
   */
  public UserEntity getUserByEmail(final String email) {
    try {
      return entityManager.createNamedQuery("userByEmail", UserEntity.class)
          .setParameter("email", email).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * @param uuid
   * @return
   */
  public UserEntity getUserByUUID(final String uuid) {
    try {
      return entityManager.createNamedQuery("userByUUID", UserEntity.class)
          .setParameter("uuid", uuid).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * @param username
   * @return
   */
  public UserEntity getUserByUsername(final String username) {
    try {
      return entityManager.createNamedQuery("userByUsername", UserEntity.class)
          .setParameter("username", username).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * @param userAuthEntity
   * @return
   */
  public UserAuthEntity createAuthToken(final UserAuthEntity userAuthEntity) {
    entityManager.persist(userAuthEntity);
    return userAuthEntity;
  }

  /**
   * @param accessToken
   * @return
   */
  public UserAuthEntity getUserByToken(final String accessToken) {
    try {
      return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class)
          .setParameter("accessToken", accessToken).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * @param updatedUserEntity
   */
  public void updateUser(final UserEntity updatedUserEntity) {
    entityManager.merge(updatedUserEntity);
  }

  /**
   * @param updatedUserAuthEntity
   */
  public void updateUserAuthEntity(final UserAuthEntity updatedUserAuthEntity) {
    entityManager.merge(updatedUserAuthEntity);
  }

}
