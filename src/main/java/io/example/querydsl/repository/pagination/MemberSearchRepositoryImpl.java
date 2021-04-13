package io.example.querydsl.repository.pagination;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.dto.MemberTeamDto;
import io.example.querydsl.domain.dto.QMemberTeamDto;
import io.example.querydsl.domain.search.MemberSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static io.example.querydsl.domain.QMember.member;
import static io.example.querydsl.domain.QTeam.team;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author : choi-ys
 * @date : 2021/04/13 1:20 오후
 * @Content : QueryDSL을 이용한 Paging 처리 Repository 구현 부 (사용자 정의 Repository)
 */
@RequiredArgsConstructor
public class MemberSearchRepositoryImpl implements MemberPaginationRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition memberSearchCondition, Pageable pageable) {
        QueryResults<MemberTeamDto> memberTeamDtoQueryResults = jpaQueryFactory
                .select(new QMemberTeamDto(
                        member.no.as("memberId"),
                        member.name.as("memberName"),
                        member.age.as("memberAge"),
                        team.no.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .join(member.team, team)
                .where(
                        memberNameEq(memberSearchCondition.getMemberName()),
                        teamNameEq(memberSearchCondition.getTeamName()),
                        memberAgeGoe(memberSearchCondition.getAgeGoe()),
                        memberAgeLoe(memberSearchCondition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) // perPageNum
                .fetchResults();

        List<MemberTeamDto> content = memberTeamDtoQueryResults.getResults();
        long totalCount = memberTeamDtoQueryResults.getTotal();
        return new PageImpl<>(content, pageable, totalCount);
    }

    /**
     * 조건을 만족하는 데이터 수와 데이터를 별도로 조회
     * @param memberSearchCondition
     * @param pageable
     * @return
     */
    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition memberSearchCondition, Pageable pageable) {
        List<MemberTeamDto> content = jpaQueryFactory
                .select(new QMemberTeamDto(
                        member.no.as("memberId"),
                        member.name.as("memberName"),
                        member.age.as("memberAge"),
                        team.no.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .join(member.team, team)
                .where(
                        memberNameEq(memberSearchCondition.getMemberName()),
                        teamNameEq(memberSearchCondition.getTeamName()),
                        memberAgeGoe(memberSearchCondition.getAgeGoe()),
                        memberAgeLoe(memberSearchCondition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) // perPageNum
                .fetch();

        long totalCount = jpaQueryFactory
                .select(member)
                .from(member)
                .where(
                        memberNameEq(memberSearchCondition.getMemberName()),
                        teamNameEq(memberSearchCondition.getTeamName()),
                        memberAgeGoe(memberSearchCondition.getAgeGoe()),
                        memberAgeLoe(memberSearchCondition.getAgeLoe())
                )
                .fetchCount();
        return new PageImpl<>(content, pageable, totalCount);
    }

    /**
     * 분리한 카운트 쿼리 최적화
     *  - 카운트 쿼리가 생략 가능한 경우 생략해서 처리
     *    - 첫 페이지이면서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때
     *    - 마지막 페이지일때 (offset + 컨텐츠 사이즈를 더하여 전체 사이즈를 계산)
     * @param memberSearchCondition
     * @param pageable
     * @return
     */
    @Override
    public Page<MemberTeamDto> searchPageComplexCountQueryOptimization(MemberSearchCondition memberSearchCondition, Pageable pageable) {
        List<MemberTeamDto> content = jpaQueryFactory
                .select(new QMemberTeamDto(
                        member.no.as("memberId"),
                        member.name.as("memberName"),
                        member.age.as("memberAge"),
                        team.no.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .join(member.team, team)
                .where(
                        memberNameEq(memberSearchCondition.getMemberName()),
                        teamNameEq(memberSearchCondition.getTeamName()),
                        memberAgeGoe(memberSearchCondition.getAgeGoe()),
                        memberAgeLoe(memberSearchCondition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()) // perPageNum
                .fetch();

        JPAQuery<Member> countQuery = jpaQueryFactory
                .select(member)
                .from(member)
                .where(
                        memberNameEq(memberSearchCondition.getMemberName()),
                        teamNameEq(memberSearchCondition.getTeamName()),
                        memberAgeGoe(memberSearchCondition.getAgeGoe()),
                        memberAgeLoe(memberSearchCondition.getAgeLoe())
                );

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }

    @Override
    public Slice<MemberTeamDto> searchSliceSimple(MemberSearchCondition memberSearchCondition, Pageable pageable) {
        List<MemberTeamDto> content = jpaQueryFactory
                .select(new QMemberTeamDto(
                        member.no.as("memberId"),
                        member.name.as("memberName"),
                        member.age.as("memberAge"),
                        team.no.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .join(member.team, team)
                .where(
                        memberNameEq(memberSearchCondition.getMemberName()),
                        teamNameEq(memberSearchCondition.getTeamName()),
                        memberAgeGoe(memberSearchCondition.getAgeGoe()),
                        memberAgeLoe(memberSearchCondition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1) // perPageNum
                .fetch();

        boolean hasNext = false;

        if(content.size() == (pageable.getPageSize()+1)){
            hasNext = true;
            content.remove(pageable.getPageSize());
        };

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private Predicate memberNameEq(String memberName) {
        return hasText(memberName) ? member.name.eq(memberName) : null;
    }

    private Predicate teamNameEq(String teamName) {
        return hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private Predicate memberAgeGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private Predicate memberAgeLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }
}