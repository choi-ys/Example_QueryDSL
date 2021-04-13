package io.example.querydsl.ch10_default_projection;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.config.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.example.querydsl.domain.QMember.member;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("QueryDsl의 기본 Projection 결과 반환")
public class QueryDslDefaultProjection extends BaseTest {

    @BeforeEach
    public void setUp(){
        memberGenerator.savedMemberWithParam("최용석", 31);
    }

    @Test
    @DisplayName("primitive Type")
    public void primitiveType() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        String searchMemberName = "최용석";
        String selectedMemberName = jpaQueryFactory
                .select(member.name)
                .from(member)
                .where(member.name.eq(searchMemberName))
                .fetchOne();

        // Then
        assertEquals(selectedMemberName, searchMemberName);
    }

    @Test
    @DisplayName("tuple Type")
    public void tupleType() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        String searchMemberName = "최용석";
        int searchMemberAge = 31;
        List<Tuple> resultTuple = jpaQueryFactory
                .select(
                        member.name,
                        member.age
                )
                .from(member)
                .where(
                        member.name.eq(searchMemberName),
                        member.age.eq(searchMemberAge)
                )
                .fetch();

        // Then
        for (Tuple tuple : resultTuple) {
            assertEquals(tuple.get(member.name), searchMemberName);
            assertEquals(tuple.get(member.age), searchMemberAge);
        }
    }
}