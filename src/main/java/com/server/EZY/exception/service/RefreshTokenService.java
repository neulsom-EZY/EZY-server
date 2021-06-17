package com.server.EZY.exception.service;

import com.server.EZY.dto.TokenDto;
import com.server.EZY.security.JwtTokenProvider;
import com.server.EZY.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


public interface RefreshTokenService {

    String getRefreshToken(TokenDto tokenDto);
}
