package com.server.EZY.model.user.controller.jwt;

import com.server.EZY.model.user.service.jwt.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @GetMapping("/refreshtoken")
    public Map<String, String> refresh(HttpServletRequest request) {
        return refreshTokenService.getRefreshToken(request);
    }
}
