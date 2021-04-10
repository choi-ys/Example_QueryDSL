package io.example.querydsl.ch02_query_dsl_return_type;

import com.querydsl.core.QueryResults;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@DisplayName("QueryDSL의 다양한 Return Type")
@Import(MemberGenerator.class)
public class QueryDslReturnType {

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
     * fetchOne() : 단건 조회
     *  - 결과가 없는 경우 : null 반환
     *  - 결과가 둘 이상인 경우 : com.querydsl.core.NonUniqueResultException 발생
     */
    @Test
    @DisplayName("단건 조회")
    public void returnTypeOne() {
        // Given
        Member savedMember = memberGenerator.savedMember();

        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        Member selectedMember = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.no.eq(savedMember.getNo()))
                .fetchOne();

        // Then
        assertEquals(selectedMember.getNo(), savedMember.getNo());
    }

    /**
     * fetch() : 리스트 조회, 데이터 없는경우 빈 리스트 반환
     */
    @Test
    @DisplayName("리스트 조회")
    public void returnTypeList() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        List<Member> memberList = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.eq(30))
                .fetch();
    }

    /**
     * fetchFirst : 결과 목록의 첫 객체 조회
     *  - limit(1).fetchOne()와 같은 결과 반환
     */
    @Test
    @DisplayName("결과 목록의 첫 객체 조회")
    public void returnTypeFirst() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        Member selectedMember = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.eq(30))
                .fetchFirst();

        Member fetchFirst = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.eq(30))
                .limit(1)
                .fetchOne();
    }

    /**
     * fetchResults() : 페이징 정보 조회 및 total count 조회 쿼리 추가 실행
     *  - 페이징 처리를 하기 위한 total count 수행 되며, 해당 쿼리의 반환 결과를 기준으로 페이징 처리
     *  - 주의 사항
     *    - total count 쿼리는 pk를 기준으로 수행
     *    - 페이징 쿼리가 복잡한 경우 먼저 수행되는 total count의 쿼리가 성능상 문제가 있을 수 있으므로,
     *      fetchCount()를 이용하여 별도로 total count를 조회 한 후, 페이징 처리 해야 한다.
     */
    @Test
    @DisplayName("페이징 처리 조회")
    public void returnTypeResults() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        QueryResults<Member> memberQueryResults = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.eq(30))
                .offset(0)
                .limit(3)
                .fetchResults();

        // Then
        long total = memberQueryResults.getTotal();
        List<Member> results = memberQueryResults.getResults();
        long offset = memberQueryResults.getOffset();
        long limit = memberQueryResults.getLimit();

        System.out.println("total count : " + total);
        for (Member result : results) {
            System.out.println("total result : ["
                    + result.getNo() +"] -> "
                    + result.getName() + ", "
                    + result.getAge());
        }
        System.out.println("current offset : " + offset);
        System.out.println("current limit : " + limit );
    }

    /**
     * fetchCount() : pk를 기준으로 count 조회 쿼리 수행
     */
    @Test
    @DisplayName("카운트 조회")
    public void returnTypeCount() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        long totalCount = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.eq(30))
                .fetchCount();
    }
}