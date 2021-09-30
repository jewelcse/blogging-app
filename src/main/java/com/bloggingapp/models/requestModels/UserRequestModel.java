package com.bloggingapp.models.requestModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestModel {

    private String user_first_name;
    private String user_last_name;
    private String username;
    private String user_email;
    private String user_password;

}
