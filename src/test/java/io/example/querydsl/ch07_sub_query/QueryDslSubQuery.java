package io.example.querydsl.ch07_sub_query;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.config.BaseTest;
import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.QMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.example.querydsl.domain.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * JPA JPQL 서브쿼리의 한계점
 *  - 주의 사항 : from절의 서브쿼리(인라인 뷰)를 지원하지 않는다.
 *    즉, JPQL를 build하여 편리하게 사용하게 해주는 QueryDSL역시 이를 지원하지 않는다.
 *  - 해결
 *    - 서브쿼리를 join으로 변경(변경 불가능한 상황 존재)
 *    - application에서 쿼리를 분리하여 실행한다.
 *    - nativeSQL을 사용한다.
 */
public class QueryDslSubQuery extends BaseTest {

    @BeforeEach
    public void setUp(){
        memberGenerator.savedMemberWithParam("권성준", 40);
        memberGenerator.savedMemberWithParam("최용석", 35);
        memberGenerator.savedMemberWithParam("이성욱", 31);
        memberGenerator.savedMemberWithParam("기호창", 30);
        memberGenerator.savedMemberWithParam("박재현", 29);
        memberGenerator.savedMemberWithParam("이하은", 29);
        memberGenerator.savedMemberWithParam("전성원", 27);
    }

    @Test
    @DisplayName("sub query")
    public void subQueryExample() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);
        QMember subMember = new QMember("subMember");

        // When
        List<Member> result = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(subMember.age.max())
                                .from(subMember)
                ))
                .fetch();

        // Then
        assertThat(result)
                .extracting("age")
                .containsExactly(40);
    }

    @Test
    @DisplayName("sub query in")
    public void subQueryInExample() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);
        QMember subMember = new QMember("subMember");

        // When
        List<Member> result = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(subMember.age)
                                .from(subMember)
                                .where(subMember.age.gt(30))
                ))
                .fetch();

        // Then
        assertThat(result)
                .extracting("age")
                .containsExactly(40, 35, 31);
    }

    @Test
    @DisplayName("sub query projection")
    public void subQueryProjection() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);
        QMember subMember = new QMember("subMember");

        // When
        List<Tuple> fetch = jpaQueryFactory
                .select(
                        member.name,
                        JPAExpressions
                                .select(subMember.age.avg())
                                .from(subMember)
                )
                .from(member)
                .fetch();

        // Then
        for (Tuple tuple : fetch) {
            System.out.println(tuple);
        }
    }
}