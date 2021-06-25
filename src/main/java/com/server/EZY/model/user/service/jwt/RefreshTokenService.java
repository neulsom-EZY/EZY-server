package com.server.EZY.model.user.service.jwt;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public interface RefreshTokenService {

    Map<String, String> getRefreshToken(HttpServletRequest request);
}
