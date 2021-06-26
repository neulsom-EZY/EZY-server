package com.server.EZY.model.user.service;

import com.server.EZY.exception.response.CustomException;
import com.server.EZY.exception.user.UserNotFoundException;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.dto.LoginDto;
import com.server.EZY.model.user.dto.PasswordChangeDto;
import com.server.EZY.model.user.dto.PhoneNumberDto;
import com.server.EZY.model.user.dto.UserDto;
import com.server.EZY.model.user.repository.UserRepository;
import com.server.EZY.security.jwt.JwtTokenProvider;
import com.server.EZY.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;


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

    @Override
    public String logout() {
        return null;
    }

    @Override
    public Boolean validPhoneNumber(PhoneNumberDto phoneNumberDto) {
        UserEntity findByPhoneNumber = userRepository.findByPhoneNumber(phoneNumberDto.getPhoneNumber());
        if (findByPhoneNumber == null) throw new UserNotFoundException();
        else return true;
    }

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

    @Override
    public String changeNickname() {
        return null;
    }


}
