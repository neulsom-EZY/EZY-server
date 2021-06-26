package com.server.EZY.model.plan.errand;

import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;
import com.server.EZY.model.plan.errand.enumType.ResponseStatus;
import com.server.EZY.model.plan.errand.repository.ErrandRepository;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.enumType.Permission;
import com.server.EZY.model.user.enumType.Role;
import com.server.EZY.model.user.repository.UserRepository;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
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

    @Test @DisplayName("ErrandEntity sava 검증")
    void ErrandEntity_저장_검증(){
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
}