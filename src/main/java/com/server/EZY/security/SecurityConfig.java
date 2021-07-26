package com.server.EZY.security;

import com.server.EZY.model.member.enumType.Role;
import com.server.EZY.security.jwt.JwtTokenFilterConfigurer;
import com.server.EZY.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable CSRF (cross site request forgery)
        http.csrf().disable();

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Entry points
        http.authorizeRequests()//
                .antMatchers("/v1/member/signin").permitAll()//
                .antMatchers("/v1/member/signup").permitAll()//
                .antMatchers("/v1/member/refreshtoken").permitAll()//
                .antMatchers("/v1/member/find/username").permitAll()//
                .antMatchers("/v1/member/change/username").permitAll()//
                .antMatchers("/v1/member/change/password").permitAll()//
                .antMatchers("/v1/member/auth").permitAll()//
                .antMatchers("/v1/member/auth/check").permitAll()//
                .antMatchers("/v1/member/logout").authenticated() // 로그인 된 유저는 모두 접근을 허용함
                .antMatchers("/v1/member/delete").authenticated() // 로그인 된 유저는 모두 접근을 허용함
                /* 이렇게 권한에 따라 url접속을 제한할 수 있다. (테스트 완료)
                .antMatchers("/v1/member/test").hasRole("CLIENT")
                .antMatchers("/v1/admin/test").hasRole("ADMIN")
                */
                .antMatchers("/exception/**").permitAll()
                .antMatchers("/h2-console/**/**").permitAll()
                .antMatchers("/exception/**").permitAll()
                // Disallow everything else..
                .anyRequest().authenticated();

        // If a user try to access a resource without having enough permissions
        http.exceptionHandling().accessDeniedPage("/login");

        // Apply JWT
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

        // Optional, if you want to test the API from a browser
        // http.httpBasic();
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