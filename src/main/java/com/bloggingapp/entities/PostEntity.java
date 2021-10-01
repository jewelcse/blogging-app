package com.bloggingapp.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="post_id")
    private Long postId;

    @Column(name ="post_title",nullable = false)
    private String postTitle;

    @Column(name ="post_body",columnDefinition = "TEXT", nullable = false)
    private String postBody;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "is_published")
    private boolean isPublished;





}
