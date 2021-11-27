package com.server.EZY.model.member.repository;

import com.server.EZY.model.member.dto.UsernameResponseDto;

import java.util.List;

/**
 * member querydsl method
 *
 * @since 1.0.0
 * @author 전지환
 */
public interface MemberCustomRepository {
    /**
     * 키워드를 포함하고 있는 username을 찾아주는 쿼리 메소드.
     *
     * @param keyword
     * @return List<UsernameResponseDto>
     * @author 전지환
     */
    List<UsernameResponseDto> searchUsernameKeywordBased(String keyword);
}
