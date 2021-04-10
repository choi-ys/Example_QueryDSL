package io.example.querydsl.ch04_query_dsl_sorting;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.Team;
import io.example.querydsl.generator.MemberGenerator;
import io.example.querydsl.generator.TeamGenerator;
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
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@DisplayName("QueryDSL의 정렬 처리")
@Import({MemberGenerator.class, TeamGenerator.class})
public class QueryDslSortingCondition {

    @Resource
    MemberGenerator memberGenerator;

    @Resource
    TeamGenerator teamGenerator;

    @Autowired
    EntityManager entityManager;
    JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    public void setUp(){
        Team savedTeam = teamGenerator.savedTeam();
        memberGenerator.savedMemberWithParam("최용석", 31);
        memberGenerator.savedMemberWithParam("이하은", 28);
        memberGenerator.savedMemberWithParam("이성욱", 31);
        memberGenerator.savedMemberWithParam("박재현", 29);
    }

    /**
     * 회원 정렬 예제
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 3. 단, 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */
    @Test
    @DisplayName("정렬")
    public void sortingCondition() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        List<Member> sortedMemberList = jpaQueryFactory
                .select(member)
                .from(member)
                .orderBy(
                        member.age.asc(),
                        member.name.asc()
                ).fetch();

        // Then
        for (Member member : sortedMemberList) {
            System.out.println(member.getNo() + " : " + member.getName() + ", " + member.getAge());
        }
    }
}