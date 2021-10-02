package com.bloggingapp.models.responseModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReactionCountResponseModel {
    private Long postId;
    private Long totalReactionCount;
    private Long totalLikesCount;
    private Long totalDislikesCount;

}
