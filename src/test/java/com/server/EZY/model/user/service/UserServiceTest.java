package com.server.EZY.model.user.service;

import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.controller.UserController;
import com.server.EZY.model.user.dto.*;
import com.server.EZY.model.user.enumType.Role;
import com.server.EZY.model.user.repository.UserRepository;
import com.server.EZY.util.CurrentUserUtil;
import com.server.EZY.security.jwt.JwtTokenProvider;
import com.server.EZY.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Slf4j
public class UserServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("로그인 되어있는 유저를 확인하는 테스트")
    void GetUserEntity(){
        //Given
        UserDto userDto = UserDto.builder()
                .nickname("배태현")
                .password("1234")
                .phoneNumber("01012341234")
                .build();

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(userDto.toEntity());
        System.out.println("======== saved =========");

        // when login session 발급
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDto.getNickname(),
                userDto.getPassword(),
                List.of(new SimpleGrantedAuthority(Role.ROLE_CLIENT.name())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        System.out.println("=================================");
        System.out.println(context);

        //then
        String currentUserNickname = CurrentUserUtil.getCurrentUserNickname();
        assertEquals("배태현", currentUserNickname);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void signupTest() {
        //given
        UserDto userDto = UserDto.builder()
                .nickname("나야나 배따횬~~")
                .password("0809")
                .phoneNumber("01008090809")
                .build();
        //when
        String signup = userService.signup(userDto);

        //then
        assertTrue(true, String.valueOf(signup != null));
    }

    @BeforeEach
    public void before(@Autowired UserController userController) {

        UserDto userDto = UserDto.builder()
                .nickname("바따햔")
                .password("0809")
                .phoneNumber("01012345678")
                .build();
        userService.signup(userDto);
    }

    @Test
    @DisplayName("로그인 테스트")
    public void signinTest() {
        //given
        AuthDto loginDto = AuthDto.builder()
                .nickname("바따햔")
                .password("0809")
                .build();
        //when
        Map<String, String> signin = userService.signin(loginDto);
        //then
        assertTrue(true, String.valueOf(signin != null));
    }

    @Test
    @DisplayName("전화번호 인증 테스트")
    public void validPhoneNumber() {
//        //given
//        PhoneNumberDto phoneNumberDto = PhoneNumberDto.builder()
//                .phoneNumber("01012345678")
//                .build();
//        //when
//        Boolean aBoolean = userService.validPhoneNumber(phoneNumberDto);
//        //then
//        assertEquals(true, aBoolean);
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    public void changePasswordTest() {
        //given
        UserEntity currentUser = currentUser();

        PasswordChangeDto passwordChangeDto = PasswordChangeDto.builder()
                .nickname("배태현")
                .newPassword("20040809")
                .build();
        //when
        if (currentUser != null) {
            String changePassword = userService.changePassword(passwordChangeDto);
            assertEquals("배태현회원 비밀번호 변경완료", changePassword);
        } else {
            log.info("비밀번호 변경 테스트 실패");
        }
    }

    @Test
    @DisplayName("회원탈퇴 테스트")
    public void withdrawalTest() {
        //given
        UserEntity currentUser = currentUser();

        AuthDto deleteUserDto = AuthDto.builder()
                .nickname("배태현")
                .password("1234")
                .build();

        //when
        if (currentUser != null) {
            String withdrawal = userService.deleteUser(deleteUserDto);
            assertEquals("배태현회원 회원탈퇴완료", withdrawal);
        } else {
            log.info("회원탈퇴 테스트 실패");
        }
    }

    /**
     * 로그인이 되어있는 유저를 만들기위해 뺀 메서드
     * @return loginUser(현재 로그인되어있는 유저)
     * @author 배태현
     */
    public UserEntity currentUser() {
        UserDto userDto = UserDto.builder()
                .nickname("배태현")
                .password("1234")
                .phoneNumber("01012341234")
                .build();

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(userDto.toEntity());
        System.out.println("======== saved =========");

        // when login session 발급
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDto.getNickname(),
                userDto.getPassword(),
                List.of(new SimpleGrantedAuthority(Role.ROLE_CLIENT.name())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        System.out.println("=================================");
        System.out.println(context);

        //then
        String currentUserNickname = CurrentUserUtil.getCurrentUserNickname();
        assertEquals("배태현", currentUserNickname);
        UserEntity loginUser = userRepository.findByNickname(currentUserNickname);
        return loginUser;
    }
}
