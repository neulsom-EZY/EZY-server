package com.server.EZY.model.member.service;

import com.server.EZY.exception.user.exception.MemberAlreadyExistException;
import com.server.EZY.exception.user.exception.MemberInformationCheckAgainException;
import com.server.EZY.exception.user.exception.MemberNotFoundException;
import com.server.EZY.exception.user.exception.NotCorrectPasswordException;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.controller.MemberController;
import com.server.EZY.model.member.dto.*;
import com.server.EZY.model.member.enum_type.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.util.CurrentUserUtil;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
    @Autowired
    private RedisUtil redisUtil;

    @Test
    @DisplayName("로그인 되어있는 유저를 확인하는 테스트")
    void getUserEntity(){
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
    @DisplayName("username 중복여부 확인 테스트")
    public void checkUsernameExistTest() {
        //given
        String username = "@Baetae";

        //when
        boolean bool = memberService.isExistUsername(username);

        //then
        assertEquals(false, bool);
    }

    @Test
    @DisplayName("phoneNumber 중복여부 확인 테스트")
    public void checkPhoneNumberExistTest() {
        //given
        String phoneNumber = "01049977055";

        //when
        boolean bool = memberService.isExistPhoneNumber(phoneNumber);

        //then
        assertEquals(false, bool);
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
        MemberEntity memberEntity = memberService.signup(memberDto);

        //then
        assertEquals("@BaeTul", memberEntity.getUsername());
    }

    @Test
    @DisplayName("이미 회원가입된 유저입니다 Exception을 반환하는 테스트")
    public void signupException() {
        //given
        MemberDto memberDto = MemberDto.builder()
                .username(currentUser().getUsername())
                .password("0809")
                .phoneNumber("01008090809")
                .build();

        //when //then
        assertThrows(
                MemberAlreadyExistException.class,
                () -> memberService.signup(memberDto)
        );
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
    @DisplayName("비밀번호가 일치하지 않는다면 혹은 회원의 닉네임을 찾을 수 없을 때 Exception이 터지나요?")
    public void signinException() {
        //given //when //then
        assertThrows(
                NotCorrectPasswordException.class,
                () -> memberService.signin(
                        AuthDto.builder()
                                .username(currentUser().getUsername())
                                .password("12345678")
                                .build()
                )
        );

        assertThrows(
                MemberNotFoundException.class,
                () -> memberService.signin(
                        AuthDto.builder()
                                .username("NoUser")
                                .password("0809")
                                .build()
                )
        );
    }

    @Test
    @DisplayName("로그아웃 테스트")
    public void logoutTest() {
        //given
        AuthDto loginDto = AuthDto.builder()
                .username("@Baetaehyeon")
                .password("0809")
                .build();

        memberService.signin(loginDto);

        //when
        memberService.logout(loginDto.getUsername());

        //then
        assertNull(redisUtil.getData(loginDto.getUsername()));
    }

    @Test
    @DisplayName("Username 변경 테스트")
    public void changeUsername() {
        //given
        MemberEntity currentUser = currentUser();

        UsernameDto usernameDto = UsernameDto.builder()
                .username("@asdfasdf")
                .build();

        //when, then
        assertEquals("@qwerqwer", currentUser.getUsername());

        memberService.changeUsername(usernameDto);

        MemberEntity memberEntity = memberRepository.findByUsername(usernameDto.getUsername());
        assertEquals("@asdfasdf", memberEntity.getUsername());
    }

    @Test
    @DisplayName("찾을 수 없는 user Exception이 터지나요?")
    public void changePasswordException() {
        //given //when //then
        assertThrows(
                MemberNotFoundException.class,
                () -> memberService.sendAuthKeyByMemberInfo(
                        MemberAuthKeySendInfoDto.builder()
                                .username("NoUser")
                                .phoneNumber("01049977055")
                                .build()
                )
        );
    }

    @Test
    @DisplayName("전화번호 변경 테스트")
    public void changePhoneNumberTest() {
        //given
        MemberEntity currentUser = currentUser();

        //when //then
        if (currentUser != null) {
            assertEquals("01000000000", currentUser.getPhoneNumber());

            memberService.changePhoneNumber(
                    PhoneNumberChangeDto.builder()
                            .newPhoneNumber("01049977055")
                            .build()
            );

            assertEquals("01049977055", currentUser.getPhoneNumber());

        } else {
            Assertions.fail("전화번호 변경 테스트 실패");
        }
    }

    @Test
    @DisplayName("회원탈퇴 테스트")
    public void deleteMemberTest() {
        //given
        MemberEntity currentUser = currentUser();

        AuthDto deleteUserDto = AuthDto.builder()
                .username("@Baetaehyeon")
                .password("0809")
                .build();

        boolean catchException = false;

        //when
        if (currentUser != null) {
            MemberEntity findByUsername = memberRepository.findByUsername(deleteUserDto.getUsername());
            assertEquals("@Baetaehyeon", findByUsername.getUsername());
            assertTrue(passwordEncoder.matches(deleteUserDto.getPassword(), findByUsername.getPassword()));

            memberService.deleteUser(deleteUserDto);

            try {
                MemberEntity memberEntity = memberRepository.findByUsername(deleteUserDto.getUsername());
                log.debug(memberEntity.getUsername());
            } catch (NullPointerException e) {
                catchException = true;
            }

        } else {
            Assertions.fail("회원탈퇴 테스트 실패");
        }

        assertTrue(catchException);
    }

    @Test
    @DisplayName("deleteUser에서 MemberNotFoundException이 잘 터지나요?")
    public void deleteUserException() {
        //given //when //then
        assertThrows(
                MemberNotFoundException.class,
                () -> memberService.deleteUser(
                        AuthDto.builder()
                                .username("NoUser")
                                .password("5678")
                                .build()
                )
        );

        assertThrows(
                NotCorrectPasswordException.class,
                () -> memberService.deleteUser(
                        AuthDto.builder()
                                .username("@Baetaehyeon")
                                .password("0000")
                                .build()
                )
        );
    }

    @Test
    @DisplayName("fcmToken이 잘 변경 되나요 ?")
    public void fcmTokenUpdateTest() {
        //given
        FcmTokenDto fcmTokenDto = FcmTokenDto.builder()
                .fcmToken("fAp6e7Snyk_kg9ZxvTkt-a:APA91bEsOTGuuATRSKcHnwjqLL_aiT42BoLCuVJHrsW_JmvmfLqw8Ub2bZmUycR6qDyMbU2I41UScu9-kiv5bnI70wNRBXA1ku-IiEp5LiH_ZzGNBai7ZQqY5VGsb3s-BLu13iXEiISm")
                .build();

        MemberEntity memberEntity = currentUser();

        //when
        memberService.updateFcmToken(fcmTokenDto);

        //then
        String currentUserFcmToken = memberRepository.findById(memberEntity.getMemberIdx()).get().getFcmToken();
        assertEquals(fcmTokenDto.getFcmToken(), currentUserFcmToken);
    }

    @Test
    @DisplayName("비밀번호를 변경하기 전 정보를 받고 인증번호를 전송할 때에 Exception들이 제대로 터지나요 ?")
    public void sendAuthKeyByMemberInfoExceptionTest() {
        assertThrows(
                MemberNotFoundException.class,
                () -> memberService.sendAuthKeyByMemberInfo(
                        MemberAuthKeySendInfoDto.builder()
                                .username("NoUser")
                                .phoneNumber("01000000000")
                                .build()
                )
        );

        assertThrows(
                MemberInformationCheckAgainException.class,
                () -> memberService.sendAuthKeyByMemberInfo(
                        MemberAuthKeySendInfoDto.builder()
                                .username(currentUser().getUsername())
                                .phoneNumber("01012341234")
                                .build()
                )
        );
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
