package com.server.EZY.exception.token.controller;

import com.server.EZY.exception.token.exception.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class TokenExceptionController {

    @GetMapping("/access-token-expired")
    public void accessTokenExpiredException(){ throw new AccessTokenExpiredException(); }

    @GetMapping("/invalid-token")
    public void invalidTokenException(){ throw new InvalidTokenException(); }

    @GetMapping("/token-logged-out")
    public void tokenLoggedOutException(){ throw new TokenLoggedOutException(); }

    @GetMapping("/authorization-header-is-empty")
    public void authorizationHeaderIsEmpty(){ throw new AuthorizationHeaderIsEmpty(); }

    @GetMapping("/refresh-token-header-is-empty")
    public void refreshTokenHeaderIsEmpty(){ throw new RefreshTokenHeaderIsEmpty(); }
}
