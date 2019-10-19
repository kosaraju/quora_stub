package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {

        //Perform null check for mandatory fields
        if (userEntity == null || userEntity.getFirstName() == null || userEntity.getLastName() == null ||
                userEntity.getUsername()==null || userEntity.getEmail() == null || userEntity.getPassword() == null ||
                userEntity.getFirstName().isEmpty() || userEntity.getLastName().isEmpty() ||
                userEntity.getEmail().isEmpty() || userEntity.getPassword().isEmpty() || userEntity.getUsername().isEmpty()
                ) {
            throw new SignUpRestrictedException("USE-001", "No input provided for required fields");
        }

        //If user already exists with same username or email, throw respective exceptions
        UserEntity existingUser1 = userDao.getUserByEmail(userEntity.getEmail());
        UserEntity existingUser2 = userDao.getUserByUsername(userEntity.getUsername());

        if(existingUser1!=null && existingUser2!=null){
            throw new SignUpRestrictedException("USE-002","Username & Email id already exists");
        }else if(existingUser2!=null){
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
        }else if(existingUser1!=null){
            throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
        }

        String[] encryptedText = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);

        return userDao.createUser(userEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity getUserProfile(String uuid) throws UserNotFoundException {
        UserEntity userEntity = userDao.getUserByUUID(uuid);
        if(userEntity == null){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }
        return userEntity;
    }


    }
