package com.server.EZY.model.plan.errand.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.ErrandStatusEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.model.plan.errand.enum_type.ErrandResponseStatus;
import com.server.EZY.model.plan.errand.enum_type.ErrandRole;
import com.server.EZY.model.plan.errand.repository.ErrandRepository;
import com.server.EZY.notification.FcmMessage;
import com.server.EZY.notification.service.FirebaseMessagingService;
import com.server.EZY.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ErrandServiceImpl implements ErrandService{
    private final CurrentUserUtil currentUserUtil;
    private final MemberRepository memberRepository;
    private final ErrandRepository errandRepository;
    private final FirebaseMessagingService fcmService;

    /**
     * 이 메서드는 심부름을 전송(저장) 할 때 사용하는 비즈니스 로직입니다.
     * @param errandSetDto
     * @return ErrandEntity
     * @author 전지환
     */
    @Override
    public ErrandEntity sendErrand(ErrandSetDto errandSetDto) throws FirebaseMessagingException {
        /**
         * sender: 보내는 사람
         * recipientIdx: 받는 사람
         */
        MemberEntity sender = currentUserUtil.getCurrentUser();
        MemberEntity recipient = memberRepository.findByUsername(errandSetDto.getRecipient());

        ErrandStatusEntity errandStatusEntity = ErrandStatusEntity.builder()
                .senderIdx(sender.getMemberIdx())
                .recipientIdx(recipient.getMemberIdx())
                .errandResponseStatus(ErrandResponseStatus.NOT_READ)
                .build();

        ErrandEntity savedErrandEntity = errandRepository.save(errandSetDto.saveToEntity(sender, errandStatusEntity));

        fcmService.sendToToken(
                createFcmMessageAboutErrand(sender.getUsername(), recipient.getUsername(), ErrandRole.SENDER),
                "eQb5CygpsUahmPBRDnTc0N:APA91bFaOlt2nZDJKJpO8dZsjS8vSDCZKxZWYBWtNXYUiIiUxLPiGTLcXuyuVTW1uqOxu55Ay9z_1ss-D2uz2xP-C_R2-5yxyV2pqn88zYts4WSxS4pgWgdvFtBAG6nU__dSYH7WW8Qk"
        );

        return savedErrandEntity;
    }

    /**
     * 심부름 관련 FcmMessage를 생성할 때 사용하는 메서드 입니다.
     * @return FcmMessage.FcmRequest
     * @author 전지환
     */
    public FcmMessage.FcmRequest createFcmMessageAboutErrand(String sender, String recipient, ErrandRole errandRole){
        String action;
        log.info("당신은 "+errandRole.toString()+" 입니다.");

        switch (errandRole.toString()){
            case "SENDER" : action = "요청";
                break;
            case "RECIPIENT" : action = "전송";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + errandRole);
        }

        return FcmMessage.FcmRequest.builder()
                .title("누군가 심부름을 " +action+"했어요")
                .body(sender+" 님이 "+action+"한 심부름을 확인해보세요!")
                .build();
    }
}
