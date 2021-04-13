package io.example.querydsl.ch06_join;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.config.BaseTest;
import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static io.example.querydsl.domain.QMember.member;
import static io.example.querydsl.domain.QTeam.team;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Query DSL Join 예제")
public class QueryDslJoin extends BaseTest {

    @PersistenceUnit
    EntityManagerFactory entityManagerFactory;

    @BeforeEach
    public void setUp(){
        Team coreDevTeam = teamGenerator.savedTeamWithTeamName("CoreDevTeam");
        Team airDevTeam = teamGenerator.savedTeamWithTeamName("AirDevTeam");
        Team devOpsTeam = teamGenerator.savedTeamWithTeamName("devOpsTeam");

        Member member1 = memberGenerator.savedMemberWithTeam("최용석", 31, coreDevTeam);
        Member member2 = memberGenerator.savedMemberWithTeam("이성욱", 31, coreDevTeam);
        Member member3 = memberGenerator.savedMemberWithTeam("박재현", 29, coreDevTeam);
        Member member4 = memberGenerator.savedMemberWithTeam("권성준", 31, airDevTeam);
        Member member5 = memberGenerator.savedMemberWithTeam("기호창", 30, airDevTeam);
        Member member6 = memberGenerator.savedMemberWithTeam("이하은", 29, devOpsTeam);
        Member member7 = memberGenerator.savedMemberWithTeam("전성원", 27, devOpsTeam);
    }

    /**
     * CoreDev팀에 소속된 모든 회원
     */
    @Test
    @DisplayName("inner join : fetchList")
    public void joinExample() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);
        String searchTeamName = "CoreDevTeam";

        // When
        List<Member> result = jpaQueryFactory
                .select(member)
                .from(member)
                .join(member.team, team)
                .where(member.team.name.eq(searchTeamName))
                .fetch();

        // Then
        assertThat(result)
                .extracting("name")
                .containsExactly("최용석", "이성욱", "박재현");
    }

    /**
     * left outer join
     *  - 예제 : 회원과 팀을 조인하면서, 팀이름이 teamA인 팀만 조인, 회원은 모두 조회
     *    - SQL : select member.name, team.name from member_tb as member left join team_tb as team on team.name = 'CoreDevTeam'
     *    - JPQL : select m.name, t.name from member as m left join team as t on m.team_no = t.team_no and t.name = 'CoreDevTeam'
     */
    @Test
    @DisplayName("left outer Join")
    public void leftOuterJoinExample() {
        // Given
        String searchTeamName = "CoreDevTeam";
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        List jpqlLeftOuterResult = entityManager.createQuery("select m, t from Member as m " +
                " left join m.team as t" +
                " on t.name = :teamName")
                .setParameter("teamName", searchTeamName)
                .getResultList();

        for (Object o : jpqlLeftOuterResult) {
            System.out.println(o.toString());
        }

        // When
        System.out.println("-------------------------------------------------------");
        jpqlLeftOuterResult = entityManager.createQuery("select m, t from Member as m " +
                " left join m.team as t" +
                " on m.team.no = t.no" +
                " and t.name = :teamName")
                .setParameter("teamName", searchTeamName)
                .getResultList();

        // Then
        for (Object o : jpqlLeftOuterResult) {
            System.out.println(o.toString());
        }

        // When
        System.out.println("-------------------------------------------------------");
        List<Tuple> queryDslLeftOuterResult = jpaQueryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq(searchTeamName))
                .fetch();

        for (Tuple tuple : queryDslLeftOuterResult) {
            System.out.println(tuple.toString());
        }
    }

    /**
     * QueryDSL의 innderJoin
     */
    @Test
    @DisplayName("inner join : fetchOne")
    public void innerJoinExample() {
        // Given
        entityManager.flush();
        entityManager.clear();
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        Member result = jpaQueryFactory
                .select(member)
                .from(member)
                .join(member.team, team)
                .where(member.name.eq("최용석"))
                .fetchOne();

        // Then
        boolean loaded = entityManagerFactory.getPersistenceUnitUtil().isLoaded(result.getTeam());
        assertThat(loaded).isFalse();
    }

    /**
     * QueryDSL의 FetchJoin
     */
    @Test
    @DisplayName("fetch join")
    public void fetchJoin() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        Member result = jpaQueryFactory
                .select(member)
                .from(member)
                .join(member.team, team).fetchJoin()
                .where(member.name.eq("최용석"))
                .fetchOne();

        // Then
        boolean loaded = entityManagerFactory.getPersistenceUnitUtil().isLoaded(result.getTeam());
        assertThat(loaded).isTrue();
    }
}