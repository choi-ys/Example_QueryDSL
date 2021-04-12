package io.example.querydsl.ch14_synamic_query_by_where_condition;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.config.BaseTest;
import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.dto.MemberSearchDto;
import io.example.querydsl.domain.dto.PeriodSearchDto;
import io.example.querydsl.domain.dto.SearchDateType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static io.example.querydsl.domain.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : choi-ys
 * @date : 2021/04/12 11:48 오전
 * @Content : Where 조건을 이용한 동적 쿼리 생성 및 실행
 */
public class DynamicQueryByWhereCondition extends BaseTest {

    @Test
    @DisplayName("Where 조건을 이용한 동적 쿼리 생성 : 전체 검색 조건")
    @Rollback(value = false)
    public void dynamicQueryByWhereConditionWithAllParam(){
        // Given
        memberGenerator.savedMemberWithParam("최용석", 31);
        memberGenerator.savedMemberWithParam("이성욱", 31);
        memberGenerator.savedMemberWithParam("박재현", 29);

        String searchMemberName = "최용석";
        Integer searchMemberAge = 31;
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        List<Member> result = jpaQueryFactory
                .select(member)
                .from(member)
                .where(memberNameEq(searchMemberName), memberAgeEq(searchMemberAge))
                .fetch();

        // Then
        assertThat(result).extracting("name").contains("최용석");
    }

    @Test
    @DisplayName("Where 조건을 이용한 동적 쿼리 생성 : 일부 검색 조건")
    @Rollback(value = false)
    public void dynamicQueryByWhereConditionWithNullParam(){
        // Given
        memberGenerator.savedMemberWithParam("최용석", 31);
        memberGenerator.savedMemberWithParam("이성욱", 31);
        memberGenerator.savedMemberWithParam("박재현", 29);

        String searchMemberName = null;
        Integer searchMemberAge = 29;
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        List<Member> result = jpaQueryFactory
                .select(member)
                .from(member)
                .where(memberNameEq(searchMemberName), memberAgeEq(searchMemberAge))
                .fetch();

        // Then
        assertThat(result).extracting("name").contains("박재현");
    }

    private Predicate memberNameEq(String searchMemberName) {
        return searchMemberName == null ? null : member.name.eq(searchMemberName);
    }

    private Predicate memberAgeEq(Integer searchMemberAge) {
        return searchMemberAge == null ? null : member.age.eq(searchMemberAge);
    }

    @Test
    @DisplayName("Where 조건을 이용한 동적 쿼리 생성 : DTO 검색 조건")
    @Rollback(value = false)
    public void newTest(){
        // Given
        memberGenerator.savedMemberWithParam("최용석", 31);
        memberGenerator.savedMemberWithParam("이성욱", 31);
        memberGenerator.savedMemberWithParam("박재현", 29);
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        SearchDateType searchDataType = SearchDateType.UPDATE;
        LocalDateTime endAt = LocalDateTime.now();
        LocalDateTime startAt = endAt.minus(1, ChronoUnit.HOURS);
        PeriodSearchDto periodSearchDto = new PeriodSearchDto(startAt, endAt);
        MemberSearchDto memberSearchDto = new MemberSearchDto(searchDataType, periodSearchDto);

        // When
        List<Member> result = jpaQueryFactory
                .select(member)
                .from(member)
                .where(memberSearchDateBetween(memberSearchDto))
                .fetch();

        // Then
        for (Member member : result) {
            System.out.println(member.getNo() + " : " + member.getName() + " : " + member.getCreatedDate());
        }
    }

    private BooleanExpression memberSearchDateBetween(MemberSearchDto memberSearchDto){
        PeriodSearchDto periodSearchDto = memberSearchDto.getPeriodSearchDto();
        switch (memberSearchDto.getSearchDateType()){
            case CREATE:
                if(periodSearchDto.getStartAt() == null && periodSearchDto.getEndAt() == null){
                    return null;
                }
                return dateBetweenParts(member.createdDate, periodSearchDto);
            case UPDATE:
                if(periodSearchDto.getStartAt() == null && periodSearchDto.getEndAt() == null){
                    return null;
                }
                return dateBetweenParts(member.updatedDate, periodSearchDto);
            default:
                return null;
        }
    }

    private BooleanExpression dateBetweenParts(DateTimePath<LocalDateTime> dateRange, PeriodSearchDto periodSearchDto) {
        return dateRange.between(periodSearchDto.getStartAt(), periodSearchDto.getEndAt());
    }
}