package com.server.EZY.model.plan.errand.repository.errand;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQueryFactory;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.errand.ErrandDetailEntity;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandResponseDto;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.model.plan.errand.dto.QErrandResponseDto_Errands;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.server.EZY.model.plan.errand.QErrandEntity.errandEntity;
import static com.server.EZY.model.plan.QPlanEntity.planEntity;
import static com.server.EZY.model.plan.errand.QErrandDetailEntity.errandDetailEntity;

@RequiredArgsConstructor
public class ErrandCustomRepositoryImpl implements ErrandCustomRepository {

    private final JPQLQueryFactory queryFactory;

    /**
     * errandIdx로 ErrandEntity를 연관되어 있는 ErrandDetailEntity와 함께 가져온다.
     * @param errandIdx 심부름 Idx
     * @return ErrandStatus를 같이 조회한 ErrandEntity (null 허용)
     */
    @Override
    public Optional<ErrandEntity> findWithErrandStatusByErrandIdx(long errandIdx) {
        ErrandEntity errand = (ErrandEntity) queryFactory
                .from(errandEntity)
                .join(errandEntity.errandDetailEntity, errandDetailEntity)
                .fetchJoin()
                .fetchOne();
        return Optional.ofNullable(errand);
    }

    /**
     * 내 심부름 전체를 조회하는 메소드.
     * CaseBuilder(sql case) 를 통해 나의 주체를 판별한다.
     *
     * @param myMemberEntity
     * @return List<ErrandResponseDto.Errands> (nullable)
     * @author 전지환
     */
    @Override
    public Optional<List<ErrandResponseDto.Errands>> findAllErrandsToList(MemberEntity myMemberEntity) {
        List<ErrandResponseDto.Errands> errandsList = queryFactory.
                select(new QErrandResponseDto_Errands(
                        planEntity.planIdx,
                        new CaseBuilder()
                                .when(errandDetailEntity.senderIdx.eq(myMemberEntity.getMemberIdx()))
                                .then("부탁한 심부름")
                                .otherwise("부탁받은 심부름"),
                        planEntity.planInfo.title,
                        planEntity.period
                ))
                .from(errandEntity)
                .join(errandEntity._super, planEntity)
                .fetch();

        return Optional.ofNullable(errandsList);
    }
}
