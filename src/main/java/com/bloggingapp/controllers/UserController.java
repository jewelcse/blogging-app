package com.bloggingapp.controllers;


import com.bloggingapp.entities.UserEntity;
import com.bloggingapp.exceptions.ApplicationException;
import com.bloggingapp.models.requestModels.UserLoginRequestModel;
import com.bloggingapp.models.requestModels.UserRequestModel;
import com.bloggingapp.models.responseModels.UserLoginResponseModel;
import com.bloggingapp.services.RoleService;
import com.bloggingapp.services.UserService;
import com.bloggingapp.servicesImpl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;
    private JwtService jwtService;
    private RoleService roleService;

    @Autowired
    public UserController(UserService service, RoleService roleService, JwtService jwtService) {
        this.userService = service;
        this.roleService = roleService;
        this.jwtService = jwtService;
    }

    @PostConstruct
    public void initRoleAndUser() {
        userService.initRoleAndUser();
    }

    @PostMapping("/user/register")
    public ResponseEntity<UserEntity> registerNewUser(@RequestBody UserRequestModel userRequestModel) {

        if (userRequestModel.getUser_first_name().isEmpty() || userRequestModel.getUser_first_name() == "") {
            throw new ApplicationException("User First Name Can't be Empty!");
        }

        if (userRequestModel.getUser_last_name().isEmpty() || userRequestModel.getUser_last_name() == "") {
            throw new ApplicationException("User Last Name Can't be Empty!");
        }

        if (userRequestModel.getUsername().isEmpty() || userRequestModel.getUsername() == "") {
            throw new ApplicationException("User username Can't be Empty!");
        }

        if (userRequestModel.getUser_email().isEmpty() || userRequestModel.getUser_email() == "") {
            throw new ApplicationException("User email Can't be Empty!");
        }

        if (userRequestModel.getUser_password().isEmpty() || userRequestModel.getUser_password() == "") {
            throw new ApplicationException("User Password Can't be Empty!");
        }

        return new ResponseEntity<>(userService.saveUser(userRequestModel), HttpStatus.CREATED);

    }

    @PostMapping("/user/login")
    public UserLoginResponseModel login(@RequestBody UserLoginRequestModel userLoginRequestModel) throws Exception {
        return jwtService.login(userLoginRequestModel);
    }

}
