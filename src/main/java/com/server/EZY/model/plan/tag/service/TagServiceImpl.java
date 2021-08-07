package com.server.EZY.model.plan.tag.service;

import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.dto.TagSetDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{

    @Override
    public TagEntity saveTag(TagSetDto tagSetDto) {
        return null;
    }
}
