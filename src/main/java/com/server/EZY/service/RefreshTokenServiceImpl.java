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

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Override
    public Map<String, String> getRefreshToken(HttpServletRequest request) {
        UserDto userDto = new UserDto();
        List<Role> roles = userDto.toEntity().getRoles();

        //나중에 세부적으로 customException을 만들어 클라이언트에게 에러메세지 반환하도록 변경 (예를 들어 Token값들이 null일 때)
        String accessToken = jwtTokenProvider.resolveToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        Map<String ,String> map = new HashMap<>();
        String newAccessToken = null;
        String newRefreshToken = null;

        String nickname = jwtTokenProvider.getUsername(accessToken);

        if (redisUtil.getData(nickname).equals(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
            redisUtil.deleteData(nickname);//refreshToken이 저장되어있는 레디스 초기화 후
            newAccessToken = jwtTokenProvider.createToken(nickname, roles);
            newRefreshToken = jwtTokenProvider.createRefreshToken();
            redisUtil.setDataExpire(nickname, newRefreshToken, 360000 * 1000l* 24 * 180); //새 refreshToken을 다시 저장
            map.put("nickname", nickname);
            map.put("NewAccessToken", "Bearer " + newAccessToken); // NewAccessToken 반환
            map.put("NewRefreshToken", "Bearer " + newRefreshToken); // NewRefreshToken 반환
            return map;
        }
        return map;
    }
}
