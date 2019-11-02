package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public UserAuthEntity getUserAuthToken(final String accesstoken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class).setParameter("accessToken", accesstoken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity getQuestion(final String questionUUID) {
        try {
            return entityManager.createNamedQuery("QuestionEntityByUuid", QuestionEntity.class).setParameter("uuid", questionUUID).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity getQuestionById(final long Id) {
        try {
            return entityManager.createNamedQuery("QuestionEntityByid", QuestionEntity.class).setParameter("id", Id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity getQuestionByContent(final String content) {
        try {
            return entityManager.createNamedQuery("QuestionEntityByContent", QuestionEntity.class).setParameter("content", content).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


    public QuestionEntity updateQuestion(final QuestionEntity questionEntity) {
        return entityManager.merge(questionEntity);
    }


    public List<QuestionEntity> findAll() {
        return entityManager.createQuery("SELECT a FROM QuestionEntity a", QuestionEntity.class).getResultList();
    }

    public QuestionEntity deleteQuestion(final QuestionEntity questionEntity){
        entityManager.remove(questionEntity);
        return questionEntity;
    }

}
