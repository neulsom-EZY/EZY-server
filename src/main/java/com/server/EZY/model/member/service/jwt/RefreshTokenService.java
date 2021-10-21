package com.server.EZY.model.member.service.jwt;

import java.util.Map;

/**
 * 토큰 재발급 서비스 로직 선언부
 *
 * @version 1.0.0
 * @author 배태현
 */
public interface RefreshTokenService {

    Map<String, String> getRefreshToken(String nickname, String refreshToken);
}
