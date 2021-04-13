package io.example.querydsl.ch04_sorting;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.config.BaseTest;
import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.example.querydsl.domain.QMember.member;

@DisplayName("QueryDSL의 정렬 처리")
public class QueryDslSortingCondition extends BaseTest {

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