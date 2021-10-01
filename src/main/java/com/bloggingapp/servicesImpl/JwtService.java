package com.bloggingapp.servicesImpl;

import com.bloggingapp.entities.UserEntity;
import com.bloggingapp.exceptions.ApplicationException;
import com.bloggingapp.exceptions.UserNotFoundException;
import com.bloggingapp.models.requestModels.UserLoginRequestModel;
import com.bloggingapp.models.responseModels.UserLoginResponseModel;
import com.bloggingapp.repositories.UserRepository;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;


    public UserLoginResponseModel login(UserLoginRequestModel jwtRequest) throws Exception {
        String userName = jwtRequest.getUsername();
        String userPassword = jwtRequest.getPassword();
        authenticate(userName, userPassword);

        UserDetails userDetails = loadUserByUsername(userName);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        UserEntity user = userRepository.findByUserName(userName).get();
        return new UserLoginResponseModel(user, newGeneratedToken);
    }



    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {

        Optional<UserEntity> user = userRepository.findByUserName(username);

        if (!user.isEmpty()) {
            return new org.springframework.security.core.userdetails.User(
                    user.get().getUserName(),
                    user.get().getUserPassword(),
                    getAuthority(user.get())
            );
        } else {
            throw new UserNotFoundException("User not found for username: " + username);
        }
    }

    private Set getAuthority(UserEntity user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

    private void authenticate(String userName, String userPassword) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new ApplicationException("User Disabled!");
        } catch (BadCredentialsException e) {
            throw new ApplicationException("Incorrect Password!");
        }
    }
}
