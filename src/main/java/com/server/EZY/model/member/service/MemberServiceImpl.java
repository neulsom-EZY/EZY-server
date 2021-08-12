package com.server.EZY.model.member.service;

import com.server.EZY.exception.authenticationNumber.exception.AuthenticationNumberTransferFailedException;
import com.server.EZY.exception.response.CustomException;
import com.server.EZY.exception.authenticationNumber.exception.InvalidAuthenticationNumberException;
import com.server.EZY.exception.user.exception.MemberAlreadyExistException;
import com.server.EZY.exception.user.exception.MemberNotFoundException;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.*;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.security.jwt.JwtTokenProvider;
import com.server.EZY.util.KeyUtil;
import com.server.EZY.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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

    @Value("${sms.api.apikey}")
    private String apiKey;

    @Value("${sms.api.apiSecret}")
    private String apiSecret;

    private final long KEY_EXPIRATION_TIME = 1000L * 60 * 30; //3분

    private long REDIS_EXPIRATION_TIME = JwtTokenProvider.REFRESH_TOKEN_VALIDATION_TIME; //6개월

    /**
     * 회원가입을 하는 서비스 로직 입니다.
     * @param memberDto
     * @return - save가 완료되면 memberEntity를 반환합니다.
     * @exception - else, 이미 존재하면 MemberAlreadyExistException
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
     * 로그인을 하는 서비스 로직 입니다.
     * @param loginDto
     * @exception 1. username을 통해 회원을 찾을 수 있나요? || loginDto.getPassword()와 찾은 member의 password가 일치한가요?
     * -> 하나라도 충족되지 않으면 MemberNotFoundException
     * @return Map<String ,String> (username, accessToken, refreshToken)을 반환 합니다.
     * @author 배태현
     */
    @Override
    public Map<String, String> signin(AuthDto loginDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(loginDto.getUsername());
        boolean passwordCheck = passwordEncoder.matches(loginDto.getPassword(), memberEntity.getPassword());
        if (memberEntity == null || !passwordCheck) throw new MemberNotFoundException();

        String accessToken = jwtTokenProvider.createToken(loginDto.getUsername(), loginDto.toEntity().getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        redisUtil.deleteData(loginDto.getUsername()); // accessToken이 만료되지않아도 로그인 할 때 refreshToken도 초기화해서 다시 생성 후 redis에 저장한다.
        redisUtil.setDataExpire(loginDto.getUsername(), refreshToken, REDIS_EXPIRATION_TIME);

        Map<String ,String> map = new HashMap<>();
        map.put("username", loginDto.getUsername());
        map.put("accessToken", "Bearer " + accessToken); // accessToken 반환
        map.put("refreshToken", "Bearer " + refreshToken); // refreshToken 반환

        return map;
    }

    /**
     * 로그아웃하는 서비스 로직
     * (redis에 있는 refreshToken을 지워준다) (Client는 accessToken을 지워준다)
     * @param nickname
     * @return void
     * @author 배태현
     */
    @Override
    public void logout(String nickname) {
        redisUtil.deleteData(nickname);
    }

    /**
     * 전화번호로 인증번호를 보내는 로직
     * @param phoneNumber
     * @exception 1. phoneNumber로 찾은 User가 null이라면 UserNotFoundException()
     * @return 문자로 인증번호 전송
     * @author 배태현
     */
    @Override
    public void sendAuthKey(String phoneNumber) {
        String authKey = keyUtil.getKey(4);
        redisUtil.setDataExpire(authKey, authKey, KEY_EXPIRATION_TIME);

        Message coolsms = new Message(apiKey, apiSecret);
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to", phoneNumber);
        params.put("from", "01049977055");
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
     * 문자로 받은 인증번호로 인증하는 로직
     * @param key
     * @return key
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
     * 전화번호를 전송해 그 전화번호로
     * 회원을 찾고 회원의 이름을 알려주는 로직
     * @param phoneNumber
     * @return memberEntity.getUsername()
     * @author 배태현
     */
    @Override
    public String findUsername(String phoneNumber) {
        MemberEntity memberEntity = memberRepository.findByPhoneNumber(phoneNumber);
        if (memberEntity == null) throw new MemberNotFoundException();

        return memberEntity.getUsername();
    }

    /**
     * username을 변경하는 서비스 로직
     * @param usernameChangeDto username, newUsername
     * @return void
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
     * @param passwordChangeDto username, newPassword
     * @return void
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
     * @param phoneNumberChangeDto username, newPhoneNumber
     * @return void
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
     * @param deleteUserDto
     * @return void
     * @author 배태현
     */
    @Override
    public void deleteUser(AuthDto deleteUserDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(deleteUserDto.getUsername());
        if (memberEntity == null) throw new MemberNotFoundException();

        if (passwordEncoder.matches(deleteUserDto.getPassword(), memberEntity.getPassword())) {
            memberRepository.deleteById(memberEntity.getMemberIdx());
        }
    }
}
