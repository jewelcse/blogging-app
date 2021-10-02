package com.bloggingapp.services;

import com.bloggingapp.entities.PostReactionEntity;
import com.bloggingapp.models.requestModels.PostReactionRequestModel;
import com.bloggingapp.models.responseModels.PostReactionCountResponseModel;

import java.security.Principal;

public interface PostReactionService {
    void makeReaction(PostReactionRequestModel reactionRequestModel, Principal principal);
    PostReactionCountResponseModel getPostReactions(Long postId);
}
