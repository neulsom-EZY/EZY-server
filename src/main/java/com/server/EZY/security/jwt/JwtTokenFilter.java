package com.server.EZY.security.jwt;

import com.server.EZY.exception.token.exception.AccessTokenExpiredException;
import com.server.EZY.exception.token.exception.AuthorizationHeaderIsEmpty;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/// 데이터베이스 호출을 수행하는 중이므로 OncePerRequestFilter을 사용하여 두 번 이상의 작업을 막는다.
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);

        if(token != null && jwtTokenProvider.validateToken(token)){
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

//        catch (ExpiredJwtException e){
//            throw new AccessTokenExpiredException();
//        } catch (MalformedJwtException e) {
//            throw new IllegalArgumentException("올바르지 않은 구조의 JWT형식입니다.");
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("적절하지 않은 인자값 입니다.");
//        } catch (SignatureException e) {
//            throw new IllegalArgumentException("JWT의 서명을 확인하지 못하였습니다.");
//        } catch (UnsupportedJwtException e) {
//            throw new IllegalArgumentException("Application 서버의 JWT형식과 일치하지 않습니다.");
//        } catch (PrematureJwtException e) {
//            throw new IllegalArgumentException("접근이 허용되기 전인 JWT입니다.");
//        }

        filterChain.doFilter(request, response);
    }
}
