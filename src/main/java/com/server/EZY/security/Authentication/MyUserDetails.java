package com.server.EZY.security.Authentication;

import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByNickname(nickname);

        if(nickname == null){
            throw new UsernameNotFoundException("nickName '" + nickname + "' not found");
        }

        return userEntity;
    }
}