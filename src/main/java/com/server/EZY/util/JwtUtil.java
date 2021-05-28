package com.server.EZY.util;

import org.springframework.beans.factory.annotation.Value;

public class JwtUtil {
    @Value("${security.jwt.token.secret-key}")
    private String key;
}
