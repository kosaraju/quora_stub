package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerService {
    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        return answerDao.createAnswer(answerEntity);
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    public AnswerEntity editAnswer(
            String content,
            UserEntity user,
            String answerUuid
    ) throws AuthorizationFailedException,
            AnswerNotFoundException {
        AnswerEntity answerEntity = answerDao.getAnswer(answerUuid);
        if(answerEntity == null){
            throw new AnswerNotFoundException(
                    "ANS-001","Entered answer uuid does not exist"
            );
        }

        if (!answerEntity.getUserEntity().getUuid().equals(user.getUuid())) {
            throw new AuthorizationFailedException(
                    "ATHR-003",
                    "Only the answer owner can edit the answer"
            );
        }

        answerEntity.setAns(content);
        return answerDao.updateAnswer(answerEntity);
    }

    public QuestionEntity getQuestionById(String questionUUID) throws InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.getQuestion(questionUUID);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }
        return questionEntity;
    }

    public List<AnswerEntity> getAllAnswersToQuestion(QuestionEntity questionEntity) {
        List<AnswerEntity> answerEntitieList = new ArrayList<>();
        answerEntitieList = answerDao.AnswerEntityByQuestionEntity(questionEntity);

        return answerEntitieList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(String userId, String answerUUID) throws AnswerNotFoundException, AuthorizationFailedException {
        AnswerEntity answerEntity = answerDao.getAnswer(answerUUID);
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        if (!userId.equals(answerEntity.getUserEntity().getUuid()) && !answerEntity.getUserEntity().getRole().equalsIgnoreCase("admin")) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }

        return answerDao.deleteAnswer(answerEntity);
    }

}
