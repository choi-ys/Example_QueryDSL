package io.example.querydsl.ch08_query_dsl_case;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.config.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.example.querydsl.domain.QMember.member;

public class QueryDslCase extends BaseTest {

    @BeforeEach
    public void setUp(){
        memberGenerator.savedMemberWithParam("권성준", 10);
        memberGenerator.savedMemberWithParam("최용석", 11);
        memberGenerator.savedMemberWithParam("이성욱", 20);
        memberGenerator.savedMemberWithParam("기호창", 21);
        memberGenerator.savedMemberWithParam("박재현", 30);
        memberGenerator.savedMemberWithParam("이하은", 31);
        memberGenerator.savedMemberWithParam("전성원", 40);
    }

    @Test
    @DisplayName("simple case")
    public void simpleCaseExample() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        List<Tuple> result = jpaQueryFactory
                .select(member.name,
                        member.age,
                        member.age
                                .when(10).then("10세")
                                .when(20).then("20세")
                                .when(30).then("30세")
                                .otherwise("기타")
                )
                .from(member)
                .fetch();

        // Then
        for (Tuple tuple : result) {
            System.out.println(tuple);
        }
    }

    @Test
    @DisplayName("complicated case")
    public void complicatedCaseExample() {
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        List<Tuple> result = jpaQueryFactory
                .select(member.name,
                        member.age,
                        new CaseBuilder()
                                .when(member.age.between(0, 10)).then("10대")
                                .when(member.age.between(11, 20)).then("20대")
                                .when(member.age.between(21, 30)).then("30대")
                                .otherwise("기타")
                )
                .from(member)
                .fetch();

        // Then
        for (Tuple tuple : result) {
            System.out.println(tuple);
        }
    }
}