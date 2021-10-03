package com.server.EZY.notification.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.notification.dto.FcmSourceDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @version 1.0.0
 * @since 1.0.0
 * @author 전지환
 */
@Service
@Slf4j
@AllArgsConstructor
public class ActiveFcmFilterService {

    private final FcmMakerService fcmMakerService;

    /**
     * fcmSource를 받아 목적에 맞는 fcm 생성 로직으로 redirect 합니다.
     * @param fcmSourceDto
     */
    public void checkFcmPurpose(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        log.info("===========FCM 사용 목적이 {}로 인식 됐습니다.", fcmSourceDto.getFcmPurposeType());
        switch (fcmSourceDto.getFcmPurposeType()){
            case 심부름: activeErrandFcm(fcmSourceDto);
                break;
            case 개인일정:
                break;
        }
    }

    public void activeErrandFcm(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        switch (fcmSourceDto.getFcmRole()){
            case 보내는사람: fcmMakerService.errandSendFcm(fcmSourceDto);
        }
    }
}
