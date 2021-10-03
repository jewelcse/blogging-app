package com.bloggingapp.controllers;


import com.bloggingapp.entities.UserEntity;
import com.bloggingapp.exceptions.ApplicationException;
import com.bloggingapp.models.requestModels.UserLoginRequestModel;
import com.bloggingapp.models.requestModels.UserRegisterRequestModel;
import com.bloggingapp.models.responseModels.UserLoginResponseModel;
import com.bloggingapp.services.RoleService;
import com.bloggingapp.services.UserService;
import com.bloggingapp.servicesImpl.JwtService;
import com.bloggingapp.utils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import java.util.List;


@RestController
@RequestMapping("/api")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

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
    public ResponseEntity<UserEntity> registerNewUser(@RequestBody UserRegisterRequestModel userRegisterRequestModel) {

        if (userRegisterRequestModel.getUser_first_name().isEmpty() || userRegisterRequestModel.getUser_first_name() == "") {
            throw new ApplicationException("User First Name Can't be Empty!");
        }

        if (userRegisterRequestModel.getUser_last_name().isEmpty() || userRegisterRequestModel.getUser_last_name() == "") {
            throw new ApplicationException("User Last Name Can't be Empty!");
        }

        if (userRegisterRequestModel.getUsername().isEmpty() || userRegisterRequestModel.getUsername() == "") {
            throw new ApplicationException("User username Can't be Empty!");
        }

        if (userRegisterRequestModel.getUser_email().isEmpty() || userRegisterRequestModel.getUser_email() == "") {
            throw new ApplicationException("User email Can't be Empty!");
        }

        if (userRegisterRequestModel.getUser_password().isEmpty() || userRegisterRequestModel.getUser_password() == "") {
            throw new ApplicationException("User Password Can't be Empty!");
        }
        logger.info("Registering a new User!");
        return new ResponseEntity<>(userService.saveUser(userRegisterRequestModel), HttpStatus.CREATED);

    }

    @PostMapping("/user/login")
    public UserLoginResponseModel userLogin(@RequestBody UserLoginRequestModel userLoginRequestModel) throws Exception {
        logger.info("Logging User for username: "+userLoginRequestModel.getUsername());
        return jwtService.login(userLoginRequestModel);
    }

    @GetMapping("/user/account/approve/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> approveUserAccountByAdmin(@PathVariable Long userId) {
        if (userId <= 0) {
            throw new ApplicationException("Invalid User Id for Id: " + userId);
        }
        logger.info("Approving User Account for Id: "+userId);
        userService.approveUserAccountByAdmin(userId);
        return new ResponseEntity<>(MethodUtils.prepareSuccessJSON(HttpStatus.OK,"User Account Approved By Admin! for user id: "+userId), HttpStatus.OK);
    }

    @GetMapping("/user/account/deactivate/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deActivateUserAccountByAdmin(@PathVariable Long userId) {
        if (userId <= 0) {
            throw new ApplicationException("Invalid User Id for Id: " + userId);
        }
        userService.deActivateUserAccountByAdmin(userId);
        return new ResponseEntity<>(MethodUtils.prepareSuccessJSON(HttpStatus.OK,"User Account Deactivated By Admin! for user id: "+userId), HttpStatus.OK);
    }


    @PostMapping("/create/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserEntity> createNewAdmin(@RequestBody UserRegisterRequestModel userRegisterRequestModel) {

        if (userRegisterRequestModel.getUser_first_name().isEmpty() || userRegisterRequestModel.getUser_first_name() == "") {
            throw new ApplicationException("User First Name Can't be Empty!");
        }

        if (userRegisterRequestModel.getUser_last_name().isEmpty() || userRegisterRequestModel.getUser_last_name() == "") {
            throw new ApplicationException("User Last Name Can't be Empty!");
        }

        if (userRegisterRequestModel.getUsername().isEmpty() || userRegisterRequestModel.getUsername() == "") {
            throw new ApplicationException("User username Can't be Empty!");
        }

        if (userRegisterRequestModel.getUser_email().isEmpty() || userRegisterRequestModel.getUser_email() == "") {
            throw new ApplicationException("User email Can't be Empty!");
        }

        if (userRegisterRequestModel.getUser_password().isEmpty() || userRegisterRequestModel.getUser_password() == "") {
            throw new ApplicationException("User Password Can't be Empty!");
        }

        logger.info("Creating a new Admin");
        return new ResponseEntity<>(userService.createNewAdmin(userRegisterRequestModel), HttpStatus.OK);

    }

    @GetMapping("/get/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserEntity>> getUsers(){
        return new ResponseEntity<>(userService.getAllUser(),HttpStatus.OK);
    }


}
