package com.bloggingapp.repositories;


import com.bloggingapp.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByUserName(String username);
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET is_approved = true  WHERE user_id =:userId",nativeQuery = true)
    void approveUserAccount(@RequestParam("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET is_approved = false  WHERE user_id =:userId",nativeQuery = true)
    void deactivateUserAccount(Long userId);
}
