package com.bloggingapp.controllers;

import com.bloggingapp.entities.PostReactionEntity;
import com.bloggingapp.models.requestModels.PostReactionRequestModel;
import com.bloggingapp.models.responseModels.PostReactionCountResponseModel;
import com.bloggingapp.services.PostReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class PostReactionController {

    private PostReactionService postReactionService;

    @Autowired
    public PostReactionController(PostReactionService postReactionService){
        this.postReactionService = postReactionService;
    }

    @PostMapping("/post/make/reaction")
    @PreAuthorize("hasAnyRole('ADMIN','BLOGGER')")
    public void makeReaction(@RequestBody PostReactionRequestModel postReactionRequestModel, Principal principal){
        postReactionService.makeReaction(postReactionRequestModel,principal);
    }

    @GetMapping("/post/{postId}/reactions")
    public ResponseEntity<PostReactionCountResponseModel> countPostReactions(@PathVariable Long postId){
        return new ResponseEntity<>(postReactionService.getPostReactions(postId),HttpStatus.OK);
    }

}
