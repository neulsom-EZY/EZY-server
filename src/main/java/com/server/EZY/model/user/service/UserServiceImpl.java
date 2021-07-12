package com.server.EZY.model.user.service;

import com.server.EZY.exception.response.CustomException;
import com.server.EZY.exception.user.exception.UserNotFoundException;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.dto.*;
import com.server.EZY.model.user.repository.UserRepository;
import com.server.EZY.security.jwt.JwtTokenProvider;
import com.server.EZY.util.KeyUtil;
import com.server.EZY.util.RedisUtil;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;
    private final KeyUtil keyUtil;

    @Value("${sms.api.apikey}")
    private String apiKey;

    @Value("${sms.api.apiSecret}")
    private String apiSecret;

    private long KEY_EXPIRATION_TIME = 60 * 30L;

    @Override
    public String signup(UserDto userDto) {
        if(!userRepository.existsByNickname(userDto.getNickname())){
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(userDto.toEntity());

            String token = jwtTokenProvider.createToken(userDto.getNickname(), userDto.toEntity().getRoles());

            return "Bearer " + token;
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public Map<String, String> signin(LoginDto loginDto) {
        UserEntity findUser = userRepository.findByNickname(loginDto.getNickname());
        if (findUser == null) throw new UserNotFoundException();
        // 비밀번호 검증
        boolean passwordCheck = passwordEncoder.matches(loginDto.getPassword(), findUser.getPassword());
        if (!passwordCheck) throw new UserNotFoundException();

        String accessToken = jwtTokenProvider.createToken(loginDto.getNickname(), loginDto.toEntity().getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        redisUtil.deleteData(loginDto.getNickname()); // accessToken이 만료되지않아도 로그인 할 때 refreshToken도 초기화해서 다시 생성 후 redis에 저장한다.
        redisUtil.setDataExpire(loginDto.getNickname(), refreshToken, 360000 * 1000l* 24 * 180);
        Map<String ,String> map = new HashMap<>();
        map.put("nickname", loginDto.getNickname());
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
     * 전화번호를 인증하는 서비스 로직
     * @param phoneNumberDto phoneNumber
     * @exception 1.phoneNumber로 찾은 User가 null이라면 UserNotFoundException()
     * @return true (findByPhoneNumber로 User를 찾았을 때) / false는 뜨지 않습니다.. 무조건 true
     * @author 배태현
     */
    @Override
    public String sendAuthKey(PhoneNumberDto phoneNumberDto) {
        UserEntity findByPhoneNumber = userRepository.findByPhoneNumber(phoneNumberDto.getPhoneNumber());
        if (findByPhoneNumber == null) throw new UserNotFoundException();

        String authKey = keyUtil.getKey(4);
        redisUtil.setDataExpire(authKey, findByPhoneNumber.getNickname(), KEY_EXPIRATION_TIME);

        Message coolsms = new Message(apiKey, apiSecret);
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to", phoneNumberDto.getPhoneNumber());
        params.put("from", "01049977055");
        params.put("type", "SMS");
        params.put("text", "[EZY] 인증번호 "+authKey+" 를 입력하세요.");
        params.put("app_version", "test app 1.2");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
            return phoneNumberDto.getPhoneNumber() + "로 인증번호 전송 완료";
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
        return phoneNumberDto.getPhoneNumber() + "로 인증번호 전송 완료";
    }

    @Override
    public String validAuthKey(String key) {
        String username = redisUtil.getData(key);
        UserEntity findUser = userRepository.findByNickname(username);
        if (findUser == null) throw new UserNotFoundException();
        redisUtil.deleteData(key);
        return username + "님 휴대전화 인증 완료";
    }

    /**
     * 비밀번호를 변경하는 서비스 로직
     * @param passwordChangeDto nickname, currentPassword, newPassword
     * @return (회원닉네임)회원 비밀번호 변경완료
     * @author 배태현
     */
    @Override
    @Transactional
    public String changePassword(PasswordChangeDto passwordChangeDto) {
        UserEntity findUser = userRepository.findByNickname(passwordChangeDto.getNickname());
        if (findUser == null) throw new UserNotFoundException();
        if (passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), findUser.getPassword())) {
            findUser.updatePassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
        }
        return passwordChangeDto.getNickname() + "회원 비밀번호 변경완료";
    }

    /**
     * 회원탈퇴 서비스 로직
     * @param withdrawalDto withdrawalDto
     * @return (회원이름)회원 회원탈퇴완료
     * @author 배태현
     */
    @Override
    @Transactional
    public String withdrawal(WithdrawalDto withdrawalDto) {
        UserEntity findUser = userRepository.findByNickname(withdrawalDto.getNickname());
        if (findUser == null) throw new UserNotFoundException();
        if (passwordEncoder.matches(withdrawalDto.getPassword(), findUser.getPassword())) {
            userRepository.deleteById(findUser.getUserIdx());
        }
        return withdrawalDto.getNickname() + "회원 회원탈퇴완료";
    }
}
