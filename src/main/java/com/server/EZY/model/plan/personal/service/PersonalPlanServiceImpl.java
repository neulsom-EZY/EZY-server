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

@Service
@RequiredArgsConstructor
public class PersonalPlanServiceImpl implements PersonalPlanService{

    private final CurrentUserUtil userUtil;
    private final TagRepository tagRepository;
    private final PersonalPlanRepository personalPlanRepository;

    @Override
    public PersonalPlanEntity createPersonalPlan(PersonalPlanSetDto personalPlan) {
        /**
         * currentUser: 로그인된 memberEntity
         * tagEntity: 등록할 tag Entity
         */
        MemberEntity currentUser = userUtil.getCurrentUser();
        TagEntity tagEntity = tagRepository.findByTagIdx(personalPlan.getTag());
        // 저장요청
        return personalPlanRepository.save(personalPlan.saveToEntity(currentUser, tagEntity));
    }
}
