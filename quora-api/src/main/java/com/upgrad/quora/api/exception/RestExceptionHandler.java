package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.UserSignupException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(UserSignupException.class)
    public ResponseEntity<ErrorResponse> userSignupException(UserSignupException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.BAD_REQUEST
        );
    }
//
//    @ExceptionHandler(AuthenticationFailedException.class)
//    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException exc, WebRequest request) {
//        return new ResponseEntity<ErrorResponse>(
//                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED
//        );
//    }
//
//    @ExceptionHandler(UploadFailedException.class)
//    public ResponseEntity<ErrorResponse> uploadFailedException(UploadFailedException exc, WebRequest request) {
//        return new ResponseEntity<ErrorResponse>(
//                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED
//        );
//    }
//
//    @ExceptionHandler(ImageNotFoundException.class)
//    public ResponseEntity<ErrorResponse> imagenotfoundException(ImageNotFoundException exc, WebRequest request) {
//        return new ResponseEntity<ErrorResponse>(
//                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND
//        );
//    }
//
//    @ExceptionHandler(UserNotSignedInException.class)
//    public ResponseEntity<ErrorResponse> usernotsignedinException(UserNotSignedInException exc, WebRequest request) {
//        return new ResponseEntity<ErrorResponse>(
//                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED
//        );
//    }
//
//    @ExceptionHandler(UnauthorizedException.class)
//    public ResponseEntity<ErrorResponse> unauthorizedException(UnauthorizedException exc, WebRequest request) {
//        return new ResponseEntity<ErrorResponse>(
//                new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.FORBIDDEN
//        );
//    }

}