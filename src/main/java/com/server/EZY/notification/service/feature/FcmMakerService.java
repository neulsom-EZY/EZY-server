package com.server.EZY.notification.service.feature;

import com.server.EZY.notification.FcmMessage;
import com.server.EZY.notification.dto.FcmSourceDto;
import com.server.EZY.notification.enum_type.FcmActionSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 전지환
 * @version 1.0.0
 */
@Slf4j
@Service
public class FcmMakerService {
    /**
     * push알람을 보내기 위헤 사용되는 {@link FcmMessage.FcmRequest}객체를 만든다.
     *
     * @param fcmSourceDto push알람의 생성에 기본적인 정보를 가지고 있는 DTO
     * @param senderOfPush 해당 push알람을 보내는 회원의 username
     * @param errandAction 심부름에서 하려는 기능
     * @return fcmPush알람의 메시지를 담고 있는 {@link FcmMessage.FcmRequest}
     * @author 정시원, 전지환
     */
    public FcmMessage.FcmRequest makeErrandFcmMessage(FcmSourceDto fcmSourceDto, String senderOfPush, FcmActionSelector.ErrandAction errandAction){
        return FcmMessage.FcmRequest.builder()
                .title("누군가 " + fcmSourceDto.getFcmPurposeType() + "을(를) " + errandAction + " 했어요!")
                .body(senderOfPush + "님이 " + errandAction+"한 "+fcmSourceDto.getFcmPurposeType()+"을 확인해보세요!")
                .build();
    }
}
