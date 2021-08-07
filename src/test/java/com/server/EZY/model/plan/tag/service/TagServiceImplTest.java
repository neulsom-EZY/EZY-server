package com.server.EZY.model.plan.tag.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.enumType.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.dto.TagSetDto;
import com.server.EZY.model.plan.tag.embeddedTypes.Color;
import com.server.EZY.util.CurrentUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Commit
class TagServiceImplTest {

    @Autowired
    private TagService tagService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    MemberEntity savedMemberEntity;
    @BeforeEach
    @DisplayName("로그인 되어있는 유저를 확인하는 테스트")
    void GetUserEntity(){
        //Given
        MemberDto memberDto = MemberDto.builder()
                .username("배태현")
                .password("1234")
                .phoneNumber("01012341234")
                .build();

        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        savedMemberEntity = memberRepository.save(memberDto.toEntity());
        System.out.println("======== saved =========");

        // when login session 발급
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                memberDto.getUsername(),
                memberDto.getPassword(),
                List.of(new SimpleGrantedAuthority(Role.ROLE_CLIENT.name())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        System.out.println("=================================");
        System.out.println(context);

        //then
        String currentUserNickname = CurrentUserUtil.getCurrentUsername();
        assertEquals("배태현", currentUserNickname);
    }

    @Test @DisplayName("Tag 를 저장할 수 있나요?")
    void 태그저장(){
        //Given
        TagSetDto tagSetDto = TagSetDto.builder()
                .tag("지환이 태그")
                .color(Color.builder()
                        .red((short) 196)
                        .green((short) 200)
                        .blue((short) 255)
                        .build())
                .build();

        //When
        TagEntity saveTag = tagService.saveTag(tagSetDto);

        //Then
        assertEquals(tagSetDto.getTag(), saveTag.getTag());
    }
}