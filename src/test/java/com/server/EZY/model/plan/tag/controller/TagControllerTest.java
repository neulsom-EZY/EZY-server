package com.server.EZY.model.plan.tag.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.enum_type.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.tag.dto.TagSetDto;
import com.server.EZY.model.plan.tag.embedded_type.Color;
import com.server.EZY.testConfig.AbstractControllerTest;
import com.server.EZY.util.CurrentUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest @Transactional @Slf4j
public class TagControllerTest extends AbstractControllerTest {

    @Autowired
    private TagController tagController;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ObjectMapper mapper;

    @Override
    protected Object controller() {
        return tagController;
    }

    MemberEntity savedMemberEntity;
    String testingFcmToken = "dBzseFuYD0dCv2-AoLOA_9:APA91bE2q3aMdjvA3CIEKouMujj4E7V_t6aKM6RFxmrCwKCDOXeB39wasAk2uEhcGo3OTU2hr2Ap4NLbKRnsaQfxeRJnF_IZ9ReOUXSCAFIuJB3q1fgfKado3al15yJQkebGU6JSfxSL";


    @BeforeEach @DisplayName("로그인 되어있는 유저를 확인하는 테스트")
    void GetUserEntity(){
        //Given
        MemberDto memberDto = MemberDto.builder()
                .username("@jyeonjyan")
                .password("1234")
                .phoneNumber("01012341234")
                .fcmToken(testingFcmToken)
                .build();

        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        savedMemberEntity = memberRepository.save(memberDto.toEntity());
        log.info("회원이 정상적으로 save 되었습니다.");

        // when login session 발급
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                memberDto.getUsername(),
                memberDto.getPassword(),
                List.of(new SimpleGrantedAuthority(Role.ROLE_CLIENT.name()))
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        log.info("=======context: {}==========", context);

        //then
        String currentUserNickname = CurrentUserUtil.getCurrentUsername();
        assertEquals("@jyeonjyan", currentUserNickname);
    }

    @Test @DisplayName("태그 저장 controller 테스트")
    void addTag() throws Exception {
        TagSetDto tagSetDto = TagSetDto.builder()
                .tag("지환이랑 놀기")
                .color(Color.builder()
                        .red((short) 123)
                        .green((short) 123)
                        .blue((short) 123)
                        .build())
                .build();

        mvc.perform(
                post("/v1/tag")
                .content(mapper.writeValueAsString(tagSetDto))
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test @DisplayName("태그 전체 조회 controller 테스트")
    void getAllTag() throws Exception {
        mvc.perform(
                get("/v1/tag")
        );
    }
}