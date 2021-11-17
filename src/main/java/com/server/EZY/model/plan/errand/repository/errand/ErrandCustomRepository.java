package com.server.EZY.model.plan.errand.repository.errand;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandResponseDto;

import java.util.List;
import java.util.Optional;

/**
 * @version 1
 * @since 1
 * @author 전지환, 정시원
 */
public interface ErrandCustomRepository {

    /**
     * errandIdx로 ErrandEntity를 연관되어 있는 ErrandDetailEntity와 함께 가져온다.
     *
     * @param errandIdx 심부름 Idx
     * @return ErrandStatus를 같이 조회한 ErrandEntity (null 허용)
     */
    Optional<ErrandEntity> findWithErrandStatusByErrandIdx(long errandIdx);

    /**
     * 내 전체 심부름을 조회한다.
     *
     * @param myMemberEntity
     * @author 전지환
     */
    List<ErrandResponseDto.ErrandPreview> findAllErrandsToList(MemberEntity myMemberEntity);
}
