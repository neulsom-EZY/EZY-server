package com.server.EZY.model.plan.tag.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.dto.TagSetDto;
import com.server.EZY.model.plan.tag.repository.TagRepository;
import com.server.EZY.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{

    private final CurrentUserUtil currentUserUtil;
    private final TagRepository tagRepository;

    @Override
    public TagEntity saveTag(TagSetDto tagSetDto) {
        MemberEntity currentUser = currentUserUtil.getCurrentUser();
        return tagRepository.save(tagSetDto.saveToEntity(currentUser));
    }
}
