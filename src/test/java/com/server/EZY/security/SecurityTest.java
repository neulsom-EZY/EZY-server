package com.server.EZY.security;


import com.server.EZY.dto.UserDto;
import com.server.EZY.exception.AccessTokenExpiredException;
import com.server.EZY.exception.UserNotFoundException;
import com.server.EZY.model.user.Role;
import com.server.EZY.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;


@SpringBootTest
@Slf4j
public class SecurityTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MyUserDetails myUserDetails;
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void tokenTest() {

        UserDto userDto = new UserDto();
        userDto.setNickname("배태현");
        userDto.setPhoneNumber("010-1234-1234");
        userDto.setPassword("1234");

        String accessToken = jwtTokenProvider.createToken(userDto.getNickname(), userDto.toEntity().getRoles());
        // 유효한 토큰인지 확인
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)){
            String nickname = jwtTokenProvider.getUsername(accessToken);
            Assertions.assertThat(nickname).isEqualTo("배태현");
        }
    }

       
}

