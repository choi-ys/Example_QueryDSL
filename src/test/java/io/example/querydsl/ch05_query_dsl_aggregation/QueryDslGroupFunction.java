package io.example.querydsl.ch05_query_dsl_aggregation;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.config.BaseTest;
import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.example.querydsl.domain.QMember.member;
import static io.example.querydsl.domain.QTeam.team;

@DisplayName("Query DSL의 그룹 함수 예제")
public class QueryDslGroupFunction extends BaseTest {

    @BeforeEach
    public void setUp(){
        Team coreDevTeam = teamGenerator.savedTeamWithTeamName("CoreDevTeam");
        Team airDevTeam = teamGenerator.savedTeamWithTeamName("AirDevTeam");
        Team devOpsTeam = teamGenerator.savedTeamWithTeamName("devOpsTeam");

        Member member1 = memberGenerator.savedMemberWithTeam("최용석", 31, coreDevTeam);
        Member member2 = memberGenerator.savedMemberWithTeam("이성욱", 31, coreDevTeam);
        Member member3 = memberGenerator.savedMemberWithTeam("박재현", 29, coreDevTeam);
        Member member4 = memberGenerator.savedMemberWithTeam("권성준", 31, coreDevTeam);
        Member member5 = memberGenerator.savedMemberWithTeam("기호창", 30, coreDevTeam);
        Member member6 = memberGenerator.savedMemberWithTeam("이하은", 29, devOpsTeam);
        Member member7 = memberGenerator.savedMemberWithTeam("전성원", 27, devOpsTeam);
    }

    /**
     * Group 함수
     *  - count() : 집계함수
     *  - sum() : 합계함수
     *  - avg() : 평균함수
     *  - max() : 최대값
     *  - min() : 최소값
     */
    @Test
    @DisplayName("group 함수")
    public void groupFunctionExample() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When : Entity 타입으로 조회하는 경우가 아닌, 다양한 데이터타입을 조회하는 경우 Tuple로 반환(Tuple보단 DTO조회를 권장)
        List<Tuple> tupleList = jpaQueryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        // Then
        for (Tuple tuple : tupleList) {
            System.out.println("member.count() : " + tuple.get(member.count()));
            System.out.println("member.age.sum() : " + tuple.get(member.age.sum()));
            System.out.println("member.age.avg() : " + tuple.get(member.age.avg()));
            System.out.println("member.age.max() : " + tuple.get(member.age.max()));
            System.out.println("member.age.min() : " + tuple.get(member.age.min()));
        }
    }

    /**
     * groupBy 함수
     *  - 예제 : 각 팀의 평균연령 조회
     */
    @Test
    @DisplayName("groupBy 함수")
    public void groupByExample() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        List<Tuple> tupleList = jpaQueryFactory
                .select(
                        team.name,
                        member.age.avg()
                )
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        // Then
        for (Tuple tuple : tupleList) {
            System.out.println("team.name : " + tuple.get(team.name) + " | " + tuple.get(member.age.avg()));
        }
    }
}