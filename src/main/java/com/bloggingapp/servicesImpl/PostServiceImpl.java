package com.bloggingapp.servicesImpl;


import com.bloggingapp.entities.PostEntity;
import com.bloggingapp.entities.RoleEntity;
import com.bloggingapp.entities.UserEntity;
import com.bloggingapp.exceptions.ApplicationException;
import com.bloggingapp.exceptions.PostNotFoundException;
import com.bloggingapp.exceptions.UserNotFoundException;
import com.bloggingapp.models.requestModels.PostRequestModel;
import com.bloggingapp.repositories.PostRepository;
import com.bloggingapp.repositories.UserRepository;
import com.bloggingapp.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository repository, UserRepository userRepository) {
        this.postRepository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public PostEntity save(PostRequestModel postRequestModel, Principal principal) {

        if (postRequestModel.getTitle().isEmpty() || postRequestModel.getTitle() == "") {
            throw new ApplicationException("Post Title Can't Be Empty!");
        }

        if (postRequestModel.getBody().isEmpty() || postRequestModel.getBody() == "") {
            throw new ApplicationException("Post Body Can't Be Empty!");
        }

        PostEntity newPost = new PostEntity();

        Optional<UserEntity> user = userRepository.findByUserName(principal.getName());
        if (user.isEmpty()) {
            throw new UserNotFoundException("User Not Found for Id: " + user.get().getUserId());
        }

        if (!user.get().isApproved()){
            throw new ApplicationException("Your Account is Under Approval, Please Wait!");
        }

        for (RoleEntity role: user.get().getRole()) {
            if (role.getRoleName().equals("ADMIN")){
                newPost.setPublished(true);
            }else {
                newPost.setPublished(false);
            }
        }
        newPost.setPostTitle(postRequestModel.getTitle());
        newPost.setPostBody(postRequestModel.getBody());
        newPost.setUserId(user.get().getUserId());

        return postRepository.save(newPost);
    }

    @Override
    public Optional<PostEntity> findPostById(Long postId) {
        return postRepository.findById(postId);
    }

    @Override
    public PostEntity update(PostEntity post) {
        return postRepository.save(post);
    }

    @Override
    public List<PostEntity> getAllPost() {
        return postRepository.findAll();
    }

    @Override
    public List<PostEntity> getAllPublishedPost() {
        return postRepository.findPublishedPosts();
    }

    @Override
    public List<PostEntity> getAllUnPublishedPost() {
        return postRepository.findUnPublishedPosts();
    }

    @Override
    public PostEntity getSinglePost(Long postId) {

        Optional<PostEntity> postEntity = postRepository.findById(postId);
        if (postEntity.isEmpty()) {
            throw new PostNotFoundException("Post Doesn't exit for postId :" + postId);
        }
        return postEntity.get();
    }

    @Override
    public List<PostEntity> getAllUserPost(Long userId) {

        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User Not Found for Id: " + userId);
        }

        return postRepository.findUserPostsByUserId(userId);
    }

    @Override
    public void approvePost(Long postId) {
        Optional<PostEntity> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new PostNotFoundException("Post Not Found For Id: " + postId);
        }
        if (post.get().isPublished()){
            throw new PostNotFoundException("Post Already Approved by Admin");
        }
        postRepository.userPostApprove(postId);
    }

    @Override
    public void removePostById(Long postId) {
        Optional<PostEntity> doesExitPost = postRepository.findById(postId);
        if (doesExitPost.isEmpty()){
            throw new PostNotFoundException("Post Not Found for Id: "+postId);
        }
        postRepository.delete(doesExitPost.get());
    }
}
