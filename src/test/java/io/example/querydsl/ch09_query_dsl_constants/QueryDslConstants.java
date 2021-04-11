package io.example.querydsl.ch09_query_dsl_constants;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.config.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.example.querydsl.domain.QMember.member;

public class QueryDslConstants extends BaseTest {

    @BeforeEach
    public void setUp(){
        memberGenerator.savedMember();
    }

    @Test
    @DisplayName("constant")
    public void constant() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        List<Tuple> result = jpaQueryFactory
                .select(member.name, Expressions.constant("A"))
                .from(member)
                .fetch();

        // Then
        for (Tuple tuple : result) {
            System.out.println(result);
        }
    }

    /**
     * stringValue() : 문자가 아닌 다른 타입인 경우 문자로 변환하기 위해 사용(ENUM 처리 시 유용)
     */
    @Test
    @DisplayName("concat")
    public void concat() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        List<String> fetch = jpaQueryFactory
                .select(member.name.concat("_").concat(member.age.stringValue()))
                .from(member)
                .fetch();

        // Then
        for (String string : fetch) {
            System.out.println(string);
        }
    }
}