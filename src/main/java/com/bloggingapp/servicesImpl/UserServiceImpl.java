package com.bloggingapp.servicesImpl;


import com.bloggingapp.entities.RoleEntity;
import com.bloggingapp.entities.UserEntity;
import com.bloggingapp.exceptions.ApplicationException;
import com.bloggingapp.exceptions.UserNotFoundException;
import com.bloggingapp.models.requestModels.UserRegisterRequestModel;
import com.bloggingapp.repositories.RoleRepository;
import com.bloggingapp.repositories.UserRepository;
import com.bloggingapp.services.UserService;
import com.bloggingapp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository repository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager){
        this.userRepository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }
    @Override
    public UserEntity saveUser(UserRegisterRequestModel userRegisterRequestModel) {

        Optional<UserEntity> doesExitUser = userRepository.findByUserName(userRegisterRequestModel.getUsername());

        if (!doesExitUser.isEmpty()){
            throw new ApplicationException("User Already Exit!");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUserFirstName(userRegisterRequestModel.getUser_first_name());
        newUser.setUserLastName(userRegisterRequestModel.getUser_last_name());
        newUser.setUserName(userRegisterRequestModel.getUsername());
        newUser.setUserEmail(userRegisterRequestModel.getUser_email());
        newUser.setUserPassword(getEncodedPassword(userRegisterRequestModel.getUser_password()));


        RoleEntity defaultRole = roleRepository.findByRoleName("BLOGGER").get();

        Set<RoleEntity> usersRole = new HashSet<>();
        usersRole.add(defaultRole);

        newUser.setRole(usersRole);
        newUser.setApproved(false);
        return userRepository.save(newUser);
    }

    @Override
    public UserEntity createNewAdmin(UserRegisterRequestModel userRegisterRequestModel) {

        Optional<UserEntity> doesExitAdmin = userRepository.findByUserName(userRegisterRequestModel.getUsername());

        if (!doesExitAdmin.isEmpty()){
            throw new ApplicationException("Admin Already Exit!");
        }

        UserEntity newAdmin = new UserEntity();
        newAdmin.setUserFirstName(userRegisterRequestModel.getUser_first_name());
        newAdmin.setUserLastName(userRegisterRequestModel.getUser_last_name());
        newAdmin.setUserName(userRegisterRequestModel.getUsername());
        newAdmin.setUserEmail(userRegisterRequestModel.getUser_email());
        newAdmin.setUserPassword(getEncodedPassword(userRegisterRequestModel.getUser_password()));


        RoleEntity defaultAdminRole = roleRepository.findByRoleName("ADMIN").get();

        Set<RoleEntity> adminRoles = new HashSet<>();
        adminRoles.add(defaultAdminRole);
        newAdmin.setRole(adminRoles);
        newAdmin.setApproved(true);
        return userRepository.save(newAdmin);
    }

    @Override
    public Optional<UserEntity> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void approveUserAccountByAdmin(Long userId) {

        Optional<UserEntity> doesUserExist = userRepository.findById(userId);
        if (doesUserExist.isEmpty()){
            throw new UserNotFoundException("User Doesn't Exit For Id: " + userId);
        }
        if (doesUserExist.get().isApproved()){
            throw new ApplicationException("User Already Approved By Admin!");
        }
        userRepository.approveUserAccount(userId);
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public List<UserEntity> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void initRoleAndUser() {

        RoleEntity adminRole = new RoleEntity();
        adminRole.setRoleName("ADMIN");
        roleRepository.save(adminRole);

        RoleEntity userRole = new RoleEntity();
        userRole.setRoleName("BLOGGER");
        roleRepository.save(userRole);

        UserEntity adminUser = new UserEntity();
        adminUser.setUserFirstName("Jewel");
        adminUser.setUserLastName("chowdhury");
        adminUser.setUserName("admin");
        adminUser.setUserEmail("admin@gmail.com");
        adminUser.setUserPassword(getEncodedPassword("admin123"));

        Set<RoleEntity> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        adminUser.setApproved(true);
        userRepository.save(adminUser);

    }


    private String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }



}
