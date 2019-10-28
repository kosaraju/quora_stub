package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private QuestionService questionService;

    /*
    Handler to post a Question
     */
    @RequestMapping(method = RequestMethod.POST, path = "/question/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> postQuestion(@RequestHeader("authorization") final String authorization, final QuestionRequest questionRequest) throws AuthorizationFailedException, AuthenticationFailedException, InvalidQuestionException {

        //Get the bearer access token
        String accessToken = authenticationService.getBearerAccessToken(authorization);

        //Validate the bearer authentication
        UserAuthEntity userAuthEntity = authenticationService.validateBearerAuthentication(accessToken, "to post a question");

        //Get the user details and fill question detail, associate user
        UserEntity user = userAuthEntity.getUser();
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setDate(ZonedDateTime.now());
        questionEntity.setUser(user);

        //Invoke business service to create question. If same question already exists, throw DuplicateQuestion related Exception message
        questionService.createQuestion(questionEntity);
        QuestionResponse questionResponse = new QuestionResponse().id(questionEntity.getUuid()).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse,HttpStatus.OK);
    }

    /*
    Handler to edit the content of the question
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String questionId, final QuestionEditRequest questionEditRequest) throws AuthorizationFailedException, InvalidQuestionException,AuthenticationFailedException {

        //Get bearer access token
        String accessToken = authenticationService.getBearerAccessToken(authorization);

        //Validate bearer authentication token
        UserAuthEntity userAuthEntity = authenticationService.validateBearerAuthentication(accessToken, "to edit the question");
        UserEntity user = userAuthEntity.getUser();

        //Invoke business Service to edit the question
        QuestionEntity questionEntity = questionService.editQuestion(questionEditRequest.getContent(), user.getId(),  questionId);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(questionEntity.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse,HttpStatus.OK);
    }

    /*
    Handler to get all the questions
    */
    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AuthenticationFailedException {

        //Get bearer access token
        String accessToken = authenticationService.getBearerAccessToken(authorization);

        //Validate bearer authentication token
        UserAuthEntity userAuthEntity = authenticationService.validateBearerAuthentication(accessToken, "to get all questions");

        //Invoke business Service to get all the questions and add them to a collection and send across in ResponseEntity
        List<QuestionEntity> questionEntityList = questionService.getAllQuestions();
        List<QuestionDetailsResponse> entities = new ArrayList<QuestionDetailsResponse>();
        for (QuestionEntity n : questionEntityList) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
            questionDetailsResponse.id(n.getUuid());
            questionDetailsResponse.content(n.getContent());
            entities.add(questionDetailsResponse);
        }

        return new ResponseEntity<List<QuestionDetailsResponse>>(entities, HttpStatus.OK);
    }
}

