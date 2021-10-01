package com.bloggingapp.controllers;


import com.bloggingapp.entities.CommentEntity;
import com.bloggingapp.entities.PostEntity;
import com.bloggingapp.entities.UserEntity;
import com.bloggingapp.exceptions.ApplicationException;
import com.bloggingapp.exceptions.CommentNotFoundException;
import com.bloggingapp.exceptions.PostNotFoundException;
import com.bloggingapp.exceptions.UserNotFoundException;
import com.bloggingapp.models.requestModels.CommentRequestModel;
import com.bloggingapp.services.CommentService;
import com.bloggingapp.services.PostService;
import com.bloggingapp.services.UserService;
import com.bloggingapp.utils.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CommentController {

    private PostService postService;
    private UserService userService;
    private CommentService commentService;


    @Autowired
    public CommentController(PostService postService, UserService userService, CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @PostMapping("/post/create/comment")
    @PreAuthorize("hasAnyRole('BLOGGER','ADMIN')")
    public ResponseEntity<CommentEntity> createComment(@RequestBody CommentRequestModel commentRequestModel) {
        if (commentRequestModel.getPostId() <= 0) {
            throw new ApplicationException("Invalid Post Id :" + commentRequestModel.getPostId());
        }
        if (commentRequestModel.getUserId() <= 0) {
            throw new ApplicationException("Invalid User Id :" + commentRequestModel.getUserId());
        }
        Optional<PostEntity> doesExiPostEntity = postService.findPostById(commentRequestModel.getPostId());
        if (doesExiPostEntity.isEmpty()) {
            throw new PostNotFoundException("Post Not Found For Id :" + commentRequestModel.getPostId());
        }
        Optional<UserEntity> doesExiUserEntity = userService.findUserById(commentRequestModel.getUserId());
        if (doesExiUserEntity.isEmpty()) {
            throw new UserNotFoundException("User Not Found For Id :" + commentRequestModel.getUserId());
        }
        if (commentRequestModel.getCommentBody().isEmpty() || commentRequestModel.getCommentBody() == "") {
            throw new ApplicationException("Comment Body Can't be empty!");
        }
        return new ResponseEntity<>(commentService.createComment(commentRequestModel), HttpStatus.CREATED);
    }


    @GetMapping("/post/{postId}/comments")
    public Page<CommentEntity> getAllCommentsByPostId(@PathVariable Long postId, Pageable pageable) {
        return commentService.findByPostId(postId, pageable);
    }

    @GetMapping("/post/{postId}/comment/remove/{commentId}")
    @PreAuthorize("hasAnyRole('BLOGGER','ADMIN')")
    public ResponseEntity<String> removeCommentById(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, Principal principal) {
        if (commentId <= 0) {
            throw new ApplicationException("Invalid Comment Id " + commentId);
        }
        Optional<UserEntity> user = userService.findUserByUsername(principal.getName());
        // post owner can and admin only remove comments
        // will do it tomorrow morning
        commentService.removePostCommentByPostIdAndCommentId(postId, commentId);
        return new ResponseEntity<>(MethodUtils.prepareSuccessJSON(HttpStatus.OK, "Comment Removed Successful"), HttpStatus.OK);
    }


}
