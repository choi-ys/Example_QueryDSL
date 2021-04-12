package io.example.querydsl.ch18_dynamic_query_with_query_projection_by_quert_dsl;

import io.example.querydsl.config.BaseTest;
import io.example.querydsl.domain.Team;
import io.example.querydsl.domain.dto.MemberTeamDto;
import io.example.querydsl.domain.search.MemberSearchCondition;
import io.example.querydsl.repository.DynamicQueryWithQueryProjectionByQueryDslRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : choi-ys
 * @date : 2021/04/12 8:15 오후
 * @Content : QueryDsl의 QueryProjection과 BooleanBuilder, Where Condition을 이용한 동적 쿼리 Test
 */
@DisplayName("QueryDsl의 QueryProjection과 BooleanBuilder, Where Condition을 이용한 동적 쿼리")
public class DynamicQueryWithQueryProjectionByQueryDsl extends BaseTest {

    @Autowired
    DynamicQueryWithQueryProjectionByQueryDslRepository dynamicQueryWithQueryProjectionByQueryDslRepository;

    @Test
    @DisplayName("QueryDsl의 BoeleanBuilder를 이용한 동적 쿼리 생성 및 QueryProjection을 이용한 DTO 조회")
//    @Rollback(value = false)
    public void DynamicQueryByBooleanBuilderWithQueryProjection(){
        // Given
        Team savedTeam = teamGenerator.savedTeam();
        memberGenerator.savedMemberWithTeam("최용석", 31, savedTeam);

        // When
        MemberSearchCondition memberSearchCondition = new MemberSearchCondition();
        memberSearchCondition.setMemberName("최용석");
        memberSearchCondition.setTeamName("CoreDevTeam");
        memberSearchCondition.setAgeGoe(20);
        memberSearchCondition.setAgeLoe(40);
        List<MemberTeamDto> memberTeamDtoList = dynamicQueryWithQueryProjectionByQueryDslRepository.findMemberTeamByBooleanBuilder(memberSearchCondition);

        // Then
        assertThat(memberTeamDtoList).extracting("memberName").containsExactly("최용석");
    }

    @Test
    @DisplayName("QueryDsl의 Where Condition를 이용한 동적 쿼리 생성 및 QueryProjection을 이용한 DTO 조회")
    //    @Rollback(value = false)
    public void DynamicQueryByWhereConditionWithQueryProjection(){
        // Given
        Team savedTeam = teamGenerator.savedTeam();
        memberGenerator.savedMemberWithTeam("최용석", 31, savedTeam);

        // When
        MemberSearchCondition memberSearchCondition = new MemberSearchCondition();
        memberSearchCondition.setMemberName("최용석");
        memberSearchCondition.setTeamName("CoreDevTeam");
        memberSearchCondition.setAgeGoe(20);
        memberSearchCondition.setAgeLoe(40);
        List<MemberTeamDto> memberTeamDtoList = dynamicQueryWithQueryProjectionByQueryDslRepository.findMemberTeamByWhereCondition(memberSearchCondition);

        // Then
        assertThat(memberTeamDtoList).extracting("memberName").containsExactly("최용석");
    }
}