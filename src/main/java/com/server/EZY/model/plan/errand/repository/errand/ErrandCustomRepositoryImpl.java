package com.server.EZY.model.plan.errand.repository.errand;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQueryFactory;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandResponseDto;
import com.server.EZY.model.plan.errand.dto.QErrandResponseDto_ErrandDetails;
import com.server.EZY.model.plan.errand.dto.QErrandResponseDto_ErrandPreview;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.server.EZY.model.plan.errand.QErrandEntity.errandEntity;
import static com.server.EZY.model.member.QMemberEntity.memberEntity;
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
                .where(errandEntity.planIdx.eq(errandIdx))
                .join(errandEntity.errandDetailEntity, errandDetailEntity)
                .fetchJoin()
                .fetchOne();
        return Optional.ofNullable(errand);
    }

    /**
     * 내 모든 심부름을 조회하는 쿼리 메소드.
     * CaseBuilder(sql case) 를 통해 나의 주체를 판별한다.
     *
     * @param myMemberEntity
     * @return List<ErrandResponseDto.ErrandPreview>
     * @author 전지환
     */
    @Override
    public List<ErrandResponseDto.ErrandPreview> findAllErrandsToList(MemberEntity myMemberEntity) {
        return queryFactory
                .select(new QErrandResponseDto_ErrandPreview(
                        errandEntity.planIdx,
                        new CaseBuilder()
                                .when(errandEntity.errandDetailEntity.senderIdx.eq(myMemberEntity.getMemberIdx()))
                                .then("부탁한 심부름")
                                .otherwise("받은 심부름"),
                        errandEntity.planInfo.title,
                        errandEntity.period
                ))
                .from(errandEntity)
                .where(errandEntity.memberEntity.eq(myMemberEntity))
                .fetch();
    }

    /**
     * 해당 심부름의 상세 정보를 가져온다.
     *
     * @param errandIdx
     * @return ErrandResponseDto.ErrandDetails
     * @author 전지환
     */
    @Override
    public ErrandResponseDto.ErrandDetails findErrandDetails(MemberEntity myMemberEntity, Long errandIdx) {
        return queryFactory
                .select(new QErrandResponseDto_ErrandDetails(
                errandEntity.planIdx,
                errandEntity.planInfo,
                errandEntity.period,
                ExpressionUtils.as(
                        JPAExpressions.select(memberEntity.username)
                        .from(memberEntity)
                        .where(memberEntity.memberIdx.eq(errandDetailEntity.senderIdx)),
                        "sender"
                ),
                ExpressionUtils.as(
                        JPAExpressions.select(memberEntity.username)
                        .from(memberEntity)
                        .where(memberEntity.memberIdx.eq(errandDetailEntity.recipientIdx)),
                        "recipient"
                ),
                errandDetailEntity.errandStatus.stringValue()
                ))
                .from(errandEntity)
                .join(errandEntity.errandDetailEntity, errandDetailEntity)
                .where(
                        errandEntity.planIdx.eq(errandIdx)
                                .and(errandDetailEntity.senderIdx.eq(myMemberEntity.getMemberIdx())
                                        .or(errandDetailEntity.recipientIdx.eq(myMemberEntity.getMemberIdx())))
                )
                .fetchOne();
    }
}
