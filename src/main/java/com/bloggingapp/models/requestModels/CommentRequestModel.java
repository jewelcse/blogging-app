package com.bloggingapp.models.requestModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestModel {

    private String commentBody;
    private Long userId;
    private Long postId;
}
