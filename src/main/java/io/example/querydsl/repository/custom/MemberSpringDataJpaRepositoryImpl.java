package io.example.querydsl.repository.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.domain.dto.MemberTeamDto;
import io.example.querydsl.domain.dto.QMemberTeamDto;
import io.example.querydsl.domain.search.MemberSearchCondition;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static io.example.querydsl.domain.QMember.member;
import static io.example.querydsl.domain.QTeam.team;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author : choi-ys
 * @date : 2021/04/13 9:55 오전
 * @Content : Spring Data Jpa Repository에 상속할 사용자 정의 Repository Interface 구현체
 *  - 목적 : Spring Data Jpa와 QueryDSL 사용을 위한 사용자 정의 Repository 구현
 */
@RequiredArgsConstructor
public class MemberSpringDataJpaRepositoryImpl implements MemberQueryRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MemberTeamDto> searchMemberTeam(MemberSearchCondition memberSearchCondition) {
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
                        memberNameEQ(memberSearchCondition.getMemberName()),
                        teamNameEq(memberSearchCondition.getTeamName()),
                        memberAgeGoe(memberSearchCondition.getAgeGoe()),
                        memberAgeLoe(memberSearchCondition.getAgeLoe())
                ).fetch();
    }

    private BooleanExpression memberNameEQ(String memberName) {
        return hasText(memberName) ? member.name.eq(memberName) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? team.name.eq(teamName) : null;
    }


    private BooleanExpression memberAgeGoe(Integer ageGoe){
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression memberAgeLoe(Integer ageLoe){
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }
}