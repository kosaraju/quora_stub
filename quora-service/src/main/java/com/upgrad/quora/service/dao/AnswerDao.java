package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@SuppressWarnings("JpaQueryApiInspection")
@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public UserAuthEntity getUserAuthToken(final String accesstoken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class).setParameter("accessToken", accesstoken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AnswerEntity getAnswer(final String answerUUID) {
        try {
            return entityManager.createNamedQuery("AnswerEntityByUuid", AnswerEntity.class).setParameter("uuid", answerUUID).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AnswerEntity getAnswerById(final long Id) {
        try {
            return entityManager.createNamedQuery("AnswerEntityByid", AnswerEntity.class).setParameter("id", Id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AnswerEntity updateAnswer(final AnswerEntity answerEntity) {
        return entityManager.merge(answerEntity);
    }

    public AnswerEntity deleteAnswer(final AnswerEntity answerEntity){
        entityManager.remove(answerEntity);
        return answerEntity;
    }
}

