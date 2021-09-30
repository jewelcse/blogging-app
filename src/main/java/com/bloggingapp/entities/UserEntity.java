package com.bloggingapp.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_id")
    private Long userId;

    @Column(name ="user_first_name",nullable = false)
    private String userFirstName;

    @Column(name ="user_last_name",nullable = false)
    private String userLastName;

    @Column(name ="user_name",nullable = false)
    private String userName;

    @Column(name ="user_email",nullable = false)
    private String userEmail;

    @Column(name ="user_password",nullable = false)
    private String userPassword;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE",
        joinColumns = {
            @JoinColumn(name = "user_id")
        },
            inverseJoinColumns = {
            @JoinColumn(name = "role_id")
            }
    )
    private Set<RoleEntity> role;


}
