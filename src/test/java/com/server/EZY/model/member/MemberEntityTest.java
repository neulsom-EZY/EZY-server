package com.server.EZY.model.member;

import com.server.EZY.model.member.enum_type.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.testConfig.QueryDslTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest // DataJpa만 Test 할것이다.
@Import(QueryDslTestConfig.class)
class MemberEntityTest {

    @Autowired private MemberRepository memberRepo;

    @Test
    @DisplayName("UserEntity DB값 정상적으로 insert 하는지 검증")
    void userEntity_최대길이검증(){

        // Given
        MemberEntity memberEntity = MemberEntity.builder()
                .username("JsonWebTok")
                .password("JsonWebTok")
                .phoneNumber("01012345678")
                .roles(Collections.singletonList(Role.ROLE_ADMIN))
                .build();

        // When
        // UserEntity save 후 flush(DB에 쿼리를 날린다.)
        MemberEntity savedMemberEntity = memberRepo.saveAndFlush(memberEntity);

        // Then
        assertEquals(memberEntity, savedMemberEntity);
    }

    @Test
    @DisplayName("UserEntity UserDetails를 구현한 Method 값 검증")
    void userEntity_UserDetails_구현한_Method_값_검증() {

        // Given
        List<Role> userRole = new ArrayList<>();
        userRole.add(Role.ROLE_CLIENT);
        userRole.add(Role.ROLE_ADMIN);
        MemberEntity memberEntity = MemberEntity.builder()
                .username("siwony")
                .password("siwony1234")
                .phoneNumber("01037283839")
                .roles(userRole)
                .build();

        List<String> userRoles = memberEntity.getRoles().stream().map(Enum::name).collect(Collectors.toList());
        Collection<? extends GrantedAuthority> getRoleConvertSimpleGrantedAuthority = userRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        // When
        String getUsername = memberEntity.getUsername();
        Collection<? extends GrantedAuthority> getAuthorities = memberEntity.getAuthorities();
        boolean getAccountNonExpired = memberEntity.isAccountNonExpired();
        boolean getAccountNonLocked = memberEntity.isAccountNonLocked();
        boolean getCredentialsNonExpired = memberEntity.isCredentialsNonExpired();
        boolean getEnabled = memberEntity.isEnabled();

        // Then
        assertEquals(getUsername, memberEntity.getUsername());
        assertEquals(getAuthorities, getRoleConvertSimpleGrantedAuthority);
        assertEquals(getAccountNonExpired, true);
        assertEquals(getAccountNonLocked, true);
        assertEquals(getCredentialsNonExpired, true);
        assertEquals(getEnabled, true);
    }
}