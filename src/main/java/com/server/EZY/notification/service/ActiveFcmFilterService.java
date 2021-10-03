package com.server.EZY.notification.service;

import com.server.EZY.notification.FcmMessage;
import com.server.EZY.notification.dto.FcmSourceDto;
import com.server.EZY.notification.enum_type.FcmRole;
import org.springframework.stereotype.Service;

/**
 * @version 1.0.0
 * @since 1.0.0
 * @author 전지환
 */
@Service
public class ActiveFcmMessageService {
    /**
     * fcmSource를 받아 목적에 맞는 fcm 생성 로직으로 redirect 합니다.
     * @param fcmSourceDto
     */
    public void checkFcmPurpose(FcmSourceDto fcmSourceDto){
        switch (fcmSourceDto.getFcmPurposeType()){
            case ERRAND: activeErrandFcm(fcmSourceDto.getFcmRole()); break;
        }
    }

    public FcmMessage.FcmRequest activeErrandFcm(FcmRole fcmRole){
        return null;
    }
}
