package com.server.EZY.model.plan.errand.repository.errand;

import com.querydsl.jpa.JPQLQueryFactory;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandResponseDto;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.server.EZY.model.plan.errand.QErrandEntity.errandEntity;
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

    @Override
    public Optional<List<ErrandResponseDto.Errands>> findAllErrandsToList(String username) {
        return Optional.empty();
    }
}
