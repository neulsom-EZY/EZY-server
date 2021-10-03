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
        FcmMessage.FcmRequest request = FcmMessage.FcmRequest.builder()
                .title("누군가 " + fcmSourceDto.getFcmPurposeType() + "을 " + FcmActionSelector.ErrandAction.요청 + " 했어요!")
                .body(fcmSourceDto.getSender() + "님이 " + FcmActionSelector.ErrandAction.요청 + "한 심부름을 확인해보세요!")
                .build();

        firebaseMessagingService.sendToToken(request, findRecipientFcmToken(fcmSourceDto.getRecipient()));
    }

    public String findRecipientFcmToken(String recipient){
        return memberRepository.findByUsername(recipient).getFcmToken();
    }
}
