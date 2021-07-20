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

    private final long KEY_EXPIRATION_TIME = 1000L * 60 * 30; //3분

    private final long REDIS_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 180; //6개월

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
        redisUtil.setDataExpire(loginDto.getNickname(), refreshToken, REDIS_EXPIRATION_TIME);
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
     * 전화번호로 인증번호를 보내는 로직
     * @param phoneNumber
     * @exception 1.phoneNumber로 찾은 User가 null이라면 UserNotFoundException()
     * @return 문자로 인증번호 전송
     * @author 배태현
     */
    @Override
    public String sendAuthKey(String phoneNumber) {
        UserEntity findByPhoneNumber = userRepository.findByPhoneNumber(phoneNumber);
        if (findByPhoneNumber == null) throw new UserNotFoundException();

        String authKey = keyUtil.getKey(4);
        redisUtil.setDataExpire(authKey, findByPhoneNumber.getNickname(), KEY_EXPIRATION_TIME);

        Message coolsms = new Message(apiKey, apiSecret);
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to", phoneNumber);
        params.put("from", "01049977055");
        params.put("type", "SMS");
        params.put("text", "[EZY] 인증번호 "+authKey+" 를 입력하세요.");
        params.put("app_version", "test app 1.2");

        try {
            JSONObject obj = coolsms.send(params);
            System.out.println(obj.toString());
            return phoneNumber + "로 인증번호 전송 완료";
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
        return phoneNumber + "로 인증번호 전송 완료";
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
        UserEntity findUser = userRepository.findByNickname(username);
        if (findUser == null) throw new UserNotFoundException(); //인증번호가 올바르지 않습니다
        redisUtil.deleteData(key);
        return username + "님 휴대전화 인증 완료";
    }

    /**
     * nickname을 변경하는 서비스 로직
     * @param nicknameDto nickname, newNickname
     * @return
     */
    @Override
    @Transactional
    public String changeNickname(NicknameDto nicknameDto) {
        UserEntity findUser = userRepository.findByNickname(nicknameDto.getNickname());
        if (findUser == null) throw new UserNotFoundException();
        findUser.updateNickname(nicknameDto.getNewNickname());
        return nicknameDto.getNickname() + "유저 " + nicknameDto.getNewNickname() + "로 닉네임 업데이트 완료";
    }

    /**
     * 비밀번호를 변경하는 서비스 로직
     * @param passwordChangeDto nickname, newPassword
     * @return (회원닉네임)회원 비밀번호 변경완료
     * @author 배태현
     */
    @Override
    @Transactional
    public String changePassword(PasswordChangeDto passwordChangeDto) {
        UserEntity findUser = userRepository.findByNickname(passwordChangeDto.getNickname());
        if (findUser == null) throw new UserNotFoundException();
        findUser.updatePassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));

        return passwordChangeDto.getNickname() + "회원 비밀번호 변경완료";
    }

    /**
     * 회원탈퇴 서비스 로직
     * @param deleteUserDto
     * @return (회원이름)회원 회원탈퇴완료
     * @author 배태현
     */
    @Override
    @Transactional
    public String deleteUser(DeleteUserDto deleteUserDto) {
        UserEntity findUser = userRepository.findByNickname(deleteUserDto.getNickname());
        if (findUser == null) throw new UserNotFoundException();
        if (passwordEncoder.matches(deleteUserDto.getPassword(), findUser.getPassword())) {
            userRepository.deleteById(findUser.getUserIdx());
        }
        return deleteUserDto.getNickname() + "회원 회원탈퇴완료";
    }
}
