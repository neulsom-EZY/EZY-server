package com.server.EZY.model.user.service;

import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.controller.UserController;
import com.server.EZY.model.user.dto.*;
import com.server.EZY.model.user.enumType.Role;
import com.server.EZY.model.user.repository.UserRepository;
import com.server.EZY.security.jwt.JwtTokenProvider;
import com.server.EZY.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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
    private UserServiceImpl userServiceImpl;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    @Test
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
        String currentUserNickname = userServiceImpl.getCurrentUserNickname();
        assertEquals("배태현", currentUserNickname);
    }

    @Test
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
    public void signinTest() {
        //given
        LoginDto loginDto = LoginDto.builder()
                .nickname("바따햔")
                .password("0809")
                .build();
        //when
        Map<String, String> signin = userService.signin(loginDto);
        //then
        assertTrue(true, String.valueOf(signin != null));
    }

    @Test
    public void validPhoneNumber() {
        //given
        PhoneNumberDto phoneNumberDto = PhoneNumberDto.builder()
                .phoneNumber("01012345678")
                .build();
        //when
        Boolean aBoolean = userService.validPhoneNumber(phoneNumberDto);
        //then
        assertEquals(true, aBoolean);
    }

    @Test
    public void changePasswordTest() {
        //given
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
        String currentUserNickname = userServiceImpl.getCurrentUserNickname();
        assertEquals("배태현", currentUserNickname);
        UserEntity loginUserNickname = userRepository.findByNickname(currentUserNickname);

        PasswordChangeDto passwordChangeDto = PasswordChangeDto.builder()
                .nickname(currentUserNickname)
                .currentPassword("1234")
                .newPassword("20040809")
                .build();
        //when
        if (loginUserNickname != null) {
            String changePassword = userService.changePassword(passwordChangeDto);
            assertEquals("배태현회원 비밀번호 변경완료", changePassword);
        } else {
            log.info("비밀번호 변경 테스트 실패");
        }
    }

    @Test
    public void withdrawalTest() {
        //given
        WithdrawalDto withdrawalDto = WithdrawalDto.builder()
                .nickname("바따햔")
                .password("0809")
                .build();
        //when
        String withdrawal = userService.withdrawal(withdrawalDto);
        //then
        assertEquals("바따햔회원 회원탈퇴완료", withdrawal);
    }
}
