package com.server.EZY.model.member.service;

import com.server.EZY.exception.authentication_number.exception.AuthenticationNumberTransferFailedException;
import com.server.EZY.exception.authentication_number.exception.InvalidAuthenticationNumberException;
import com.server.EZY.exception.user.exception.MemberAlreadyExistException;
import com.server.EZY.exception.user.exception.MemberNotFoundException;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.*;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.security.jwt.JwtTokenProvider;
import com.server.EZY.util.CurrentUserUtil;
import com.server.EZY.util.KeyUtil;
import com.server.EZY.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;
    private final KeyUtil keyUtil;
    private final CurrentUserUtil currentUserUtil;

    @Value("${sms.api.apikey}")
    private String apiKey;

    @Value("${sms.api.apiSecret}")
    private String apiSecret;

    private final long KEY_EXPIRATION_TIME = 1000L * 60 * 30; //3분

    private long REDIS_EXPIRATION_TIME = JwtTokenProvider.REFRESH_TOKEN_VALIDATION_TIME; //6개월

    /**
     * 회원가입 서비스 로직
     * @param memberDto memberDto(username, password, phoneNumber, fcmToken)
     * @return - save가 완료되면 test코드를 위한 memberEntity를 반환합니다.
     * @exception - else, 이미 존재하는 정보의 회원이라면 MemberAlreadyExistException
     * @author 배태현
     */
    @Override
    public MemberEntity signup(MemberDto memberDto) {
        if(!memberRepository.existsByUsername(memberDto.getUsername())){
            memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

            return memberRepository.save(memberDto.toEntity());
        } else {
            throw new MemberAlreadyExistException();
        }
    }

    /**
     * 로그인 서비스 로직
     * @param loginDto loginDto(username, password)
     * @exception - username으로 member를 찾을 수 없거나, 올바르지 않은 비밀번호라면 MemberNotFoundException
     * @return Map<String ,String> (username, accessToken, refreshToken) 반환
     * @author 배태현
     */
    @Override
    @Transactional
    public Map<String, String> signin(AuthDto loginDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(loginDto.getUsername());
        if (memberEntity == null) throw new MemberNotFoundException();

        boolean passwordCheck = passwordEncoder.matches(loginDto.getPassword(), memberEntity.getPassword());
        if (!passwordCheck) throw new MemberNotFoundException();

        String accessToken = jwtTokenProvider.createToken(memberEntity.getUsername(), memberEntity.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        redisUtil.deleteData(memberEntity.getUsername()); // accessToken이 만료되지않아도 로그인 할 때 refreshToken도 초기화해서 다시 생성 후 redis에 저장한다.
        redisUtil.setDataExpire(memberEntity.getUsername(), refreshToken, REDIS_EXPIRATION_TIME);

        Map<String ,String> map = new HashMap<>();
        map.put("username", loginDto.getUsername());
        map.put("accessToken", "Bearer " + accessToken); // accessToken 반환
        map.put("refreshToken", "Bearer " + refreshToken); // refreshToken 반환

        return map;
    }

    /**
     * 로그아웃 서비스 로직
     * (redis에 있는 refreshToken을 지워준다) (Client는 accessToken을 지워준다)
     * @param nickname
     * @author 배태현
     */
    @Override
    public void logout(String nickname) {
        redisUtil.deleteData(nickname);
    }

    /**
     * 전화번호로 인증번호를 보내는 서비스 로직
     * @param phoneNumber
     * @exception - phoneNumber로 member를 찾을 수 없다면 UserNotFoundException
     * @author 배태현
     */
    @Override
    public void sendAuthKey(String phoneNumber) {
        String authKey = keyUtil.getKey(4);
        redisUtil.setDataExpire(authKey, authKey, KEY_EXPIRATION_TIME);

        Message coolsms = new Message(apiKey, apiSecret);
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to", phoneNumber);
        params.put("from", "07080283503");
        params.put("type", "SMS");
        params.put("text", "[EZY] 인증번호 "+authKey+" 를 입력하세요.");
        params.put("app_version", "test app 1.2");

        try {
            JSONObject obj = coolsms.send(params);
            log.debug(obj.toString());
        } catch (CoolsmsException e) {
            log.debug(e.getMessage());
            log.debug(String.valueOf(e.getCode()));
            throw new AuthenticationNumberTransferFailedException();
        }
    }

    /**
     * 사용자가 문자로 받은 인증번호를 검증하는 서비스 로직
     * @param key
     * @return test코드 작성을 위한 key 반환
     * @author 배태현
     */
    @Override
    public String validAuthKey(String key) {
        if (key.equals(redisUtil.getData(key))) {
            redisUtil.deleteData(key);
            return key;
        } else {
            throw new InvalidAuthenticationNumberException();
        }
    }

    /**
     * username을 변경하는 서비스 로직
     * @param usernameChangeDto usernameChangeDto(username, newUsername)
     * @author 배태현
     */
    @Override
    @Transactional
    public void changeUsername(UsernameChangeDto usernameChangeDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(usernameChangeDto.getUsername());
        if (memberEntity == null) throw new MemberNotFoundException();

        memberEntity.updateUsername(usernameChangeDto.getNewUsername());
    }

    /**
     * 비밀번호를 변경하는 서비스 로직
     * @param passwordChangeDto passwordChangeDto(username, newPassword)
     * @author 배태현
     */
    @Override
    @Transactional
    public void changePassword(PasswordChangeDto passwordChangeDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(passwordChangeDto.getUsername());
        if (memberEntity == null) throw new MemberNotFoundException();

        memberEntity.updatePassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
    }

    /**
     * 전화번호를 변경하는 서비스 로직
     * @param phoneNumberChangeDto phoneNumberChangeDto(username, newPhoneNumber)
     * @author 배태현
     */
    @Override
    @Transactional
    public void changePhoneNumber(PhoneNumberChangeDto phoneNumberChangeDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(phoneNumberChangeDto.getUsername());
        if (memberEntity == null) throw new MemberNotFoundException();

        memberEntity.updatePhoneNumber(phoneNumberChangeDto.getNewPhoneNumber());
    }

    /**
     * 회원탈퇴 서비스 로직
     * @param deleteUserDto deleteUserDto(username, password)
     * @exception - 올바르지 않는 비밀번호일 시 MemberNotFoundException
     * @author 배태현
     */
    @Override
    public void deleteUser(AuthDto deleteUserDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(deleteUserDto.getUsername());
        if (memberEntity == null) throw new MemberNotFoundException();

        if (passwordEncoder.matches(deleteUserDto.getPassword(), memberEntity.getPassword())) {
            memberRepository.deleteById(memberEntity.getMemberIdx());
        } else throw new MemberNotFoundException();
    }

    /**
     * fcmToken 변경 서비스로직
     * @param fcmTokenDto fcmTokenDto(fcmToken)
     * @author 배태현
     */
    @Override
    @Transactional
    public void updateFcmToken(FcmTokenDto fcmTokenDto) {
        MemberEntity currentUser = currentUserUtil.getCurrentUser();

        currentUser.updateFcmToken(fcmTokenDto.getFcmToken());
    }
}
