package com.bloggingapp.servicesImpl;


import com.bloggingapp.entities.RoleEntity;
import com.bloggingapp.entities.UserEntity;
import com.bloggingapp.entities.UserRoleEntity;
import com.bloggingapp.exceptions.ApplicationException;
import com.bloggingapp.models.requestModels.UserLoginRequestModel;
import com.bloggingapp.models.requestModels.UserRequestModel;
import com.bloggingapp.models.responseModels.UserLoginResponseModel;
import com.bloggingapp.models.responseModels.UserResponseModel;
import com.bloggingapp.repositories.RoleRepository;
import com.bloggingapp.repositories.UserRepository;
import com.bloggingapp.services.UserService;
import com.bloggingapp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.HashSet;
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
    public UserServiceImpl(UserRepository repository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil,
                           AuthenticationManager authenticationManager){
        this.userRepository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }
    @Override
    public UserEntity saveUser(UserRequestModel userRequestModel) {

        Optional<UserEntity> doesExitUser = userRepository.findByUserName(userRequestModel.getUsername());

        if (!doesExitUser.isEmpty()){
            throw new ApplicationException("User Already Exit!");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUserFirstName(userRequestModel.getUser_first_name());
        newUser.setUserLastName(userRequestModel.getUser_last_name());
        newUser.setUserName(userRequestModel.getUsername());
        newUser.setUserEmail(userRequestModel.getUser_email());
        newUser.setUserPassword(getEncodedPassword(userRequestModel.getUser_password()));


        RoleEntity defaultRole = roleRepository.findByRoleName("USER").get();

        Set<RoleEntity> usersRole = new HashSet<>();
        usersRole.add(defaultRole);

        newUser.setRole(usersRole);
        return userRepository.save(newUser);
    }


    @Override
    public void initRoleAndUser() {

        RoleEntity adminRole = new RoleEntity();
        adminRole.setRoleName("ADMIN");
        roleRepository.save(adminRole);

        RoleEntity userRole = new RoleEntity();
        userRole.setRoleName("USER");
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

        userRepository.save(adminUser);

    }


    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }



}
