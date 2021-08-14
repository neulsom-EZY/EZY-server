package com.server.EZY.security.authentication;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MyUserDetailsTest {

    @Autowired MyUserDetails myUserDetails;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    void before() {
        //given
        MemberDto memberDto = MemberDto.builder()
                .username("@BaeTul")
                .password("0809")
                .phoneNumber("01008090809")
                .build();
        //when
        MemberEntity memberEntity = memberService.signup(memberDto);

        //then
        assertEquals("@BaeTul", memberEntity.getUsername());
    }

    @Test
    public void loadByUsernameTest() {
        //given //when
        UserDetails member = myUserDetails.loadUserByUsername("@BaeTul");

        //then
        assertNotNull(member);
        assertEquals("@BaeTul", member.getUsername());
    }

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