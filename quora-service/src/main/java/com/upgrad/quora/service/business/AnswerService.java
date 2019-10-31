package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;

    public AnswerEntity deleteAnswer(long userId , String answerUUID) throws AnswerNotFoundException, AuthorizationFailedException {
    AnswerEntity answerEntity = answerDao.getAnswer(answerUUID);
    if(answerEntity==null)
        {
        throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
    if(userId != answerEntity.getUserEntity().getId()|| ! answerEntity.getUserEntity().getRole().equalsIgnoreCase("admin") ) {
        throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
    }

    return answerDao.deleteAnswer(answerEntity);
    }
}
