package com.server.EZY.model.member.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.controller.MemberController;
import com.server.EZY.model.member.dto.*;
import com.server.EZY.model.member.enumType.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.util.CurrentUserUtil;
import com.server.EZY.security.jwt.JwtTokenProvider;
import com.server.EZY.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Slf4j
public class MemberServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("로그인 되어있는 유저를 확인하는 테스트")
    void GetUserEntity(){
        //Given
        MemberDto memberDto = MemberDto.builder()
                .username("@asdfasdf")
                .password("1234")
                .phoneNumber("01012341234")
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
        assertEquals("@asdfasdf", currentUserNickname);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void signupTest() {
        //given
        MemberDto memberDto = MemberDto.builder()
                .username("@BaeTul")
                .password("0809")
                .phoneNumber("01008090809")
                .build();
        //when
        String signup = memberService.signup(memberDto);

        //then
        assertTrue(signup == "@BaeTul");
    }

    @BeforeEach
    public void before(@Autowired MemberController memberController) {

        MemberDto memberDto = MemberDto.builder()
                .username("@Baetaehyeon")
                .password("0809")
                .phoneNumber("01012345678")
                .build();
        memberService.signup(memberDto);
    }

    @Test
    @DisplayName("로그인 테스트")
    public void signinTest() {
        //given
        AuthDto loginDto = AuthDto.builder()
                .username("@Baetaehyeon")
                .password("0809")
                .build();
        //when
        Map<String, String> signin = memberService.signin(loginDto);
        //then
        assertTrue(true, String.valueOf(signin != null));
    }

    @Test
    @DisplayName("username을 찾는 테스트")
    public void findUsername() {
        //given
        String phoneNumber = "01012345678";

        MemberEntity memberEntity = memberRepository.findByPhoneNumber(phoneNumber);

        //when
        String username = memberService.findUsername(phoneNumber);

        //then
        assertEquals(memberEntity.getUsername(), username);
    }

    @Test
    @DisplayName("Username 변경 테스트")
    public void changeUsername() {
        //given
        MemberEntity currentUser = currentUser();

        UsernameChangeDto usernameChangeDto = UsernameChangeDto.builder()
                .username("@Baetaehyeon")
                .newUsername("@asdfasdf")
                .build();
        //when //then
        if (currentUser != null) {
            String changeNickname = memberService.changeUsername(usernameChangeDto);
            assertEquals("@Baetaehyeon유저 @asdfasdf(으)로 닉네임 업데이트 완료", changeNickname);
        } else {
            log.info("닉네임 변경테스트 실패");
        }
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    public void changePasswordTest() {
        //given
        MemberEntity currentUser = currentUser();

        PasswordChangeDto passwordChangeDto = PasswordChangeDto.builder()
                .username("@Baetaehyeon")
                .newPassword("20040809")
                .build();
        //when //then
        if (currentUser != null) {
            String changePassword = memberService.changePassword(passwordChangeDto);
            assertEquals("@Baetaehyeon회원 비밀번호 변경완료", changePassword);
        } else {
            log.info("비밀번호 변경 테스트 실패");
        }
    }

    @Test
    public void changePhoneNumberTest() {
        //given
        MemberEntity currentUser = currentUser();

        //when //then
        if (currentUser != null) {
            String username = memberService.changePhoneNumber(
                    PhoneNumberChangeDto.builder()
                            .username("@qwerqwer")
                            .phoneNumber("01049977055")
                            .build()
            );

            MemberEntity memberEntity = memberRepository.findByUsername(username);

            assertEquals(memberEntity.getUsername(), username);
            assertEquals("01049977055", memberEntity.getPhoneNumber());
        } else {
            log.info("전화번호 변경 테스트 실패");
        }
    }

    @Test
    @DisplayName("회원탈퇴 테스트")
    public void DeleteMemberTest() {
        //given
        MemberEntity currentUser = currentUser();

        AuthDto deleteUserDto = AuthDto.builder()
                .username("@Baetaehyeon")
                .password("1234")
                .build();

        //when
        if (currentUser != null) {
            String withdrawal = memberService.deleteUser(deleteUserDto);
            assertEquals("@Baetaehyeon회원 회원탈퇴완료", withdrawal);
        } else {
            log.info("회원탈퇴 테스트 실패");
        }
    }

    /**
     * 로그인이 되어있는 유저를 만들기위해 뺀 메서드
     * @return loginUser(현재 로그인되어있는 유저)
     * @author 배태현
     */
    public MemberEntity currentUser() {
        MemberDto memberDto = MemberDto.builder()
                .username("@qwerqwer")
                .password("1234")
                .phoneNumber("01000000000")
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
        assertEquals("@qwerqwer", currentUserNickname);
        MemberEntity loginUser = memberRepository.findByUsername(currentUserNickname);
        return loginUser;
    }
}
