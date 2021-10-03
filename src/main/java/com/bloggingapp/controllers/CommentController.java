package com.bloggingapp.controllers;


import com.bloggingapp.entities.CommentEntity;
import com.bloggingapp.models.requestModels.CommentRequestModel;
import com.bloggingapp.services.CommentService;
import com.bloggingapp.services.PostService;
import com.bloggingapp.services.UserService;
import com.bloggingapp.utils.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;


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
    public ResponseEntity<CommentEntity> createComment(@RequestBody CommentRequestModel commentRequestModel,Principal principal) {
        return new ResponseEntity<>(commentService.createComment(commentRequestModel,principal), HttpStatus.CREATED);
    }


    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<List<CommentEntity>> getAllCommentsByPostId(@PathVariable Long postId) {
        return new ResponseEntity<>(commentService.findCommentsByPostId(postId),HttpStatus.OK);
    }

    @GetMapping("/post/{postId}/comment/remove/{commentId}")
    @PreAuthorize("hasAnyRole('BLOGGER','ADMIN')")
    public ResponseEntity<String> removeCommentById(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, Principal principal) {
        commentService.removePostCommentByPostIdAndCommentId(postId, commentId,principal);
        return new ResponseEntity<>(MethodUtils.prepareSuccessJSON(HttpStatus.OK, "Comment Removed Successful for comment id: "+commentId), HttpStatus.OK);
    }


}
