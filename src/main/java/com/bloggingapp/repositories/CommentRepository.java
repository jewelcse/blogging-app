package com.bloggingapp.repositories;


import com.bloggingapp.entities.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity,Long> {

    Page<CommentEntity> findByPostId(Long postId, Pageable pageable);
    Optional<CommentEntity> findByCommentIdAndPostId(Long id, Long postId);
}
