package com.server.EZY.notification.service;

import com.server.EZY.notification.FcmMessage;
import com.server.EZY.notification.dto.FcmSourceDto;
import org.springframework.stereotype.Service;

@Service
public class FcmMakerService {
    public void errandSendFcm(FcmSourceDto fcmSourceDto){
        FcmMessage.FcmRequest.builder()
                .title("누군가 "+fcmSourceDto.getFcmRole())
    }
}
