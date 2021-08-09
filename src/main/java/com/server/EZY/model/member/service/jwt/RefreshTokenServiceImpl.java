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

    /**
     * accessToken에서 가져온 username과, refreshToken으로 새로운 accessToken과 refreshToken을 생성하는 메서드
     * @param nickname, refreshToken
     * @return Map<String, String> username, newAccessToken, newRefreshToken
     * @author 배태현
     */
    @Override
    public Map<String, String> getRefreshToken(String nickname, String refreshToken) {
        Map<String ,String> map = new HashMap<>();
        String newAccessToken = null;
        String newRefreshToken = null;

        MemberEntity findUser = memberRepository.findByUsername(nickname);
        List<Role> roles = findUser.getRoles();

        if (redisUtil.getData(nickname) == null) throw new TokenLoggedOutException();

        if (redisUtil.getData(nickname).equals(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
            redisUtil.deleteData(nickname);//refreshToken이 저장되어있는 레디스 초기화 후

            newAccessToken = jwtTokenProvider.createToken(nickname, roles);
            newRefreshToken = jwtTokenProvider.createRefreshToken();

            redisUtil.setDataExpire(nickname, newRefreshToken, 360000 * 1000l* 24 * 180); //새 refreshToken을 다시 저장

            map.put("username", nickname);
            map.put("NewAccessToken", "Bearer " + newAccessToken); // NewAccessToken 반환
            map.put("NewRefreshToken", "Bearer " + newRefreshToken); // NewRefreshToken 반환

            return map;
        } else {
            throw new MemberNotFoundException(); // token 재발급 실패 Exception
        }
    }
}
