package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Repository;

@SuppressWarnings("JpaQueryApiInspection")
@Repository
public class AnswerDao {

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * @param answerEntity
   * @return
   */
  public AnswerEntity createAnswer(AnswerEntity answerEntity) {
    entityManager.persist(answerEntity);
    return answerEntity;
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
   * @param answerUUID
   * @return
   */
  public AnswerEntity getAnswer(final String answerUUID) {
    try {
      return entityManager.createNamedQuery("AnswerEntityByUuid", AnswerEntity.class)
          .setParameter("uuid", answerUUID).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * @param Id
   * @return
   */
  public AnswerEntity getAnswerById(final long Id) {
    try {
      return entityManager.createNamedQuery("AnswerEntityByid", AnswerEntity.class)
          .setParameter("id", Id).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * @param answerEntity
   * @return
   */
  public AnswerEntity updateAnswer(final AnswerEntity answerEntity) {
    return entityManager.merge(answerEntity);
  }

  /**
   * @param questionEntity
   * @return
   */
  public List<AnswerEntity> AnswerEntityByQuestionEntity(final QuestionEntity questionEntity) {
    return entityManager
        .createNamedQuery(
            "AnswerEntityByQuestionEntity",
            AnswerEntity.class)
        .setParameter(
            "questionEntity",
            questionEntity)
        .getResultList();
  }

  /**
   * @param answerEntity
   * @return
   */
  @OnDelete(action = OnDeleteAction.CASCADE)
  public AnswerEntity deleteAnswer(final AnswerEntity answerEntity) {
    entityManager.remove(answerEntity);
    return answerEntity;
  }
}
