package com.server.EZY.model.member.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.*;

import java.util.List;
import java.util.Map;

/**
 * 회원 관련 서비스 로직 선언부
 *
 * @version 1.0.0
 * @author 배태현
 */
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

    /**
     * 존재하는 닉네임인지 검색할 수 있는 메소드
     *
     * @param keyword
     * @return List<UsernameResponseDto>
     * @author 전지환
     */
    List<UsernameResponseDto> searchUser(String keyword);
}
