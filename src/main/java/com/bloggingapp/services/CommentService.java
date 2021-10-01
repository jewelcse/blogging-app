package com.bloggingapp.services;

import com.bloggingapp.entities.CommentEntity;
import com.bloggingapp.models.requestModels.CommentRequestModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentEntity createComment(CommentRequestModel commentRequestModel);
    Page<CommentEntity> findByPostId(Long postId, Pageable pageable);
    void removePostCommentByPostIdAndCommentId(Long postId, Long commentId);
}
