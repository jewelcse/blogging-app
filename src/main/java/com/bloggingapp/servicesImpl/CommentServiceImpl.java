package com.bloggingapp.servicesImpl;


import com.bloggingapp.entities.CommentEntity;
import com.bloggingapp.entities.PostEntity;
import com.bloggingapp.entities.UserEntity;
import com.bloggingapp.exceptions.ApplicationException;
import com.bloggingapp.exceptions.PostNotFoundException;
import com.bloggingapp.models.requestModels.CommentRequestModel;
import com.bloggingapp.repositories.CommentRepository;
import com.bloggingapp.repositories.PostRepository;
import com.bloggingapp.repositories.UserRepository;
import com.bloggingapp.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {


    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentEntity createComment(CommentRequestModel commentRequestModel,Principal principal) {
        if (commentRequestModel.getPostId() <= 0) {
            throw new ApplicationException("Invalid Post Id :" + commentRequestModel.getPostId());
        }
        Optional<PostEntity> doesPostExist = postRepository.findById(commentRequestModel.getPostId());
        if (doesPostExist.isEmpty()) {
            throw new PostNotFoundException("Post Not Found For Id :" + commentRequestModel.getPostId());
        }
        if (!doesPostExist.get().isPublished()){
            throw new ApplicationException("Post is under approval, try later!");
        }
        Optional<UserEntity> user = userRepository.findByUserName(principal.getName());

        if (!user.get().isApproved()){
            throw new ApplicationException("Your Account is under approval, please wait to make comments!");
        }
        if (commentRequestModel.getCommentBody().isEmpty() || commentRequestModel.getCommentBody() == "") {
            throw new ApplicationException("Comment Body Can't be empty!");
        }
        CommentEntity newComment = new CommentEntity();
        newComment.setCommentBody(commentRequestModel.getCommentBody());
        newComment.setPostId(commentRequestModel.getPostId());
        newComment.setUserId(user.get().getUserId());
        return commentRepository.save(newComment);
    }

    @Override
    public List<CommentEntity> findCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public void removePostCommentByPostIdAndCommentId(Long postId, Long commentId, Principal principal) {
        if (postId <= 0) {
            throw new ApplicationException("Invalid Post Id " + postId);
        }
        if (commentId <= 0) {
            throw new ApplicationException("Invalid Comment Id " + commentId);
        }
        Optional<UserEntity> user = userRepository.findByUserName(principal.getName());
        commentRepository.findByCommentIdAndPostId(commentId,postId).map(comment -> {
            if (comment.getUserId().equals(user.get().getUserId()))
            {
                commentRepository.delete(comment);
            }else {
                throw new ApplicationException("You have Don't permission to remove the comment for Id: " + commentId);
            }
            return 1;
        }).orElseThrow(()->new ApplicationException("Post have no comments yet!"));
    }

}
