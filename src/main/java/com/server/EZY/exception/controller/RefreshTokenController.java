package com.server.EZY.exception.controller;

import com.server.EZY.dto.TokenDto;
import com.server.EZY.exception.service.RefreshTokenService;
import com.server.EZY.model.user.Role;
import com.server.EZY.security.JwtTokenProvider;
import com.server.EZY.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @GetMapping("/refreshtoken")
    public String refresh(@Valid @RequestBody TokenDto tokenDto) {
        return refreshTokenService.getRefreshToken(tokenDto);
    }
}
