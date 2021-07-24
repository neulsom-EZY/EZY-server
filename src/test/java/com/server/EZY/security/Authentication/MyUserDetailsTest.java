package com.server.EZY.security.Authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyUserDetailsTest {

    @Autowired MyUserDetails myUserDetails;

    @Test
    @DisplayName("loadUserByUsername 메서드에 등록되지않은 유저의 username 또는 null을 인자로 넘겨준다면?")
    public void notFoundUsername() {

        assertThrows(
                UsernameNotFoundException.class,
                () -> myUserDetails.loadUserByUsername("taehyeon")
        );

        assertThrows(
                UsernameNotFoundException.class,
                () -> myUserDetails.loadUserByUsername(null)
        );

    }

}