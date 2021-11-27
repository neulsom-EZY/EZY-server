package com.server.EZY.model.member.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import com.server.EZY.model.member.dto.QUsernameResponseDto;
import com.server.EZY.model.member.dto.UsernameResponseDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.server.EZY.model.member.QMemberEntity.memberEntity;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository{

    private final JPQLQueryFactory queryFactory;

    /**
     * 키워드를 포함하고 있는 username을 찾아주는 쿼리 메소드.
     *
     * @param keyword
     * @return List<UsernameResponseDto>
     * @author 전지환
     */
    @Override
    public List<UsernameResponseDto> searchUsernameKeywordBased(String keyword) {
        return queryFactory
                .select(new QUsernameResponseDto(
                    memberEntity.username
                ))
                .from(memberEntity)
                .where(memberEntity.username.contains(keyword))
                .fetch();
    }
}
