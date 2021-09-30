package com.bloggingapp.services;

import com.bloggingapp.entities.UserEntity;
import com.bloggingapp.models.requestModels.UserLoginRequestModel;
import com.bloggingapp.models.requestModels.UserRequestModel;
import com.bloggingapp.models.responseModels.UserLoginResponseModel;
import com.bloggingapp.models.responseModels.UserResponseModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    void initRoleAndUser();
    UserEntity saveUser(UserRequestModel userRequestModel);
}
