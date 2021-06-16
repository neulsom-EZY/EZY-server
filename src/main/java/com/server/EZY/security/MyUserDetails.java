package com.server.EZY.security;

import com.server.EZY.model.user.UserEntity;
import com.server.EZY.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByNickname(nickname);

        if(nickname == null){
            throw new UsernameNotFoundException("nickName '" + nickname + "' not found");
        }

        return userEntity;
    }
}