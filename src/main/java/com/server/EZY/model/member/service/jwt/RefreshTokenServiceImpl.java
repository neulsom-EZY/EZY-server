package com.server.EZY.model.member.service.jwt;

import com.server.EZY.exception.token.exception.InvalidTokenException;
import com.server.EZY.exception.token.exception.RefreshTokenHeaderIsEmpty;
import com.server.EZY.exception.token.exception.TokenLoggedOutException;
import com.server.EZY.exception.user.exception.MemberNotFoundException;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.enum_type.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.security.jwt.JwtTokenProvider;
import com.server.EZY.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    private long REDIS_EXPIRATION_TIME = JwtTokenProvider.REFRESH_TOKEN_VALIDATION_TIME; //6개월

    /**
     * accessToken에서 가져온 username과, refreshToken으로 새로운 accessToken과 refreshToken을 생성하는 메서드
     * @param nickname, refreshToken
     * @return Map<String, String> (username, newAccessToken, newRefreshToken)
     * @author 배태현
     */
    @Override
    public Map<String, String> getRefreshToken(String nickname, String refreshToken) {
        Optional.ofNullable(refreshToken).orElseThrow(RefreshTokenHeaderIsEmpty::new);

        Map<String ,String> map = new HashMap<>();
        String newAccessToken = null;
        String newRefreshToken = null;

        MemberEntity findUser = memberRepository.findByUsername(nickname);
        List<Role> roles = findUser.getRoles();

        String redisRefreshToken = Optional.ofNullable(redisUtil.getData(nickname)).orElseThrow(TokenLoggedOutException::new);

        if (redisRefreshToken.equals(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
            redisUtil.deleteData(nickname);//refreshToken이 저장되어있는 레디스 초기화 후

            newAccessToken = jwtTokenProvider.createToken(nickname, roles);
            newRefreshToken = jwtTokenProvider.createRefreshToken();

            redisUtil.setDataExpire(nickname, newRefreshToken, REDIS_EXPIRATION_TIME); //새 refreshToken을 다시 저장

            map.put("nickname", nickname);
            map.put("NewAccessToken", "Bearer " + newAccessToken); // NewAccessToken 반환
            map.put("NewRefreshToken", "Bearer " + newRefreshToken); // NewRefreshToken 반환

            return map;
        } else {
            throw new InvalidTokenException();
        }
    }
}
