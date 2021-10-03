package com.server.EZY.notification.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.notification.FcmMessage;
import com.server.EZY.notification.dto.FcmSourceDto;
import com.server.EZY.notification.enum_type.FcmActionSelector;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class FcmMakerService {
    private final FirebaseMessagingService firebaseMessagingService;
    private final MemberRepository memberRepository;

    public void errandSendFcm(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        log.info("==================정상적으로 심부름 fcm maker 메소드로 도착했습니다.===================");
        FcmMessage.FcmRequest request = FcmMessage.FcmRequest.builder()
                .title(
                        "누군가 " + fcmSourceDto.getFcmPurposeType() + "을 " + FcmActionSelector.ErrandAction.요청 + " 했어요!"
                )
                .body(
                        fcmSourceDto.getSender() + "님이 " + FcmActionSelector.ErrandAction.요청 + "한 심부름을 확인해보세요!"
                ).build();
        log.info("==================정상적으로 심부름 fcm이 만들어졌습니다.===================");
        firebaseMessagingService.sendToToken(request, findRecipientFcmToken(fcmSourceDto.getRecipient()));
        log.info("==================정상적으로 fcm 이 전송됐습니다.===================");
    }

    public String findRecipientFcmToken(String recipient){
        String recipientFcmToken = memberRepository.findByUsername(recipient).getFcmToken();
        log.info("==================정상적으로 받는사람의 fcmToken을 찾았습니다===================");
        return recipientFcmToken;
    }
}
