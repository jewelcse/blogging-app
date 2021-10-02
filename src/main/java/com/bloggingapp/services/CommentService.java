package com.bloggingapp.services;

import com.bloggingapp.entities.CommentEntity;
import com.bloggingapp.models.requestModels.CommentRequestModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface CommentService {
    CommentEntity createComment(CommentRequestModel commentRequestModel,Principal principal);
    Page<CommentEntity> findCommentsByPostId(Long postId, Pageable pageable);
    void removePostCommentByPostIdAndCommentId(Long postId, Long commentId, Principal principal);
}
