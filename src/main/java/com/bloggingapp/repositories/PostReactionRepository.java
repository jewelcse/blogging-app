package com.bloggingapp.repositories;


import com.bloggingapp.entities.PostReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.DoubleStream;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReactionEntity,Long> {

    @Query(value = "SELECT count(is_liked)  FROM post_reactions WHERE is_liked = true AND post_id =:postId",nativeQuery = true)
    Long findLikedByPostId(Long postId);

    @Query(value = "SELECT count(is_dis_liked)  FROM post_reactions WHERE is_dis_liked = true AND post_id =:postId",nativeQuery = true)
    Long findDisLikedByPostId(Long postId);

    @Query(value = "SELECT *  FROM post_reactions WHERE user_id =:userId AND post_id=:postId",nativeQuery = true)
    Optional<PostReactionEntity> findByUserIdAndPostId(Long userId, Long postId);
}
