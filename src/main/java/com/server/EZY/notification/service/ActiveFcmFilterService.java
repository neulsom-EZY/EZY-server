package com.server.EZY.notification.service;

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
    public void checkFcmPurpose(FcmSourceDto fcmSourceDto){
        log.info("===========FCM 사용 목적이 {}로 인식 됐습니다.", fcmSourceDto.getFcmPurposeType());
        switch (fcmSourceDto.getFcmPurposeType()){
            case ERRAND: activeErrandFcm(fcmSourceDto);
                break;
            case PERSONALPLAN:
                break;
        }
    }

    public void activeErrandFcm(FcmSourceDto fcmSourceDto){
        switch (fcmSourceDto.getFcmRole()){
            case SENDER: fcmMakerService.errandSendFcm(fcmSourceDto);
        }
    }
}
