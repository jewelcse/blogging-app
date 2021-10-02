package com.bloggingapp.models.requestModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReactionRequestModel {

    private Long postId;
    private boolean reaction;

}
