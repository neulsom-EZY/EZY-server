package com.server.EZY.model.plan.errand.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.exception.response.CustomException;
import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;

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
     * @throws CustomException        PlanNotFound 해당 심부름이 존재하지 않을 때
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
}
