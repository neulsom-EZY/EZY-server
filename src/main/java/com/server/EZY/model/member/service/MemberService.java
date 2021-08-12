package com.server.EZY.model.member.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public interface MemberService {

    MemberEntity signup(MemberDto memberDto);

    Map<String, String> signin(AuthDto loginDto);

    void logout(String nickname);

    void sendAuthKey(String phoneNumber);

    String validAuthKey(String key);

    String findUsername(String phoneNumber);

    void changeUsername(UsernameChangeDto usernameChangeDto);

    void changePassword(PasswordChangeDto passwordChangeDto);

    void changePhoneNumber(PhoneNumberChangeDto phoneNumberChangeDto);

    void deleteUser(AuthDto deleteUserDto);

}
