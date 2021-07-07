package com.server.EZY.model.plan.errand;

import com.server.EZY.model.plan.errand.dto.ErrandUpdateDto;
import com.server.EZY.model.plan.errand.enumType.ResponseStatus;
import com.server.EZY.model.plan.errand.repository.ErrandRepository;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.enumType.Permission;
import com.server.EZY.model.user.enumType.Role;
import com.server.EZY.model.user.repository.UserRepository;
import com.server.EZY.testConfig.QueryDslTestConfig;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Calendar;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslTestConfig.class)
class ErrandEntityTest {

    @Autowired UserRepository userRepo;
    @Autowired ErrandRepository errandRepo;

    // Test 편의를 위한 유저 생성 userEntityInit
    UserEntity userEntityInit(){
        UserEntity user = UserEntity.builder()
                .nickname(RandomString.make(10))
                .password(RandomString.make(10))
                .phoneNumber("010"+ (int)(Math.random()* Math.pow(10, 8)))
                .permission(Permission.PERMISSION)
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
        return userRepo.save(user);
    }

    // Test 편의를 위한 심부름 생성
    ErrandEntity errandEntityInit(UserEntity sender, UserEntity recipientUserEntity){
        ErrandEntity errandEntity = ErrandEntity.builder()
                .senderUserEntity(sender)
                .recipientIdx(recipientUserEntity.getUserIdx())
                .errandName(RandomString.make(10))
                .where(RandomString.make(20))
                .startAt(Calendar.getInstance())
                .endAt(Calendar.getInstance())
                .responseStatus(ResponseStatus.NOT_READ)
                .build();
        errandEntity.getEndAt().add(Calendar.DATE, 5);
        return errandEntity;
    }

    @Test @DisplayName("ErrandEntity 저장 조회 검증")
    void ErrandEntity_저장_조히_검증(){
        // Given
        UserEntity userA = userEntityInit();
        UserEntity userB = userEntityInit();

        // When
        final String ERRAND_NAME = "가랏 희철이!! 메이커스페이스!";
        final String WHERE = "광주하드웨어공고겸마이스터고등학교";
        final Calendar START_AT = Calendar.getInstance(); START_AT.set(2021, 4, 18);
        final Calendar END_AT = Calendar.getInstance(); END_AT.set(2021, 4, 22);
        final ResponseStatus RESPONSE_STATUS = ResponseStatus.NOT_READ;
        ErrandEntity errandEntity = ErrandEntity.builder()
                .senderUserEntity(userA)
                .recipientIdx(userB.getUserIdx())
                .errandName(ERRAND_NAME)
                .where(WHERE)
                .startAt(START_AT)
                .endAt(END_AT)
                .responseStatus(RESPONSE_STATUS)
                .build();
        ErrandEntity savedErrandEntity = errandRepo.save(errandEntity);

        // Then
        assertEquals(userA, savedErrandEntity.getSenderUserEntity());
        assertEquals(userB.getUserIdx(), savedErrandEntity.getRecipientIdx());
        assertEquals(ERRAND_NAME, savedErrandEntity.getErrandName());
        assertEquals(WHERE, savedErrandEntity.getWhere());
        assertEquals(0, savedErrandEntity.getStartAt().compareTo(START_AT));
        assertEquals(0, savedErrandEntity.getEndAt().compareTo(END_AT));
        assertEquals(RESPONSE_STATUS, savedErrandEntity.getResponseStatus());
    }

    @Test @DisplayName("ErrandEntity 수정 검증")
    void ErrandEntity_수정_검증(){
        // Given
        UserEntity userA = userEntityInit();
        UserEntity userB = userEntityInit();

        ErrandEntity beforeUpdateErrandEntity = errandRepo.saveAndFlush(errandEntityInit(userA, userB));

        // When
        final String ERRAND_NAME = "가랏 희철이!! 메이커스페이스!";
        final String WHERE = "광주하드웨어공고겸마이스터고등학교";
        final Calendar START_AT = Calendar.getInstance(); START_AT.set(2021, 4, 18);
        final Calendar END_AT = Calendar.getInstance(); END_AT.set(2021, 4, 22);
        final ResponseStatus RESPONSE_STATUS = ResponseStatus.READ;
        ErrandEntity updatedErrandEntity = ErrandUpdateDto.builder()
                .errandName(ERRAND_NAME)
                .where(WHERE)
                .startAt(START_AT)
                .endAt(END_AT)
                .responseStatus(RESPONSE_STATUS)
                .build()
                .toEntity();

        beforeUpdateErrandEntity.updateErrand(updatedErrandEntity);
        updatedErrandEntity = errandRepo.save(beforeUpdateErrandEntity);

        // Then
        assertEquals(ERRAND_NAME, updatedErrandEntity.getErrandName());
        assertEquals(WHERE, updatedErrandEntity.getWhere());
        assertEquals(0, updatedErrandEntity.getStartAt().compareTo(START_AT));
        assertEquals(0, updatedErrandEntity.getEndAt().compareTo(END_AT));
        assertEquals(RESPONSE_STATUS, updatedErrandEntity.getResponseStatus());
    }

    @Test
    void ErrandEntity_삭제_검증(){
        // Given
        UserEntity userA = userEntityInit();
        UserEntity userB = userEntityInit();

        ErrandEntity beforeUpdateErrandEntity = errandRepo.saveAndFlush(errandEntityInit(userA, userB));

        // When
        errandRepo.deleteById(beforeUpdateErrandEntity.getErrandIdx());

        // Than
        assertThrows(Exception.class,
                () -> errandRepo.getById(beforeUpdateErrandEntity.getErrandIdx())
        );
    }
}