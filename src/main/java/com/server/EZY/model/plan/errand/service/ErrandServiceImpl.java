package com.server.EZY.model.plan.errand.service;

import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrandServiceImpl implements ErrandService{
    @Override
    public ErrandEntity sendErrand(ErrandSetDto errandSetDto) {
        return null;
    }
}
