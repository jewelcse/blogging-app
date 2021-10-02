package com.bloggingapp.services;

import com.bloggingapp.entities.UserEntity;
import com.bloggingapp.models.requestModels.UserRegisterRequestModel;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void initRoleAndUser();
    UserEntity saveUser(UserRegisterRequestModel userRegisterRequestModel);
    UserEntity createNewAdmin(UserRegisterRequestModel userRegisterRequestModel);
    Optional<UserEntity> findUserById(Long userId);
    void approveUserAccountByAdmin(Long userId);
    Optional<UserEntity> findUserByUsername(String name);
    List<UserEntity> getAllUser();
}
