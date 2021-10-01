package com.server.EZY.model.plan.errand.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;

public interface ErrandService {
    ErrandEntity sendErrand(ErrandSetDto errandSetDto) throws FirebaseMessagingException;
}
