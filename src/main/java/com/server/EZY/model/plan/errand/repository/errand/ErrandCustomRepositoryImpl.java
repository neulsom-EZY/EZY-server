package com.server.EZY.model.plan.errand.repository.errand;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQueryFactory;
import com.server.EZY.model.plan.errand.ErrandEntity;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.server.EZY.model.plan.errand.QErrandEntity.errandEntity;

@RequiredArgsConstructor
public class ErrandCustomRepositoryImpl implements ErrandCustomRepository {

    private final JPQLQueryFactory queryFactory;

    /**
     * errandIdx로 ErrandEntity를 연관되어 있는 ErrandStatusEntity와 함께 가져온다.
     * @param errandIdx 심부름 Idx
     * @return ErrandStatus를 같이 조회한 ErrandEntity (null 허용)
     */
    @Override
    public Optional<ErrandEntity> findWithErrandStatusByErrandIdx(long errandIdx) {
        Tuple errandEntityTuple = queryFactory
                .select(errandEntity, errandEntity.errandStatusEntity)
                .from(errandEntity)
                .where(errandEntity.planIdx.eq(errandIdx))
                .fetchOne();
        return Optional.ofNullable(errandEntityTuple.get(errandEntity));
    }
}
