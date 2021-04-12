package io.example.querydsl.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.domain.dto.MemberTeamDto;
import io.example.querydsl.domain.dto.QMemberTeamDto;
import io.example.querydsl.domain.search.MemberSearchCondition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static io.example.querydsl.domain.QMember.member;
import static io.example.querydsl.domain.QTeam.team;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author : choi-ys
 * @date : 2021/04/12 8:41 오후
 * @Content : QueryDsl의 QueryProjection과 BooleanBuilder, Where Condition을 이용한 동적 쿼리
 *  - 주의 사항
 *    - 조회 조건인 {@link MemberSearchCondition}의 요청 값이 하나도 없는 경우,
 *      where문이 구성되지 않아 전체 데이터가 조회된다. 따라서 동적 쿼리 구성 시,
 *      기본 검색 조건을 포함하여 페이징 처리 하는 것을 권장
 */
@Repository
@Transactional(readOnly = true)
public class DynamicQueryWithQueryProjectionByQueryDslRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;

    public DynamicQueryWithQueryProjectionByQueryDslRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    public List<MemberTeamDto> findMemberTeamByBooleanBuilder(MemberSearchCondition memberSearchCondition){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(hasText(memberSearchCondition.getMemberName())){
            booleanBuilder.and(member.name.eq(memberSearchCondition.getMemberName()));
        }

        if(hasText(memberSearchCondition.getTeamName())){
            booleanBuilder.and(team.name.eq(memberSearchCondition.getTeamName()));
        }

        if(memberSearchCondition.getAgeGoe() != null){
            booleanBuilder.and(member.age.goe(memberSearchCondition.getAgeGoe()));
        }

        if(memberSearchCondition.getAgeLoe() != null){
            booleanBuilder.and(member.age.loe(memberSearchCondition.getAgeLoe()));
        }

        return jpaQueryFactory
                .select(new QMemberTeamDto(
                        member.no.as("memberId"),
                        member.name.as("memberName"),
                        member.age.as("memberAge"),
                        team.no.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .join(member.team, team)
                .where(booleanBuilder)
                .fetch();
    }

    public List<MemberTeamDto> findMemberTeamByWhereCondition(MemberSearchCondition memberSearchCondition){
        return jpaQueryFactory
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
                        ageGoe(memberSearchCondition.getAgeGoe()),
                        ageLoe(memberSearchCondition.getAgeLoe())
                )
                .fetch();
    }

    private BooleanExpression memberNameEq(String memberName) {
        return hasText(memberName) ? member.name.eq(memberName) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe!=null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }
}