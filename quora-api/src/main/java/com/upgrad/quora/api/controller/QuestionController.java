package com.upgrad.quora.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private QuestionService questionService;
    @RequestMapping(method = RequestMethod.POST, path = "/question/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> login(@RequestHeader("authorization") final String authorization, final QuestionRequest questionRequest) throws  AuthorizationFailedException,AuthenticationFailedException{
    String accessToken = authenticationService.getBearerAccessToken(authorization);
        //Check if the bearer authentication exists
        UserAuthEntity userAuthEntity = authenticationService.checkAuthenticationforCreateQuestion(accessToken);

        UserEntity user = userAuthEntity.getUser();
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setDate(ZonedDateTime.now());
        questionEntity.setUser(user);

        // create question
        questionService.createQuestion(questionEntity);
        QuestionResponse questionResponse = new QuestionResponse().id(questionEntity.getUuid()).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse,HttpStatus.OK);
    }
    
        //PUT Request
    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> login(@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String questionId, final QuestionEditRequest questionEditRequest) throws AuthorizationFailedException, InvalidQuestionException,AuthenticationFailedException {

        String accessToken = authenticationService.getBearerAccessToken(authorization);
        //Check if the bearer authentication exists
        UserAuthEntity userAuthEntity = authenticationService.checkAuthenticationEditQuestion(accessToken);
        UserEntity user = userAuthEntity.getUser();
        // Edit question
        QuestionEntity questionEntity = questionService.editQuestion(questionEditRequest.getContent(), user.getId(),  questionId);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(questionEntity.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse,HttpStatus.OK);
    }
}

