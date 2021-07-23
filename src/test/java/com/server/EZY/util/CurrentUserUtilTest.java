package com.server.EZY.util;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.enumType.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class CurrentUserUtilTest {

    @Autowired
    private CurrentUserUtil currentUserUtil;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * currentUserUtil에 만든 getCurrentUserNickname 메서드가 잘 동작하는지 확인하는 메서드
     * @author 배태현
     */
    @Test
    public void currentUserNickname() {
        //given
        MemberDto memberDto = MemberDto.builder()
                .nickname("배태현")
                .password("1234")
                .phoneNumber("01012341234")
                .build();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        memberRepository.save(memberDto.toEntity());
        System.out.println("======== saved =========");

        // when login session 발급
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                memberDto.getNickname(),
                memberDto.getPassword(),
                List.of(new SimpleGrantedAuthority(Role.ROLE_CLIENT.name())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        System.out.println("=================================");
        System.out.println(context);

        //then
        String currentUserNickname = CurrentUserUtil.getCurrentUserNickname();
        assertEquals("배태현", currentUserNickname);
    }

    /**
     * currentUserUtil에 만든 getCurrentUser메서드가 잘 작동하는지 확인하는 테스트
     * @author 배태현
     */
    @Test
    public void currentUser() {
        //given
        MemberDto memberDto = MemberDto.builder()
                .nickname("배태현")
                .password("1234")
                .phoneNumber("01012341234")
                .build();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        memberRepository.save(memberDto.toEntity());
        System.out.println("======== saved =========");

        // when login session 발급
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                memberDto.getNickname(),
                memberDto.getPassword(),
                List.of(new SimpleGrantedAuthority(Role.ROLE_CLIENT.name())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        System.out.println("=================================");
        System.out.println(context);

        //then
        MemberEntity currentUser = currentUserUtil.getCurrentUser();
        assertTrue(currentUser != null, "true");
        assertEquals(memberDto.toEntity().getNickname(), currentUser.getNickname());
        assertEquals(memberDto.toEntity().getPhoneNumber(), currentUser.getPhoneNumber());
    }
}