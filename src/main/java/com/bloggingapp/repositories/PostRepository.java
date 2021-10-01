package com.bloggingapp.repositories;


import com.bloggingapp.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity,Long> {

    @Query(value = "SELECT *  FROM posts WHERE is_published = true",nativeQuery = true)
    List<PostEntity> findPublishedPosts();

    @Query(value = "SELECT *  FROM posts WHERE is_published = false",nativeQuery = true)
    List<PostEntity> findUnPublishedPosts();

    @Query(value = "SELECT *  FROM posts WHERE user_id =:userId",nativeQuery = true)
    List<PostEntity> findUserPostsByUserId(@RequestParam("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE posts SET is_published = true  WHERE post_id =:postId",nativeQuery = true)
    void userPostApprove(@RequestParam("postId") Long postId);
}
