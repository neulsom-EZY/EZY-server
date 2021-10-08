package com.server.EZY.model.member.controller.jwt;

import com.server.EZY.model.member.service.jwt.RefreshTokenService;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.SingleResult;
import com.server.EZY.security.jwt.JwtTokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * accessToken, refreshToken을 재발급 받는 controller
     * @param request
     * @return SingleResult (NewAccessToken, NewRefreshToken, nickname)
     */
    @GetMapping("/refreshtoken")
    @ApiOperation(value = "토큰 재발급", notes = "토큰 재발급")
    @ResponseStatus( HttpStatus.OK )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public SingleResult<Map<String, String>> refresh(HttpServletRequest request) {
        Map<String, String> map = refreshTokenService.getRefreshToken(request.getRemoteUser(), jwtTokenProvider.resolveRefreshToken(request));
        return responseService.getSingleResult(map);
    }
}
