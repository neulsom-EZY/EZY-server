package com.server.EZY.security.exception_hendler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@RequiredArgsConstructor
@Configuration
public class FilterExceptionHandlerFilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final FilterExceptionHandlerFilter filterExceptionHandlerFilter;

    /**
     * FilterExceptionHandlerFilter를 Security관련 Filter를 맨 처음으로 설정
     * JwtTokenFilter전에 추가하려 하면 NPE가 발생해 일단 맨앞에 등록
     * @param http
     * @author 정시원
     */
    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(filterExceptionHandlerFilter, SecurityContextPersistenceFilter.class); // Security Filter의 맨 앞에
    }
}
