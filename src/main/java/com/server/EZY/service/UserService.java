package com.server.EZY.service;

import com.server.EZY.dto.UserDto;
import com.server.EZY.repository.UserRepository;
import com.server.EZY.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;

    public String signin(){
        return null;
    }
    public String signup(UserDto userDto){
        if(!userRepository.existsByNickname(userDto.getNickname())){

        }
        return null;
    }
}
