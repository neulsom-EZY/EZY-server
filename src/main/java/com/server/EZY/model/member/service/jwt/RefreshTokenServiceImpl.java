package com.server.EZY.model.member.service.jwt;

import com.server.EZY.exception.token.exception.TokenLoggedOutException;
import com.server.EZY.exception.user.exception.MemberNotFoundException;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.enumType.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.security.jwt.JwtTokenProvider;
import com.server.EZY.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Override
    public Map<String, String> getRefreshToken(HttpServletRequest request) {

        //나중에 세부적으로 customException을 만들어 클라이언트에게 에러메세지 반환하도록 변경 (예를 들어 Token값들이 null일 때)
        String accessToken = jwtTokenProvider.resolveToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        Map<String ,String> map = new HashMap<>();
        String newAccessToken = null;
        String newRefreshToken = null;

        String username = jwtTokenProvider.getUsername(accessToken);

        MemberEntity findUser = memberRepository.findByUsername(username);
        List<Role> roles = findUser.getRoles();

        if (redisUtil.getData(username) == null) throw new TokenLoggedOutException();

        if (redisUtil.getData(username).equals(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
            redisUtil.deleteData(username);//refreshToken이 저장되어있는 레디스 초기화 후

            newAccessToken = jwtTokenProvider.createToken(username, roles);
            newRefreshToken = jwtTokenProvider.createRefreshToken();

            redisUtil.setDataExpire(username, newRefreshToken, 360000 * 1000l* 24 * 180); //새 refreshToken을 다시 저장

            map.put("username", username);
            map.put("NewAccessToken", "Bearer " + newAccessToken); // NewAccessToken 반환
            map.put("NewRefreshToken", "Bearer " + newRefreshToken); // NewRefreshToken 반환

            return map;
        } else {
            throw new MemberNotFoundException(); // token 재발급 실패 Exception
        }
    }
}
