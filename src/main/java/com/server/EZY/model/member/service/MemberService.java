package com.server.EZY.model.member.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.*;

import java.util.Map;


public interface MemberService {

    boolean isExistUsername(String username);

    boolean isExistPhoneNumber(String phoneNumber);

    MemberEntity signup(MemberDto memberDto);

    Map<String, String> signin(AuthDto loginDto);

    void logout(String nickname);

    void sendAuthKey(String phoneNumber);

    String validAuthKey(String key);

    void findUsernameByPhoneNumber(String phoneNumber);

    void changeUsername(UsernameDto usernameDto);

    void sendAuthKeyByMemberInfo(MemberAuthKeySendInfoDto memberAuthKeySendInfoDto);

    void changePassword(PasswordChangeDto passwordChangeDto);

    void changePhoneNumber(PhoneNumberChangeDto phoneNumberChangeDto);

    void deleteUser(AuthDto deleteUserDto);

    void updateFcmToken(FcmTokenDto fcmTokenDto);
}
