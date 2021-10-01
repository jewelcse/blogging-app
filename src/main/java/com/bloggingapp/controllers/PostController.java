package com.bloggingapp.controllers;

import com.bloggingapp.entities.PostEntity;
import com.bloggingapp.exceptions.ApplicationException;
import com.bloggingapp.exceptions.PostNotFoundException;
import com.bloggingapp.models.requestModels.PostRequestModel;
import com.bloggingapp.services.PostService;
import com.bloggingapp.utils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostController {

    Logger logger = LoggerFactory.getLogger(PostController.class);

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }


    @PostMapping("/create/post")
    @PreAuthorize("hasAnyRole('ADMIN','BLOGGER')")
    public ResponseEntity<PostEntity> createPost(@RequestBody PostRequestModel postRequestModel) {
        logger.info("Creating a new post");
        return new ResponseEntity<>(postService.save(postRequestModel), HttpStatus.CREATED);
    }

    @PostMapping("/edit/post/{postId}")
    @PreAuthorize("hasAnyRole('ADMIN','BLOGGER')")
    public ResponseEntity<PostEntity> editPost(@RequestBody PostRequestModel postRequestModel, @PathVariable Long postId) {
        if (postId <= 0) {
            throw new ApplicationException("Invalid Post Id :" + postId);
        }

        if (postRequestModel.getTitle().isEmpty() || postRequestModel.getTitle() == "") {
            throw new ApplicationException("Post Title Can't Be Empty!");
        }

        if (postRequestModel.getBody().isEmpty() || postRequestModel.getBody() == "") {
            throw new ApplicationException("Post Body Can't Be Empty!");
        }

        Optional<PostEntity> doesExitPost = postService.findPostById(postId);

        if (doesExitPost.isEmpty()) {
            throw new PostNotFoundException("Post Not Found for Id: " + postId);
        }

        doesExitPost.get().setPostTitle(postRequestModel.getTitle());
        doesExitPost.get().setPostBody(postRequestModel.getBody());
        logger.info("Updating post for id: " + postId);
        return new ResponseEntity<>(postService.update(doesExitPost.get()), HttpStatus.OK);

    }

    @GetMapping("/get/posts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PostEntity>> getPosts() {
        logger.info("Getting all posts");
        return new ResponseEntity<>(postService.getAllPost(), HttpStatus.OK);
    }


    @GetMapping("/get/published/posts")
    public ResponseEntity<List<PostEntity>> getPublishedPosts() {
        logger.info("Getting all Published post");
        return new ResponseEntity<>(postService.getAllPublishedPost(), HttpStatus.OK);
    }

    @GetMapping("/get/published/post/{postId}")
    public ResponseEntity<PostEntity> getSinglePublishedPostById(@PathVariable Long postId) {
        if (postId <= 0) {
            throw new ApplicationException("Invalid Post Id :" + postId);
        }
        logger.info("Getting a single published post");
        return new ResponseEntity<>(postService.getSinglePost(postId), HttpStatus.OK);
    }

    @GetMapping("/get/unpublished/posts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PostEntity>> getUnpublishedPosts() {
        logger.info("Getting all unpublished post");
        return new ResponseEntity<>(postService.getAllUnPublishedPost(), HttpStatus.OK);
    }

    @GetMapping("/get/user/posts/{userId}")
    @PreAuthorize("hasAnyRole('BLOGGER','ADMIN')")
    public ResponseEntity<List<PostEntity>> getUserPosts(@PathVariable Long userId) {
        if (userId <= 0) {
            throw new ApplicationException("Invalid User Id :" + userId);
        }
        logger.info("Getting User Posts by User Id");
        return new ResponseEntity<>(postService.getAllUserPost(userId), HttpStatus.OK);
    }

    @GetMapping("/approve/post/{postId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> approveUserPost(@PathVariable Long postId) {
        if (postId <= 0) {
            throw new ApplicationException("Invalid Post Id :" + postId);
        }
        logger.info("User Post approving by Admin");
        postService.approvePost(postId);
        logger.info("User Post approved by Admin successful");
        return new ResponseEntity<>(MethodUtils.prepareSuccessJSON(HttpStatus.OK, "Approve Post Success"), HttpStatus.OK);
    }

    @GetMapping("/remove/post/{postId}")
    @PreAuthorize("hasAnyRole('BLOGGER','ADMIN')")
    public ResponseEntity<String> removePostById(@PathVariable Long postId) {
        if (postId <= 0) {
            throw new ApplicationException("Invalid PostId for Id: " + postId);
        }
        logger.info("Removing post for id: " + postId);
        postService.removePostById(postId);
        logger.info("Removed post successful for id: " + postId);
        return new ResponseEntity<>(MethodUtils.prepareSuccessJSON(HttpStatus.OK, "Removed Success for Id: " + postId), HttpStatus.OK);
    }


}
