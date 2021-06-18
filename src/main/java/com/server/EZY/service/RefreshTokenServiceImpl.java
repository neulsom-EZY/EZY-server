package com.server.EZY.service;

import com.server.EZY.dto.UserDto;
import com.server.EZY.model.user.Role;
import com.server.EZY.security.JwtTokenProvider;
import com.server.EZY.service.RefreshTokenService;
import com.server.EZY.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Override
    public Map<String, String> getRefreshToken(HttpServletRequest request) {
        UserDto userDto = new UserDto();
        List<Role> roles = userDto.toEntity().getRoles();

        String accessToken = jwtTokenProvider.resolveToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        Map<String ,String> map = new HashMap<>();
        String newAccessToken = null;
        String newRefreshToken = null;

        if (refreshToken != null && accessToken != null) {

            String nickname = jwtTokenProvider.getUsername(accessToken);

            if (redisUtil.getData(nickname).equals(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {

                newAccessToken = jwtTokenProvider.createToken(nickname, roles);
                newRefreshToken = jwtTokenProvider.createRefreshToken();
                map.put("nickname", nickname);
                map.put("NewAccessToken", "Bearer " + newAccessToken); // NewAccessToken 반환
                map.put("NewRefreshToken", "Bearer " + newRefreshToken); // NewRefreshToken 반환
                return map;
            }
        } else {
            String message = "다시 로그인하세요.";
            map.put("message", message);
            return map;
        }

        return map;
    }
}
