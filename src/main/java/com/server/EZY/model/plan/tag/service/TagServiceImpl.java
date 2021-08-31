package com.server.EZY.model.plan.tag.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.dto.TagSetDto;
import com.server.EZY.model.plan.tag.repository.TagRepository;
import com.server.EZY.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<TagSetDto> getAllTag() {
        MemberEntity currentUser = currentUserUtil.getCurrentUser();
        return tagRepository.findTagEntitiesByMemberEntity(currentUser);
    }

    @Override
    public void deleteTag(Long tagIdx) throws Exception {
        MemberEntity currentUser = currentUserUtil.getCurrentUser();
        TagEntity tagEntity = tagRepository.findByTagIdx(tagIdx);

        if (currentUser == tagEntity.getMemberEntity()){
            tagRepository.delete(tagEntity);
        } else{
            throw new Exception("해당 태그를 삭제할 수 없습니다.");
        }
    }
}
