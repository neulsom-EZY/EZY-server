package com.server.EZY.model.member.service.jwt;

import java.util.Map;


public interface RefreshTokenService {

    Map<String, String> getRefreshToken(String nickname, String refreshToken);
}
