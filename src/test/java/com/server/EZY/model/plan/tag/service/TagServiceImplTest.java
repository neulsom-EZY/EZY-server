package com.server.EZY.model.plan.tag.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.enum_type.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.dto.TagSetDto;
import com.server.EZY.model.plan.tag.embedded_type.Color;
import com.server.EZY.model.plan.tag.repository.TagRepository;
import com.server.EZY.util.CurrentUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class TagServiceImplTest {

    @Autowired
    private TagService tagService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public MemberEntity savedMemberEntity;
    String testingFcmToken = "dBzseFuYD0dCv2-AoLOA_9:APA91bE2q3aMdjvA3CIEKouMujj4E7V_t6aKM6RFxmrCwKCDOXeB39wasAk2uEhcGo3OTU2hr2Ap4NLbKRnsaQfxeRJnF_IZ9ReOUXSCAFIuJB3q1fgfKado3al15yJQkebGU6JSfxSL";

    @BeforeEach
    @DisplayName("로그인 되어있는 유저를 확인하는 테스트")
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
        System.out.println("======== saved =========");

        // when login session 발급
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                memberDto.getUsername(),
                memberDto.getPassword(),
                List.of(new SimpleGrantedAuthority(Role.ROLE_CLIENT.name()))
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        log.info("=================== context: " + context);

        //then
        String currentUserNickname = CurrentUserUtil.getCurrentUsername();
        assertEquals("@jyeonjyan", currentUserNickname);
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

    @Test @DisplayName("Tag 를 삭제할 수 있나요?")
    void 태그삭제() throws Exception {
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
        tagService.deleteTag(saveTag.getTagIdx());

        //Then
        assertTrue(tagRepository.findByTagIdx(saveTag.getTagIdx()) == null);

        //Exception-Then
        assertThrows(Exception.class, () -> tagService.deleteTag(saveTag.getTagIdx()));
    }
}