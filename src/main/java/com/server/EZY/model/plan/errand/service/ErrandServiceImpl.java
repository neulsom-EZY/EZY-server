package com.server.EZY.model.plan.errand.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.exception.plan.exception.PlanNotFoundException;
import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.exception.user.exception.MemberNotFoundException;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.enum_type.PlanType;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.ErrandDetailEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.model.plan.errand.enum_type.ErrandStatus;
import com.server.EZY.model.plan.errand.repository.errand.ErrandRepository;
import com.server.EZY.model.plan.errand.repository.errand_status.ErrandStatusRepository;
import com.server.EZY.notification.dto.FcmSourceDto;
import com.server.EZY.notification.enum_type.FcmPurposeType;
import com.server.EZY.notification.enum_type.FcmRole;
import com.server.EZY.notification.service.feature.FcmActiveSender;
import com.server.EZY.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

/**
 * @author 전지환
 * @version 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ErrandServiceImpl implements ErrandService{
    private final CurrentUserUtil currentUserUtil;
    private final MemberRepository memberRepository;
    private final ErrandRepository errandRepository;
    private final ErrandStatusRepository errandStatusRepository;
    private final FcmActiveSender fcmActiveSender;

    /**
     * 이 메서드는 심부름을 전송(저장) 할 때 사용하는 비즈니스 로직입니다.
     * @param errandSetDto
     * @return ErrandEntity
     * @author 전지환
     */
    @Override
    public ErrandEntity sendErrand(ErrandSetDto errandSetDto) throws Exception {
        /**
         * sender: 보내는 사람
         * recipient: 받는 사람
         */
        MemberEntity sender = currentUserUtil.getCurrentUser();
        MemberEntity recipient = memberRepository.findByUsername(errandSetDto.getRecipient());

        // 심부름 세부사항을 세팅한다.
        ErrandDetailEntity errandDetails = ErrandDetailEntity.builder()
                .senderIdx(sender.getMemberIdx())
                .recipientIdx(recipient.getMemberIdx())
                .errandStatus(ErrandStatus.NONE)
                .build();

        /**
         * savedErrandDetails: 심부름에 대한 상세정보를 저장한다.
         * savedErrandEntity: 심부름을 저장한다.
         */
        ErrandDetailEntity savedErrandDetails = errandStatusRepository.save(errandDetails);
        ErrandEntity savedErrandEntity = errandRepository.save(errandSetDto.saveToEntity(sender, savedErrandDetails));

        // 여기서 FCM 스펙을 정의 함.
        FcmSourceDto fcmSourceDto = FcmSourceDto.builder()
                .sender(sender.getUsername())
                .recipient(recipient.getUsername())
                .fcmPurposeType(FcmPurposeType.심부름)
                .fcmRole(FcmRole.보내는사람)
                .build();
        fcmActiveSender.sendRequestErrandFcmToRecipient(fcmSourceDto);

        return savedErrandEntity;
    }

    /**
     * 심부름을 수락한다. <br>
     * 수신자의 Errand가 DB에 저장되고, 심부름을 수락 push알람을 발신자에게 전송한다.
     *
     * @param errandIdx 수락할 errandIdx(planIdx)
     * @return 수신자의 ErrandEntity
     * @throws InvalidAccessException 해당 심부름에 잘못된 접근을 할 경우
     * @throws PlanNotFoundException  해당 심부름이 존재하지 않을 때
     * @throws FirebaseMessagingException push알람이 실패할 때
     * @author 정시원
     */
    @Override
    @Transactional
    public ErrandEntity acceptErrand(long errandIdx) throws FirebaseMessagingException {
        ErrandEntity senderErrandEntity = errandRepository.findWithErrandStatusByErrandIdx(errandIdx)
                .orElseThrow(
                        () -> new PlanNotFoundException(PlanType.심부름)
                );
        ErrandDetailEntity senderErrandDetailEntity = senderErrandEntity.getErrandDetailEntity();
        MemberEntity currentMember = currentUserUtil.getCurrentUser();

        senderErrandDetailEntity.updateErrandStatus(ErrandStatus.ACCEPT);
        checkRecipientByErrand(senderErrandDetailEntity, currentMember, InvalidAccessException::new);

        ErrandEntity recipientErrand = errandRepository.save(senderErrandEntity.cloneToMemberEntity(currentMember));

        FcmSourceDto fcmSourceDto = FcmSourceDto.builder()
                .sender(senderErrandEntity.getMemberEntity().getUsername())
                .recipient(recipientErrand.getMemberEntity().getUsername())
                .fcmPurposeType(FcmPurposeType.심부름)
                .fcmRole(FcmRole.받는사람)
                .build();
        fcmActiveSender.sendAcceptErrandFcmToSender(fcmSourceDto);
        return recipientErrand;
    }

    /**
     * 심부름을 거절한다. <br>
     * 발신자의 Errand가 DB에 삭제되고, 심부름 거절 push알람을 발신자에게 전송한다.
     *
     * @param errandIdx 거절할 errandIdx(planIdx)
     * @throws FirebaseMessagingException push알람이 실패할 때
     */
    @Override
    @Transactional
    public void refuseErrand(long errandIdx) throws FirebaseMessagingException {
        ErrandEntity senderErrandEntity = errandRepository.findWithErrandStatusByErrandIdx(errandIdx)
                .orElseThrow(
                        () -> new PlanNotFoundException(PlanType.심부름)
                );
        ErrandDetailEntity senderErrandDetailEntity = senderErrandEntity.getErrandDetailEntity();
        MemberEntity currentMember = currentUserUtil.getCurrentUser();

        checkRecipientByErrand(senderErrandDetailEntity, currentMember, InvalidAccessException::new);

        errandRepository.delete(senderErrandEntity);
        errandStatusRepository.delete(senderErrandDetailEntity);

        FcmSourceDto fcmSourceDto = FcmSourceDto.builder()
                .sender(senderErrandEntity.getMemberEntity().getUsername())
                .recipient(currentMember.getUsername())
                .fcmPurposeType(FcmPurposeType.심부름)
                .fcmRole(FcmRole.받는사람)
                .build();
        fcmActiveSender.sendCompletionErrandFcmToRecipient(fcmSourceDto);
    }

    /**
     * 심부름이 성공한다. <br>
     * 해당 심부름의 ErrandDetailEntity의 ErrandStauts가 COMPLETION 으로 변경되고, 수신자에게 성공 push알람이 전송된다.
     *
     * @param errandIdx 거절할 errandIdx(planIdx)
     * @author 정시원
     */
    @Override
    public void completionErrand(long errandIdx) throws FirebaseMessagingException {
        ErrandEntity errandEntity = errandRepository.findWithErrandStatusByErrandIdx(errandIdx)
                .orElseThrow(
                        () -> new PlanNotFoundException(PlanType.심부름)
                );
        ErrandDetailEntity errandDetailEntity = errandEntity.getErrandDetailEntity();
        MemberEntity sender = currentUserUtil.getCurrentUser();
        MemberEntity recipient = memberRepository.findById(errandDetailEntity.getRecipientIdx()).orElseThrow(MemberNotFoundException::new);

        checkSenderByErrand(errandDetailEntity, sender, InvalidAccessException::new);

        errandDetailEntity.updateErrandStatus(ErrandStatus.COMPLETION);

        FcmSourceDto fcmSourceDto = FcmSourceDto.builder()
                .sender(sender.getUsername())
                .recipient(recipient.getUsername())
                .fcmPurposeType(FcmPurposeType.심부름)
                .fcmRole(FcmRole.받는사람)
                .build();

        fcmActiveSender.sendCompletionErrandFcmToRecipient(fcmSourceDto);
    }

    /**
     * 이 심부름의 발신자가 아닌지 확인하고, Supplier로 넘겨준 Exception을 던진다.
     *
     * @param errandDetailEntity - 해당 심부름의 발신자의 정보를 가지고 있는 ErrandDetailEntity
     * @param memberEntity - 해당심부름의 발신자인지 검증할 MemberEntity
     * @param exceptionSupplier 해당 심부름의 발신자가 아닐경우 던질 exception supplier
     * @author 정시원
     */
    private void checkSenderByErrand(ErrandDetailEntity errandDetailEntity, MemberEntity memberEntity, Supplier<? extends RuntimeException> exceptionSupplier){
        if(!errandDetailEntity.getSenderIdx().equals(memberEntity.getMemberIdx()))
            throw exceptionSupplier.get();
    }

    /**
     * 이 심부름의 수신자가 아닌지 확인하고, Supplier로 넘겨준 Exception을 던진다.
     *
     * @param errandDetailEntity - 해당 심부름의 수신자의 정보를 가지고 있는 ErrandDetailEntity
     * @param memberEntity - 해당심부름의 수신자인지 검증할 MemberEntity
     * @param exceptionSupplier 해당 심부름의 수신자가 아닐경우 던질 exception supplier
     * @author 정시원
     */
    private void checkRecipientByErrand(ErrandDetailEntity errandDetailEntity, MemberEntity memberEntity, Supplier<? extends RuntimeException> exceptionSupplier){
        if(!errandDetailEntity.getRecipientIdx().equals(memberEntity.getMemberIdx()))
            throw exceptionSupplier.get();
    }
}
