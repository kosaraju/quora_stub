package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {
    @Autowired
    private AnswerDao answerDao;
    @Autowired
    private UserDao userDao;

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
    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteAnswer(final String answerUUID,final String accessToken) throws AnswerNotFoundException, AuthorizationFailedException {
        AnswerEntity answerEntity = answerDao.getAnswer(answerUUID);
        UserAuthEntity userByToken = userDao.getUserByToken(accessToken);

        if(answerEntity==null)
        {
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        if(userByToken.getUser().getId() != answerEntity.getUserEntity().getId() && ! answerEntity.getUserEntity().getRole().equalsIgnoreCase("admin") ) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }

        return answerUUID;
    }
}
