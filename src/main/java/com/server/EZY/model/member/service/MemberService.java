package com.server.EZY.model.member.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.*;

import java.util.Map;


public interface MemberService {

    MemberEntity signup(MemberDto memberDto);

    Map<String, String> signin(AuthDto loginDto);

    void logout(String nickname);

    void sendAuthKey(String phoneNumber);

    String validAuthKey(String key);

    void changeUsername(UsernameChangeDto usernameChangeDto);

    void sendAuthKeyByMemberInfo(MemberAuthKeySendInfoDto memberAuthKeySendInfoDto);

    void changePassword(PasswordChangeDto passwordChangeDto);

    void changePhoneNumber(PhoneNumberChangeDto phoneNumberChangeDto);

    void deleteUser(AuthDto deleteUserDto);

    void updateFcmToken(FcmTokenDto fcmTokenDto);
}
