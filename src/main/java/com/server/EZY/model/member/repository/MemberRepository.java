package com.server.EZY.model.member.repository;

import com.server.EZY.model.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByUsernameAndPhoneNumber(String username, String phoneNumber);
    Optional<MemberEntity> findByPhoneNumber(String phoneNumber);
}

