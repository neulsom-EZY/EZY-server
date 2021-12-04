package com.server.EZY.security;

import com.server.EZY.security.exception_hendler.FilterExceptionHandlerFilter;
import com.server.EZY.security.exception_hendler.FilterExceptionHandlerFilterConfig;
import com.server.EZY.security.handler.CustomAccessDeniedHandler;
import com.server.EZY.security.handler.CustomAuthenticationEntryPointHandler;
import com.server.EZY.security.jwt.JwtTokenFilterConfigurer;
import com.server.EZY.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    private final FilterExceptionHandlerFilter filterExceptionHandlerFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable CSRF (cross site request forgery)
        http
                .cors().and()
                .csrf().disable() // rest api이므로 기본설정 사용안함, 기본 설정은 비인증시 로그인 폼으로 리다이렉트
                .httpBasic().disable(); //rest api, csrf보안이 필요없다.

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Entry points
        http.authorizeRequests()
                .antMatchers("/v1/member/verified/username").permitAll()
                .antMatchers("/v1/member/verified/phone").permitAll()
                .antMatchers("/v1/member/signup").permitAll()
                .antMatchers("/v1/member/signin").permitAll()
                .antMatchers("/v1/member/find/username").permitAll()
                .antMatchers("/v1/member/auth").permitAll()
                .antMatchers("/v1/member/verified/auth").permitAll()
                .antMatchers("/v1/member/send/change/password/authkey").permitAll()
                .antMatchers("/v1/member/change/password").permitAll()

                .antMatchers("/v1/plan/**").authenticated()
                .antMatchers("/v1/errand/**").authenticated() //개발 편의상 permitAll 처리 해 두었음
                .antMatchers("/v1/tag/**").authenticated() //개발 편의상 permitAll 처리 해 두었음

                .antMatchers("/exception/**").permitAll()
                .antMatchers("/h2-console/**/**").permitAll()
                .antMatchers("/exception/**").permitAll()

                /* 이렇게 권한에 따라 url접속을 제한할 수 있다. (테스트 완료)
                .antMatchers("/v1/member/test").hasRole("CLIENT")
                .antMatchers("/v1/admin/**").hasRole("ADMIN")
                */

                // Disallow everything else..
                .anyRequest().authenticated();

        // If a user try to access a resource without having enough permissions
        http.exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPointHandler());

        // Apply JWT
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
        http.apply(new FilterExceptionHandlerFilterConfig(filterExceptionHandlerFilter));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Allow swagger to be accessed without authentication
        web.ignoring().antMatchers("/v2/api-docs")//
                .antMatchers("/swagger-resources/**")//
                .antMatchers("/swagger-ui.html")//
                .antMatchers("/configuration/**")//
                .antMatchers("/webjars/**")//
                .antMatchers("/public")

                // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
                .antMatchers("/exception/**")
                .and()
                .ignoring()
                .antMatchers("/h2-console/**/**");;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

}