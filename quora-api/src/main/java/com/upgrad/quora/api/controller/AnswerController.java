package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerDeleteResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AnswerService answerService;

    @RequestMapping(method = RequestMethod.DELETE,path="answer/delete/{answerId}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestHeader("authorization") final String authorization,@RequestHeader ( "answerId") final String answerId) throws AuthenticationFailedException, AuthorizationFailedException, AnswerNotFoundException {
        String accessToken = authenticationService.getBearerAccessToken(authorization);
        UserAuthEntity userAuthEntity= authenticationService.validateBearerAuthentication(accessToken, "to delete the answer");
        UserEntity user = userAuthEntity.getUser();
        AnswerEntity answerEntity = answerService.deleteAnswer(user.getId(), answerId);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerEntity.getUuid()).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }

}
