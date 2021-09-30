package com.bloggingapp.models.responseModels;


import com.bloggingapp.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseModel {

    private UserEntity userEntity;
    private String token;
}
