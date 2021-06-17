package com.server.EZY.exception.service;

import com.server.EZY.dto.TokenDto;
import com.server.EZY.security.JwtTokenProvider;
import com.server.EZY.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Override
    public String getRefreshToken(TokenDto tokenDto) {
//        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String newJwt = null;
        log.info("refreshToken = " + refreshToken);
        String nickname = jwtTokenProvider.getUsername(refreshToken);

//        List<Role> roles = jwtTokenProvider.getRoles(accessToken); //에러남 getRoles
//        log.info("String Role : " + roles);

        if (refreshToken.equals(redisUtil.getData(nickname)) && jwtTokenProvider.validateToken(refreshToken)) {
            newJwt = jwtTokenProvider.createRefreshToken(nickname);
            System.out.println("newJwt = " + newJwt);
        }
        return newJwt;
    }
}
