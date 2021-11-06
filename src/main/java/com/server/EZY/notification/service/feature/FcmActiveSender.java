package com.server.EZY.notification.service.feature;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.notification.FcmMessage;
import com.server.EZY.notification.dto.FcmSourceDto;
import com.server.EZY.notification.enum_type.FcmActionSelector;
import com.server.EZY.notification.service.FirebaseMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmActiveSender {
    private final FirebaseMessagingService firebaseMessagingService;
    private final FcmMakerService fcmMakerService;
    private final MemberRepository memberRepository;

    /**
     * 심부름 요청 정보를 수신자에게 알림을 보낸다.
     *
     * @param fcmSourceDto
     * @throws FirebaseMessagingException
     * @author 전지환
     */
    public void sendRequestErrandFcmToRecipient(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        FcmMessage.FcmRequest request =
                fcmMakerService.makeErrandFcmMessage(fcmSourceDto, fcmSourceDto.getSender(), FcmActionSelector.ErrandAction.요청);
        // 실제로 push를 전송하는 구간
        firebaseMessagingService.sendToToken(request, findFcmTokenByUsername(fcmSourceDto.getRecipient()));
    }

    /**
     * 심부름 수락 정보를 발신자에게 알림을 보낸다.
     *
     * @param fcmSourceDto push알람의 생성에 기본적인 정보를 가지고 있는 DTO
     * @throws FirebaseMessagingException 해당 push알람이 실패할 때
     * @author 정시원
     */
    public void sendAcceptErrandFcmToSender(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        FcmMessage.FcmRequest request =
                fcmMakerService.makeErrandFcmMessage(fcmSourceDto, fcmSourceDto.getRecipient(), FcmActionSelector.ErrandAction.승인);
        // 실제로 push를 전송하는 구간
        firebaseMessagingService.sendToToken(request, findFcmTokenByUsername(fcmSourceDto.getSender()));
    }

    /**
     * 심부름 거절 정보를 발신자에게 push알람을 보낸다.
     *
     * @param fcmSourceDto push알람의 생성에 기본적인 정보를 가지고 있는 DTO
     * @throws FirebaseMessagingException 해당 push알람이 실패할 때
     * @author 정시원
     */
    public void sendRefuseErrandFcmToSender(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        FcmMessage.FcmRequest request =
                fcmMakerService.makeErrandFcmMessage(fcmSourceDto, fcmSourceDto.getRecipient(), FcmActionSelector.ErrandAction.거절);

        firebaseMessagingService.sendToToken(request, findFcmTokenByUsername(fcmSourceDto.getSender()));
    }

    /**
     * 심부름 완료 정보를 수신자에게 push알람을 보낸다.
     *
     * @param fcmSourceDto 해당 push알람이 실패할 때
     * @throws FirebaseMessagingException 해당 push알람이 실패할 때
     */
    public void sendCompletionErrandFcmToRecipient(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        FcmMessage.FcmRequest request =
                fcmMakerService.makeErrandConfirmFcmMessageToRecipient(fcmSourceDto, FcmActionSelector.ErrandAction.완료);

        firebaseMessagingService.sendToToken(request, findFcmTokenByUsername(fcmSourceDto.getRecipient()));
    }

    /**
     * 심부름 실패 정보를 수신자에게 push알람을 보낸다.
     *
     * @param fcmSourceDto 해당 push알람이 실패할 때
     * @throws FirebaseMessagingException 해당 push알람이 실패할 때
     */
    public void sendFailErrandFcmToRecipient(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        FcmMessage.FcmRequest request =
                fcmMakerService.makeErrandConfirmFcmMessageToRecipient(fcmSourceDto, FcmActionSelector.ErrandAction.실패);

        firebaseMessagingService.sendToToken(request, findFcmTokenByUsername(fcmSourceDto.getRecipient()));
    }

    /**
     * 심부름 포기 정보를 수신자에게 push알람을 보낸다
     *
     * @param fcmSourceDto push알람의 생성에 기본적인 정보를 가지고 있는 DTO
     * @throws FirebaseMessagingException 해당 push알람이 실패할 때
     * @author 정시원
     */
    public void sendGiveUpErrandFcmToSender(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException{
        FcmMessage.FcmRequest request =
                fcmMakerService.makeErrandFcmMessage(fcmSourceDto, fcmSourceDto.getRecipient(), FcmActionSelector.ErrandAction.포기);
        firebaseMessagingService.sendToToken(request, findFcmTokenByUsername(fcmSourceDto.getSender()));
    }

    /**
     * target, fcm target token을 찾아준다.
     *
     * @param recipient
     * @return recipientFcmToken
     * @author 전지환
     */
    private String findFcmTokenByUsername(String recipient){
        return memberRepository.findByUsername(recipient).getFcmToken();
    }
}
