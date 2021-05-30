package com.server.EZY.repository;

import com.server.EZY.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // MyUserDetails 에서 사용.
    UserEntity findByNickname(String nickName);
    // UserService 에서 사용.
    boolean existsByNickname(String nickname);
}
