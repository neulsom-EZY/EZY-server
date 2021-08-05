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

    @Override
    public String signup(MemberDto memberDto) {
        if(!memberRepository.existsByUsername(memberDto.getUsername())){
            memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

            MemberEntity memberEntity = memberRepository.save(memberDto.toEntity());

            return memberEntity.getUsername();
        } else {
            throw new MemberAlreadyExistException();
        }
    }

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
     * 로그아웃하는 서비스 로직 (redis에 있는 refreshToken을 지워준다) (Client는 accessToken을 지워준다)
     * @param request HttpServletRequest
     * @return "로그아웃 되었습니다."
     * @author 배태현
     */
    @Override
    public String logout(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsername(accessToken);
        redisUtil.deleteData(username);
        return "로그아웃 되었습니다.";
    }

    /**
     * 전화번호로 인증번호를 보내는 로직 (이미 회원가입 된 회원에게)
     * @param phoneNumber
     * @exception 1.phoneNumber로 찾은 User가 null이라면 UserNotFoundException()
     * @return 문자로 인증번호 전송
     * @author 배태현
     */
    @Override
    public void sendAuthKey(String phoneNumber) {
        MemberEntity findByPhoneNumber = memberRepository.findByPhoneNumber(phoneNumber);
        if (findByPhoneNumber == null) throw new AuthenticationNumberTransferFailedException();

        String authKey = keyUtil.getKey(4);
        redisUtil.setDataExpire(authKey, findByPhoneNumber.getUsername(), KEY_EXPIRATION_TIME);

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
     * @return SuccessResult
     * @author 배태현
     */
    @Override
    public String validAuthKey(String key) {
        String username = redisUtil.getData(key);

        MemberEntity memberEntity = memberRepository.findByUsername(username);
        if (memberEntity == null) throw new InvalidAuthenticationNumberException();

        redisUtil.deleteData(key);

        return username + "님 휴대전화 인증 완료";
    }

    /**
     * 전화번호 인증을 완료한 뒤
     * 전화번호를 한번 더 전송해 그 전화번호로
     * 회원을 찾고 회원의 이름을 알려주는 로직
     * @param phoneNumber
     * @return Username
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
     * @return
     */
    @Override
    @Transactional
    public String changeUsername(UsernameChangeDto usernameChangeDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(usernameChangeDto.getUsername());
        if (memberEntity == null) throw new MemberNotFoundException();

        memberEntity.updateUsername(usernameChangeDto.getNewUsername());

        return usernameChangeDto.getUsername() + "유저 " + usernameChangeDto.getNewUsername() + "(으)로 닉네임 업데이트 완료";
    }

    /**
     * 비밀번호를 변경하는 서비스 로직
     * @param passwordChangeDto username, newPassword
     * @return (회원닉네임)회원 비밀번호 변경완료
     * @author 배태현
     */
    @Override
    @Transactional
    public String changePassword(PasswordChangeDto passwordChangeDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(passwordChangeDto.getUsername());
        if (memberEntity == null) throw new MemberNotFoundException();
        memberEntity.updatePassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));

        return passwordChangeDto.getUsername() + "회원 비밀번호 변경완료";
    }

    /**
     * 전화번호를 변경하는 서비스 로직
     * @param phoneNumberChangeDto username, newPhoneNumber
     * @return username
     */
    @Override
    @Transactional
    public String changePhoneNumber(PhoneNumberChangeDto phoneNumberChangeDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(phoneNumberChangeDto.getUsername());
        if (memberEntity == null) throw new MemberNotFoundException();
        memberEntity.updatePhoneNumber(phoneNumberChangeDto.getNewPhoneNumber());

        return memberEntity.getUsername();
    }

    /**
     * 회원탈퇴 서비스 로직
     * @param deleteUserDto
     * @return (회원이름)회원 회원탈퇴완료
     * @author 배태현
     */
    @Override
    public String deleteUser(AuthDto deleteUserDto) {
        MemberEntity memberEntity = memberRepository.findByUsername(deleteUserDto.getUsername());
        if (memberEntity == null) throw new MemberNotFoundException();

        if (passwordEncoder.matches(deleteUserDto.getPassword(), memberEntity.getPassword())) {
            memberRepository.deleteById(memberEntity.getMemberIdx());
        }

        return deleteUserDto.getUsername() + "회원 회원탈퇴완료";
    }
}
