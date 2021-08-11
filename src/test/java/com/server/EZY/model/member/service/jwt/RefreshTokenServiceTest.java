package com.server.EZY.model.member.service.jwt;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.enumType.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.security.jwt.JwtTokenProvider;
import com.server.EZY.util.CurrentUserUtil;
import com.server.EZY.util.RedisUtil;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RefreshTokenServiceTest {

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private long REDIS_EXPIRATION_TIME = JwtTokenProvider.REFRESH_TOKEN_VALIDATION_TIME; //6개월

    public MemberEntity currentUser() {
        MemberDto memberDto = MemberDto.builder()
                .username("@Bee")
                .password("1234")
                .phoneNumber("01033333333")
                .build();

        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        memberRepository.save(memberDto.toEntity());
        System.out.println("======== saved =========");

        // when login session 발급
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                memberDto.getUsername(),
                memberDto.getPassword(),
                List.of(new SimpleGrantedAuthority(Role.ROLE_CLIENT.name())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        System.out.println("=================================");
        System.out.println(context);

        //then
        String currentUserNickname = CurrentUserUtil.getCurrentUsername();
        assertEquals("@Bee", currentUserNickname);
        MemberEntity loginUser = memberRepository.findByUsername(currentUserNickname);
        return loginUser;
    }

    @Test
    @DisplayName("토큰들이 재발급 되는지 확인하는 테스트")
    public void refreshTest() {
        //given
        String nickname = currentUser().getUsername();
        String refreshToken = jwtTokenProvider.createRefreshToken();
        redisUtil.setDataExpire(nickname, refreshToken, REDIS_EXPIRATION_TIME);

        //when
        Map<String, String> tokenMap = refreshTokenService.getRefreshToken(nickname, refreshToken);

        String newAccessToken = tokenMap.get("NewAccessToken");
        //검증
        assertTrue(newAccessToken.startsWith("Bearer "));

        newAccessToken = newAccessToken.substring(7);

        //then
        assertThat(tokenMap.get("nickname")).isEqualTo(nickname);
        assertNotNull(newAccessToken);
        assertThat(jwtTokenProvider.getUsername(newAccessToken)).isEqualTo(nickname);
        assertNotEquals(refreshToken, tokenMap.get("NewRefreshToken"));
    }
}