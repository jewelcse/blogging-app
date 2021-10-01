package com.bloggingapp.services;

import com.bloggingapp.entities.PostEntity;
import com.bloggingapp.models.requestModels.PostRequestModel;

import java.util.List;
import java.util.Optional;

public interface PostService {


    PostEntity save(PostRequestModel postRequestModel);

    Optional<PostEntity> findPostById(Long postId);

    PostEntity update(PostEntity post);

    List<PostEntity> getAllPost();

    List<PostEntity> getAllPublishedPost();

    List<PostEntity> getAllUnPublishedPost();

    PostEntity getSinglePost(Long postId);

    List<PostEntity> getAllUserPost(Long userId);

    void approvePost(Long postId);

    void removePostById(Long postId);
}
