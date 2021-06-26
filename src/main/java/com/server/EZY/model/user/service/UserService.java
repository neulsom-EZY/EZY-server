package com.server.EZY.model.user.service;

import com.server.EZY.model.user.dto.LoginDto;
import com.server.EZY.model.user.dto.PasswordChangeDto;
import com.server.EZY.model.user.dto.PhoneNumberDto;
import com.server.EZY.model.user.dto.UserDto;

import java.util.Map;


public interface UserService {

    /**
     * 회원가입을 하는 서비스 로직 입니다.
     * @param userDto
     * @return - if, save 완료 시 token return.
     * @exception - else, 이미 존재하면 userAlreadyExist 터트리기.
     * @author 전지환
     */
    String signup(UserDto userDto);

    /**
     * 로그인을 하는 서비스 로직 입니다.
     * @param loginDto
     * @exception 1. email을 통해 회원을 찾을 수 있나요? -> false, UserNotFoundException();
     *            2. 해당 회원의 비밀번호가 loginDto.getPassword()와 일치하나요? -> false, UserNotFoundException();
     * @return 서두에 있는 모든 조건을 만족할 시에  Map<String ,String> 을 반환 합니다.
     * @author 전지환
     */
    Map<String, String> signin(LoginDto loginDto);

    String logout();

    Boolean validPhoneNumber(PhoneNumberDto phoneNumberDto);

    String changePassword(PasswordChangeDto passwordChangeDto);

    String changeNickname();
}
