package com.server.EZY.util;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserUtil {

    private final MemberRepository memberRepository;

    /**
     * 현재 로그인 되어있는(인증되어있는) User의 nickname을 반환하는 메서드
     * @return nickname
     * @author 배태현
     */
    public static String getCurrentUsername() {
        String nickname = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            nickname = ((UserDetails) principal).getUsername();
        } else{
            nickname = principal.toString();
        }
        return nickname;
    }

    public MemberEntity getCurrentUser() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else{
            username = principal.toString();
        }
        return memberRepository.findByUsername(username);
    }
}
