package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> login(@PathVariable("userId") final String userId, @RequestHeader("authorization") final String authorization) throws UserNotFoundException, AuthorizationFailedException, AuthenticationFailedException {
        String[] tokens = authorization.split("Bearer ");
        String accessToken = null;
        try{
            accessToken = tokens[1];
        }catch(IndexOutOfBoundsException ie){
            //throw new AuthenticationFailedException("ATH-005","Use format: 'Bearer JWTToken'");
        }
        //Check if the bearer authentication exists
        authenticationService.validateBearerAuthentication(accessToken);

        // search userByUuid
        UserEntity userEntity = userService.getUserProfile(userId);


        UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
        userDetailsResponse.setUserName(userEntity.getUsername());
        userDetailsResponse.setAboutMe(userEntity.getAboutme());
        userDetailsResponse.setContactNumber(userEntity.getContactnumber());
        userDetailsResponse.setCountry(userEntity.getCountry());
        userDetailsResponse.setDob(userEntity.getDob());
        userDetailsResponse.setEmailAddress(userEntity.getEmail());
        userDetailsResponse.setFirstName(userEntity.getFirstName());
        userDetailsResponse.setLastName(userEntity.getLastName());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }

}
