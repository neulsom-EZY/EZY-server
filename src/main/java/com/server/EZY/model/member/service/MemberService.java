package com.server.EZY.model.member.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public interface MemberService {

    /**
     * 회원가입을 하는 서비스 로직 입니다.
     * @param memberDto
     * @return - if, save 완료 시 token return.
     * @exception - else, 이미 존재하면 userAlreadyExist 터트리기.
     * @author 전지환
     */
    MemberEntity signup(MemberDto memberDto);

    /**
     * 로그인을 하는 서비스 로직 입니다.
     * @param loginDto
     * @exception 1. username을 통해 회원을 찾을 수 있나요? -> false, UserNotFoundException();
     * @exception 2. 해당 회원의 비밀번호가 loginDto.getPassword()와 일치하나요? -> false, UserNotFoundException();
     * @return 서두에 있는 모든 조건을 만족할 시에  Map<String ,String> 을 반환 합니다.
     * @author 전지환
     */
    Map<String, String> signin(AuthDto loginDto);

    String logout(HttpServletRequest request);

    void sendAuthKey(String phoneNumber);

    String validAuthKey(String key);

    String findUsername(String phoneNumber);

    void changeUsername(UsernameChangeDto usernameChangeDto);

    void changePassword(PasswordChangeDto passwordChangeDto);

    void changePhoneNumber(PhoneNumberChangeDto phoneNumberChangeDto);

    void deleteUser(AuthDto deleteUserDto);

}
