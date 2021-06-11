package com.server.EZY.model.user;

import com.server.EZY.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.validation.ConstraintViolationException;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest // DataJpa만 Test 할것이다.
class UserEntityTest {

    @Autowired private UserRepository userRepo;

    @Test
    @DisplayName("UserEntity DB값 정상적으로 insert 하는지 검증")
    void userEntity_최대길이검증(){

        // Given
        UserEntity userEntity = UserEntity.builder()
                .nickname("JsonWebTok")
                .password("JsonWebTok")
                .phoneNumber("01012345678")
                .permission(Permission.PERMISSION)
                .roles(Collections.singletonList(Role.ROLE_ADMIN))
                .build();

        // When
        // UserEntity save 후 flush(DB에 쿼리를 날린다.)
        UserEntity savedUserEntity = userRepo.saveAndFlush(userEntity);

        // Then
        assertEquals(userEntity, savedUserEntity);
    }

    @Test
    @DisplayName("UserEntity 최대길이_초과시_Exception 검증 (ConstraintViolationException 발생시 Test 성공)")
    void userEntity_최대길이_초과_Exception_검증() throws Exception {
        UserEntity user = UserEntity.builder()
                .nickname("JsonWebTok1")
                .password("JsonWebTok2")
                .phoneNumber("010123456783")
                .permission(Permission.PERMISSION)
                .build();

        assertThrows(ConstraintViolationException.class,  () ->{
            userRepo.save(user);
        });
    }

    @Test
    @DisplayName("UserEntity 최대길이_미만시_Exception 검증 (ConstraintViolationException 발생시 Test 성공)")
    void userEntity_최소길이_미만_Exception_검증() throws Exception {
        UserEntity user = UserEntity.builder()
                .nickname("")
                .password("123")
                .phoneNumber("0103629383")
                .permission(Permission.PERMISSION)
                .build();

        assertThrows(ConstraintViolationException.class, () ->{
            userRepo.save(user);
        });

    }

    @Test
    @DisplayName("UserEntity UserDetails를 구현한 Method 값 검증")
    void userEntity_UserDetails_구현한_Method_값_검증(){

        // Given
        List<Role> userRole = new ArrayList<>();
        userRole.add(Role.ROLE_CLIENT);
        userRole.add(Role.ROLE_ADMIN);
        UserEntity userEntity = UserEntity.builder()
                .nickname("siwony")
                .permission(Permission.PERMISSION)
                .password("siwony1234")
                .phoneNumber("01037283839")
                .roles(userRole)
                .build();

        List<String> userRoles = userEntity.getRoles().stream().map(Enum::name).collect(Collectors.toList());
        Collection<? extends GrantedAuthority> getRoleConvertSimpleGrantedAuthority = userRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        // When
        String getUsername = userEntity.getUsername();
        Collection<? extends GrantedAuthority> getAuthorities = userEntity.getAuthorities();
        boolean getAccountNonExpired = userEntity.isAccountNonExpired();
        boolean getAccountNonLocked = userEntity.isAccountNonLocked();
        boolean getCredentialsNonExpired = userEntity.isCredentialsNonExpired();
        boolean getEnabled = userEntity.isEnabled();

        // Then
        assertEquals(getUsername, userEntity.getNickname());
        assertEquals(getAuthorities, getRoleConvertSimpleGrantedAuthority);
        assertEquals(getAccountNonExpired, true);
        assertEquals(getAccountNonLocked, true);
        assertEquals(getCredentialsNonExpired, true);
        assertEquals(getEnabled, true);
    }

}