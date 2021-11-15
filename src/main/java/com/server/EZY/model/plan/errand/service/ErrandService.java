package com.server.EZY.model.plan.errand.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.exception.plan.exception.PlanNotFoundException;
import com.server.EZY.exception.response.CustomException;
import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandResponseDto;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;

import java.util.List;
import java.util.Optional;

public interface ErrandService {
    /**
     * 이 메서드는 심부름을 전송(저장) 할 때 사용하는 비즈니스 로직입니다.
     * @param errandSetDto
     * @return ErrandEntity
     * @author 전지환
     */
    ErrandEntity sendErrand(ErrandSetDto errandSetDto) throws Exception;

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
    ErrandEntity acceptErrand(long errandIdx) throws InvalidAccessException, CustomException, FirebaseMessagingException;

    /**
     * 심부름을 거절한다. <br>
     * 발신자의 Errand가 DB에 삭제되고, 심부름 거절 push알람을 발신자에게 전송한다.
     *
     * @param errandIdx 거절할 errandIdx(planIdx)
     * @throws FirebaseMessagingException push알람이 실패할 때
     * @author 정시원
     */
    void refuseErrand(long errandIdx) throws FirebaseMessagingException;

    /**
     * 심부름이 성공한다. <br>
     * 해당 심부름의 ErrandDetailEntity의 ErrandStauts가 COMPLETION 으로 변경되고, 수신자에게 성공 push알람이 전송된다.
     *
     * @param errandIdx 거절할 errandIdx(planIdx)
     * @author 정시원
     */
    void completionErrand(long errandIdx) throws FirebaseMessagingException;

    /**
     * 심부름이 실패한다. <br>
     * 해당 심부름의 ErrandDetailEntity의 ErrandStauts가 FAIL 으로 변경되고, 수신자에게 실패 push알람이 전송된다.
     *
     * @param errandIdx 거절할 errandIdx(planIdx)
     * @author 정시원
     */
    void failErrand(long errandIdx) throws FirebaseMessagingException;

    /**
     * 심부름을 수신자가 포기한다.
     *
     * @param errandIdx 포기할 심부름 Idx
     * @throws FirebaseMessagingException push알람이 실패할 때
     * @author 정시원
     */
    void giveUpErrand(long errandIdx) throws FirebaseMessagingException;

    /**
     * 나의 모든 심부름 내역을 확인하는 메소드.
     *
     * @author 전지환
     * @return List<ErrandResponseDto.Errands> (nullable)
     */
    Optional<List<ErrandResponseDto.Errands>> findAllMyErrands();
}
