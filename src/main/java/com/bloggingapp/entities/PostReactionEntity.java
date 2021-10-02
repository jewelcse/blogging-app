package com.bloggingapp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post_reactions")
public class PostReactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="post_reaction_id")
    private Long postReactionId;

    @Column(name ="post_id",nullable = false)
    private Long postId;

    @Column(name ="user_id",nullable = false)
    private Long userId;

    @Column(name ="is_liked",nullable = false)
    private boolean isLiked;

    @Column(name ="is_dis_liked",nullable = false)
    private boolean isDisliked;


}
