package com.bloggingapp.repositories;


import com.bloggingapp.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity,Long> {

    List<CommentEntity> findByPostId(Long postId);
    Optional<CommentEntity> findByCommentIdAndPostId(Long id, Long postId);
}
