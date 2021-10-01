package com.bloggingapp.servicesImpl;


import com.bloggingapp.entities.CommentEntity;
import com.bloggingapp.exceptions.CommentNotFoundException;
import com.bloggingapp.models.requestModels.CommentRequestModel;
import com.bloggingapp.repositories.CommentRepository;
import com.bloggingapp.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {


    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentEntity createComment(CommentRequestModel commentRequestModel) {
        CommentEntity newComment = new CommentEntity();
        newComment.setCommentBody(commentRequestModel.getCommentBody());
        newComment.setPostId(commentRequestModel.getPostId());
        newComment.setUserId(commentRequestModel.getUserId());
        return commentRepository.save(newComment);
    }

    @Override
    public Page<CommentEntity> findByPostId(Long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable);
    }

    @Override
    public void removePostCommentByPostIdAndCommentId(Long postId, Long commentId) {
        commentRepository.findByCommentIdAndPostId(commentId,postId).map(comment -> {
            commentRepository.delete(comment);
            return 1;
        });
    }

//    @Override
//    public void removeCommentById(Long commentId) {
//        Optional<CommentEntity> doesComment = commentRepository.findById(commentId);
//        if (doesComment.isEmpty()){
//            throw new CommentNotFoundException("Comment Not Found for Id: "+commentId);
//        }
//        commentRepository.delete(doesComment.get());
//    }
}
