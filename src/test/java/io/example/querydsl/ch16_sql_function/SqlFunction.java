package io.example.querydsl.ch16_sql_function;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.config.BaseTest;
import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.QMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static io.example.querydsl.domain.QMember.member;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author : choi-ys
 * @date : 2021/04/12 5:27 오후
 * @Content : QueryDsl을 이용한 Sql Function 호출
 */
public class SqlFunction extends BaseTest {

    @Test
    @DisplayName("replace")
//    @Rollback(value = false)
    public void replaceFunction(){
        // Given
        Member savedMember = memberGenerator.savedMember();
        jpaQueryFactory = new JPAQueryFactory(entityManager);
        String replaceString = "용석";
        // When
        String result = jpaQueryFactory
                .select(Expressions.stringTemplate("function('replace', {0}, {1}, {2})",
                        member.name, "최용석", replaceString))
                .from(member)
                .where(member.no.eq(savedMember.getNo()))
                .fetchOne();
        // Then
        assertEquals(result, replaceString);
    }

    @Test
    @DisplayName("lower")
//    @Rollback(value = false)
    public void lowerFunction(){
        // Given
        Member savedMember = memberGenerator.savedMemberWithParam("CHOI-YONG-SEOK", 31);
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        String result = jpaQueryFactory
                .select(Expressions.stringTemplate("function('lower', {0})",
                        member.name))
                .from(member)
                .where(member.no.eq(savedMember.getNo()))
                .fetchOne();

        // Then
        assertEquals(result, savedMember.getName().toLowerCase());
    }

    @Test
    @DisplayName("query dsl lower")
//    @Rollback(value = false)
    public void queryDslLower(){
        // Given
        Member savedMember = memberGenerator.savedMemberWithParam("CHOI-YONG-SEOK", 31);
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        String result = jpaQueryFactory
                .select(member.name.lower())
                .from(member)
                .where(member.no.eq(savedMember.getNo()))
                .fetchOne();

        // Then
        assertEquals(result, savedMember.getName().toLowerCase());
    }
}
