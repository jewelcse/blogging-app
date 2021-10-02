package com.bloggingapp.servicesImpl;

import com.bloggingapp.entities.PostEntity;
import com.bloggingapp.entities.PostReactionEntity;
import com.bloggingapp.entities.UserEntity;
import com.bloggingapp.exceptions.ApplicationException;
import com.bloggingapp.exceptions.PostNotFoundException;
import com.bloggingapp.models.requestModels.PostReactionRequestModel;
import com.bloggingapp.models.responseModels.PostReactionCountResponseModel;
import com.bloggingapp.repositories.PostReactionRepository;
import com.bloggingapp.repositories.PostRepository;
import com.bloggingapp.repositories.UserRepository;
import com.bloggingapp.services.PostReactionService;
import com.bloggingapp.services.PostService;
import com.bloggingapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class PostReactionServiceImpl implements PostReactionService {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private PostReactionRepository postReactionRepository;

    @Autowired
    public PostReactionServiceImpl(UserRepository userRepository, PostRepository postRepository, PostReactionRepository postReactionRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postReactionRepository = postReactionRepository;
    }


    @Override
    public void makeReaction(PostReactionRequestModel postReactionRequestModel, Principal principal) {

        Optional<PostEntity> doesExitPost = postRepository.findById(postReactionRequestModel.getPostId());

        if (doesExitPost.isEmpty()){
            throw new PostNotFoundException("Post Not Found for Id: "+postReactionRequestModel.getPostId());
        }
        Optional<UserEntity> user = userRepository.findByUserName(principal.getName());
        if (!user.get().isApproved()){
            throw new ApplicationException("Your Account is Under approval, please try again later!");
        }
        Optional<PostReactionEntity> postReaction = postReactionRepository.findByUserIdAndPostId(user.get().getUserId(),postReactionRequestModel.getPostId());
        if (postReaction.isPresent()){
            if(postReactionRequestModel.isReaction()){
                postReaction.get().setLiked(true);
                postReaction.get().setDisliked(false);
            } else{
                postReaction.get().setLiked(false);
                postReaction.get().setDisliked(true);
            }
            postReactionRepository.save(postReaction.get());

        }else {
            PostReactionEntity newPostReaction = new PostReactionEntity();
            newPostReaction.setPostId(postReactionRequestModel.getPostId());
            newPostReaction.setUserId(user.get().getUserId());
            if(postReactionRequestModel.isReaction()){
                newPostReaction.setLiked(true);
                newPostReaction.setDisliked(false);
            } else{
                newPostReaction.setLiked(false);
                newPostReaction.setDisliked(true);
            }
            postReactionRepository.save(newPostReaction);
        }
    }

    @Override
    public PostReactionCountResponseModel getPostReactions(Long postId) {

        Optional<PostEntity> doesExitPost = postRepository.findById(postId);

        if (doesExitPost.isEmpty()){
            throw new PostNotFoundException("Post Not Found for Id: "+postId);
        }
        Long likes = postReactionRepository.findLikedByPostId(postId);
        Long disLikes = postReactionRepository.findDisLikedByPostId(postId);
        Long totalReactions = likes + disLikes;

        PostReactionCountResponseModel reactionCountResponseModel = new PostReactionCountResponseModel();
        reactionCountResponseModel.setPostId(postId);
        reactionCountResponseModel.setTotalLikesCount(likes);
        reactionCountResponseModel.setTotalDislikesCount(disLikes);
        reactionCountResponseModel.setTotalReactionCount(totalReactions);

        return reactionCountResponseModel;
    }
}
