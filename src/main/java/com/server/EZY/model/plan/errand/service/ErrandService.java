package com.server.EZY.model.plan.errand.service;

import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.model.plan.errand.enum_type.ErrandResponseStatus;
import com.server.EZY.model.plan.errand.enum_type.ErrandRole;
import com.server.EZY.notification.FcmMessage;

public interface ErrandService {
    ErrandEntity sendErrand(ErrandSetDto errandSetDto) throws Exception;
    FcmMessage.FcmRequest createFcmMessageAboutErrand(String sender, String recipient, ErrandRole errandRole, ErrandResponseStatus errandResponseStatus);
}
