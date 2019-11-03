package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDao {

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * @param questionEntity
   * @return
   */
  public QuestionEntity createQuestion(QuestionEntity questionEntity) {
    entityManager.persist(questionEntity);
    return questionEntity;
  }

  /**
   * @param accesstoken
   * @return
   */
  public UserAuthEntity getUserAuthToken(final String accesstoken) {
    try {
      return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class)
          .setParameter("accessToken", accesstoken).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * @param questionUUID
   * @return
   */
  public QuestionEntity getQuestion(final String questionUUID) {
    try {
      return entityManager.createNamedQuery("QuestionEntityByUuid", QuestionEntity.class)
          .setParameter("uuid", questionUUID).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * @param Id
   * @return
   */
  public QuestionEntity getQuestionById(final long Id) {
    try {
      return entityManager.createNamedQuery("QuestionEntityByid", QuestionEntity.class)
          .setParameter("id", Id).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * @param content
   * @return
   */
  public QuestionEntity getQuestionByContent(final String content) {
    try {
      return entityManager.createNamedQuery("QuestionEntityByContent", QuestionEntity.class)
          .setParameter("content", content).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * @param questionEntity
   * @return
   */
  public QuestionEntity updateQuestion(final QuestionEntity questionEntity) {
    return entityManager.merge(questionEntity);
  }

  /**
   * @return
   */
  public List<QuestionEntity> findAll() {
    return entityManager.createQuery("SELECT a FROM QuestionEntity a", QuestionEntity.class)
        .getResultList();
  }

  /**
   * @param user
   * @return
   */
  public List<QuestionEntity> findAllByUser(UserEntity user) {
    return entityManager.createNamedQuery("QuestionEntitiesByUser", QuestionEntity.class)
        .setParameter("user", user).getResultList();
  }

  /**
   * @param questionEntity
   * @return
   */
  public QuestionEntity deleteQuestion(final QuestionEntity questionEntity) {
    entityManager.remove(questionEntity);
    return questionEntity;
  }

}
