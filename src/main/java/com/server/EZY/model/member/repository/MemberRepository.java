package com.server.EZY.model.member.repository;

import com.server.EZY.model.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // MyUserDetails 에서 사용.
    MemberEntity findByUsername(String username);
    // UserService 에서 사용.
    boolean existsByUsername(String username);
    MemberEntity findByPhoneNumber(String phoneNumber);
}

