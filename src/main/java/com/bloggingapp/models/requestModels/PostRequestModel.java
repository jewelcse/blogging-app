package com.bloggingapp.models.requestModels;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestModel {

    private String title;
    private String body;
    private Long userId;

}
