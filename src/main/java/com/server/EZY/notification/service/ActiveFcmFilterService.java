package com.server.EZY.notification.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.notification.dto.FcmSourceDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 전지환
 * @version 1.0.0
 */
@Service
@Slf4j
@AllArgsConstructor
public class ActiveFcmFilterService {
    private final FcmMakerService fcmMakerService;

    /**
     * 목적부터 탐색하여 ~ 최종적으로 전송하게 할 수 있도록 함
     * @param fcmSourceDto
     * @throws FirebaseMessagingException
     */
    public void send(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        checkFcmPurpose(fcmSourceDto);
    }

    /**
     * 자신의 목적에 맞는 메소드로 redirect
     * @param fcmSourceDto
     * @throws FirebaseMessagingException
     */
    public void checkFcmPurpose(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        log.info("===========목적이 {}로 인식 됐습니다.", fcmSourceDto.getFcmPurposeType());
        switch (fcmSourceDto.getFcmPurposeType()){
            case 심부름: activeErrandFcm(fcmSourceDto);
                break;
            case 개인일정:
                break;
        }
    }

    /**
     * 자신의 목적, 역할에 맞는 메소드로 redirect
     * @param fcmSourceDto
     * @throws FirebaseMessagingException
     */
    public void activeErrandFcm(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        log.info("===========역할이 {}로 인식 됐습니다.", fcmSourceDto.getFcmRole());
        switch (fcmSourceDto.getFcmRole()){
            case 보내는사람: fcmMakerService.errandSendFcm(fcmSourceDto);
        }
    }
}
