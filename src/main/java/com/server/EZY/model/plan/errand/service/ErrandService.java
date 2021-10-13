package com.server.EZY.model.plan.errand.service;

import com.server.EZY.exception.response.CustomException;
import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.ErrandStatusEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;

public interface ErrandService {
    ErrandEntity sendErrand(ErrandSetDto errandSetDto) throws Exception;

    /**
     * 심부름을 수락한다. <br>
     * 수신자의 ErrandStatus가 DB에 저장되고, 심부름을 수락했다는 push알람을 발신자에게 전송한다.
     * @param errandIdx 수락할 errandIdx(planIdx)
     * @return 수신자의 errandStatus
     * @throws InvalidAccessException 해당 심부름에 잘못된 접근을 할 경우
     * @throws CustomException PlanNotFound 해당 심부름이 존재하지 않을 때
     */
    ErrandStatusEntity acceptErrand(long errandIdx) throws InvalidAccessException, CustomException;
}
