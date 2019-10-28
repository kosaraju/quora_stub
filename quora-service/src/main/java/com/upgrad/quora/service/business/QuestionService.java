package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity) throws InvalidQuestionException {
        String content = questionEntity.getContent();
        if (questionDao.getQuestionByContent(content.trim()) != null) {
            throw new InvalidQuestionException("QUE-999", "Question already exists. Duplicate question not allowed");
        }

        if (content == null || content.isEmpty() || content.trim().isEmpty()) {
            throw new InvalidQuestionException("QUE-888", "Content can't be null or empty");
        }
        return questionDao.createQuestion(questionEntity);
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestion(String content, long userId, String questionUUID) throws AuthorizationFailedException, InvalidQuestionException{
        QuestionEntity questionEntity = questionDao.getQuestion(questionUUID);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        if (userId != questionEntity.getUser().getId()) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
        if (content == null || content.isEmpty() || content.trim().isEmpty() || content.equalsIgnoreCase(questionEntity.getContent())) {
            throw new InvalidQuestionException("QUE-888", "Content can't be null or empty or equal to existing content");
        }
        questionEntity.setContent(content);
        questionDao.updateQuestion(questionEntity);
        return questionDao.getQuestion(questionUUID);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions() {
        return questionDao.findAll();
    }

}
