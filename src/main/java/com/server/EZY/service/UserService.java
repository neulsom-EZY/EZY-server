package com.server.EZY.service;

import com.server.EZY.dto.LoginDto;
import com.server.EZY.dto.UserDto;
import com.server.EZY.exception.CustomException;
import com.server.EZY.exception.UserNotFoundException;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.repository.user.UserRepository;
import com.server.EZY.security.JwtTokenProvider;
import com.server.EZY.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RedisUtil redisUtil;

    public Map<String, String> signin(LoginDto loginDto){
        UserEntity findUser = userRepository.findByNickname(loginDto.getNickname());
        if (findUser == null) throw new UserNotFoundException();
        // 비밀번호 검증
        boolean passwordCheck = passwordEncoder.matches(loginDto.getPassword(), findUser.getPassword());
        if (!passwordCheck) throw new UserNotFoundException();

        String token = jwtTokenProvider.createToken(loginDto.getNickname(), loginDto.toEntity().getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(loginDto.getNickname());

        redisUtil.setDataExpire(loginDto.getNickname(), refreshToken, 360000);
        Map<String ,String> map = new HashMap<>();
        map.put("nickname", loginDto.getNickname());
        map.put("accessToken", token); // accessToken 반환
        map.put("refreshToken", refreshToken); // refreshToken 반환

        return map;
    }
    public String signup(UserDto userDto) {
        if(!userRepository.existsByNickname(userDto.getNickname())){
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(userDto.toEntity());

            return jwtTokenProvider.createToken(userDto.getNickname(), userDto.toEntity().getRoles());
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }


    }
}
