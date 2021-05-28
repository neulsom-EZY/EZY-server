package com.server.EZY;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application.yml")
@ConfigurationProperties(prefix = "security.jwt")
@Getter
@Setter
public class JwtRead {
    private JwtSource token;
}
