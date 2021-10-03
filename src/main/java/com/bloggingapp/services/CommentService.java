package com.bloggingapp.services;

import com.bloggingapp.entities.CommentEntity;
import com.bloggingapp.models.requestModels.CommentRequestModel;
import java.security.Principal;
import java.util.List;

public interface CommentService {
    CommentEntity createComment(CommentRequestModel commentRequestModel,Principal principal);
    List<CommentEntity> findCommentsByPostId(Long postId);
    void removePostCommentByPostIdAndCommentId(Long postId, Long commentId, Principal principal);
}
