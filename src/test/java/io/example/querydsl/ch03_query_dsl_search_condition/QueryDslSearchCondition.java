package io.example.querydsl.ch03_query_dsl_search_condition;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.domain.Member;
import io.example.querydsl.generator.MemberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;

import static io.example.querydsl.domain.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@DisplayName("QueryDSL의 다양한 Return Type")
@Import(MemberGenerator.class)
public class QueryDslSearchCondition {

    @Resource
    MemberGenerator memberGenerator;

    @Autowired
    EntityManager entityManager;
    JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    public void setUp(){
        String memberName = "최용석";
        for (int i = 0; i < 100; i++) {
            this.memberGenerator.savedMemberWithParam(memberName+i, (int)(Math.random()*26 + 10));
        }
    }

    /**
     * eq() : =
     * ne() : !=
     * eq().not() : !=
     */
    @Test
    @DisplayName("값 동일 비교")
    public void equalsCompareCondition() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);
        int searchAge = 30;

        // When : QueryDSL eq()
        List<Member> thirtyAgeMemberList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.eq(searchAge))
                .fetch();

        // Then : Check condition
        for (Member member : thirtyAgeMemberList) {
            assertEquals(member.getAge(), searchAge);
        }

        // When : QueryDSL ne()
        List<Member> ageIsNotThirtyMemberList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.ne(searchAge))
                .fetch();

        // Then : Check condition
        for (Member member : ageIsNotThirtyMemberList) {
            assertNotEquals(member.getAge(), searchAge);
        }

        // When : QueryDSL eq().not()
        List<Member> ageIsNotEqualsThirtyMemberList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.eq(searchAge).not())
                .fetch();

        // Then : Check condition
        for (Member member : ageIsNotThirtyMemberList) {
            assertNotEquals(member.getAge(), searchAge);
        }
    }
    
    /**
     * goe() : >=
     * gt() : >
     * loe : <=
     * lt : <
     */
    @Test
    @DisplayName("값 대소 비교")
    public void greaterThanLessThanCompareCondition() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);
        int searchAge = 30;

        // When : QueryDSL goe()
        List<Member> goeMemberList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.goe(searchAge))
                .fetch();

        // Then
        for (Member member : goeMemberList) {
            assertThat(member.getAge()).isGreaterThanOrEqualTo(searchAge);
        }

        // When
        List<Member> gtMemberList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.gt(searchAge))
                .fetch();

        // Then
        for (Member member : gtMemberList) {
            assertThat(member.getAge()).isGreaterThan(searchAge);
        }

        List<Member> loeMemberList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.loe(searchAge))
                .fetch();

        for (Member member : loeMemberList) {
            assertThat(member.getAge()).isLessThanOrEqualTo(searchAge);
        }

        List<Member> ltMemberList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.lt(searchAge))
                .fetch();

        for (Member member : ltMemberList) {
            assertThat(member.getAge()).isLessThan(searchAge);
        }
    }

    /**
     * isNull()
     * isNotNull() : is not null
     */
    @Test
    @DisplayName("값 Null 여부 비교")
    public void nullValueCompareCondition() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        long teamIsNullMemberCount = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.team.isNull())
                .fetchCount();

        // Then
        assertThat(teamIsNullMemberCount).isNotZero();

        long ageIsNotNullMemberCount = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.isNotNull())
                .fetchCount();

        assertThat(ageIsNotNullMemberCount).isNotZero();
    }

    /**
     * in() : in()
     * notIn() : not in()
     * between : ()
     */
    @Test
    @DisplayName("값 포함 여부 비교")
    public void includeValueCompareCondition() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);
        Integer[] multiCondition = new Integer[]{27, 31};

        // When
        long memberAgeInList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.in(multiCondition))
                .fetchCount();

        // Then
        long memberAgeNotInList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.notIn(multiCondition))
                .fetchCount();

        int startAge = 27;
        int endAge = 31;

        long memberAgeBetweenList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.between(startAge, endAge))
                .fetchCount();

        long memberAgeNotBetweenList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.notBetween(startAge, endAge))
                .fetchCount();
    }



    /**
     *  like() : like 'keyword'
     *  contains() : like '%keyword%'
     *  startWith : like 'keyword%'
     */
    @Test
    @DisplayName("키워드 포함 여부 비교")
    public void keywordSearchCondition() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        List<Member> memberNameLikeList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.name.like("최용석"))
                .fetch();

        List<Member> memberNameContainsList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.name.contains("최용석"))
                .fetch();

        List<Member> memberNameStartWithList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.name.startsWith("최용석"))
                .fetch();
        // Then
    }

    /**
     * and contidion
     */
    @Test
    @DisplayName("And 조건")
    public void andCondition() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);
        String searchName = "최용석";
        int searchAge = 30;

        // When
        List<Member> andConditionMemberList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(
                        member.name.startsWith(searchName)
                                .and(member.age.eq(searchAge))
                ).fetch();

        List<Member> commaConditionMemberList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(
                        member.name.startsWith(searchName),
                        member.age.eq(searchAge)
                ).fetch();
        // Then
    }
}