package com.upgrad.quora.api.controller;
import com.upgrad.quora.api.model.*;
import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.api.model.AnswerEditResponse;
import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.POST,
            path = "/question/{questionId}/answer/create",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> postAnswer(
            @RequestHeader("authorization") final String authorization,
            @PathVariable("questionId") final String questionId,
            final AnswerRequest answerRequest
    ) throws  AuthorizationFailedException,AuthenticationFailedException,InvalidQuestionException {
        String accessToken = authenticationService.getBearerAccessToken(authorization);
        //Check if the bearer authentication exists
        UserAuthEntity userAuthEntity = authenticationService.validateBearerAuthentication(accessToken, "to post a answer");
        QuestionEntity questionEntity = questionService.getQuestionById(questionId);

        UserEntity user = userAuthEntity.getUser();
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setAns(answerRequest.getAnswer());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setUserEntity(user);
        answerEntity.setQuestionEntity(questionEntity);

        // create answer
        answerService.createAnswer(answerEntity);
        AnswerResponse answerResponse = new AnswerResponse().id(answerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse,HttpStatus.OK);
    }

    //PUT Request
    @RequestMapping(method = RequestMethod.PUT,
            path = "/answer/edit/{answerId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse>
    editAnswer(@RequestHeader("authorization") final String authorization,
               @PathVariable("answerId") final String answerId,
               final AnswerEditRequest answerEditRequest
    ) throws AuthorizationFailedException,
            InvalidQuestionException,
            AuthenticationFailedException, AnswerNotFoundException {

        String accessToken =
                authenticationService.getBearerAccessToken(authorization);
        //Check if the bearer authentication exists
        UserAuthEntity userAuthEntity =
                authenticationService.validateBearerAuthentication(
                        accessToken,
                        "to edit the answer"
                );
        UserEntity user = userAuthEntity.getUser();
        // Edit question
        AnswerEntity answerEntity =
                answerService.editAnswer(
                        answerEditRequest.getContent(),
                        user,
                        answerId
                );
        AnswerEditResponse answerEditResponse =
                new AnswerEditResponse()
                        .id(answerEntity.getUuid())
                        .status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse,HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.DELETE,path="answer/delete/{answerId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestHeader("authorization") final String authorization,@PathVariable ( "answerId") final String answerId) throws AuthenticationFailedException, AuthorizationFailedException, AnswerNotFoundException {
        String accessToken = authenticationService.getBearerAccessToken(authorization);
        UserAuthEntity userAuthEntity= authenticationService.validateBearerAuthentication(accessToken, "to delete the answer");
        String answerEntity = answerService.deleteAnswer(answerId,accessToken);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerEntity).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK); // return
    }


}

