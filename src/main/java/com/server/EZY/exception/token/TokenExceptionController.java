package com.server.EZY.exception.token;

import com.server.EZY.exception.token.exception.AccessTokenExpiredException;
import com.server.EZY.exception.token.exception.InvalidTokenException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class TokenExceptionController {

    @GetMapping("/access-token-expired")
    public void accessTokenExpiredException(){
        throw new AccessTokenExpiredException();
    }

    @GetMapping("/invalid-token-exception")
    public void invalidTokenException(){
        throw new InvalidTokenException();
    }
}
