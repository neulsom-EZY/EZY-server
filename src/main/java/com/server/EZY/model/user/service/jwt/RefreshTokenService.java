package com.server.EZY.model.user.service.jwt;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public interface RefreshTokenService {

    /**
     * accessToken과 refresh토큰을 이용하여 새로운 accessToken과 refreshToken을 생성하는 메서드
     * @param request HttpServletRequest
     * @return nickname, newAccessToken, newRefreshToken
     * @author 배태현
     */
    Map<String, String> getRefreshToken(HttpServletRequest request);
}
