package com.server.EZY.security;

import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.security.authentication.MyUserDetails;
import com.server.EZY.security.jwt.JwtTokenProvider;
import com.server.EZY.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


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

        MemberDto memberDto = MemberDto.builder()
                .username("배태현")
                .phoneNumber("01012341234")
                .password("1234")
                .build();

        String accessToken = jwtTokenProvider.createToken(memberDto.getUsername(), memberDto.toEntity().getRoles());

        String nickname = jwtTokenProvider.getUsername(accessToken);

        assertNotNull(accessToken);
        assertTrue(jwtTokenProvider.validateToken(accessToken));
        assertThat(nickname).isEqualTo("배태현");
    }

}

