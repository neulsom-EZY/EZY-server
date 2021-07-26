package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanSetDto;
import com.server.EZY.model.plan.personal.repository.PersonalPlanRepository;
import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.repository.TagRepository;
import com.server.EZY.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalPlanServiceImpl implements PersonalPlanService{

    private final CurrentUserUtil userUtil;
    private final TagRepository tagRepository;
    private final PersonalPlanRepository personalPlanRepository;

    /**
     * personalPlan을 "생성"하기 위해 사용되는 비즈니스 로직입니다.
     * @param personalPlan
     * @return PersonalPlanEntity
     * @author 전지환
     */
    @Override
    public PersonalPlanEntity createPersonalPlan(PersonalPlanSetDto personalPlan) {
        MemberEntity currentUser = userUtil.getCurrentUser();
        TagEntity tagEntity = tagRepository.findByTagIdx(personalPlan.getTagIdx());
        // 저장요청
        return personalPlanRepository.save(personalPlan.saveToEntity(currentUser, tagEntity));
    }

    @Override
    public List<PersonalPlanEntity> getAllPersonalPlan() {
        return null;
    }
}
