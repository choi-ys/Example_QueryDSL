package io.example.querydsl.chO1_hello_query_dsl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.QMember;
import io.example.querydsl.generator.MemberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@DisplayName("QType을 다루는 2가지 방법")
@Import(MemberGenerator.class)
public class DefaultQueryDslHandling {

    @Resource
    MemberGenerator memberGenerator;

    @Autowired
    EntityManager entityManager;

    // Thread Safe하므로 field level로 선언
    JPAQueryFactory jpaQueryFactory;

    /**
     * Qtype을 다루는 2가지 방법
     */
    @Test
    @DisplayName("QType 객체 생성을 통한 QueryDSL 실행")
    public void firstSolution() {
        // Given
        Member savedMember = memberGenerator.savedMember();

        jpaQueryFactory = new JPAQueryFactory(entityManager);
        QMember member = QMember.member;

        // When
        Member selectedMember = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.no.eq(savedMember.getNo()))
                .fetchOne();

        // Then
        assertEquals(selectedMember.getNo(), savedMember.getNo());
    }

    @Test
    @DisplayName("Static QType 객체 사용을 통한 QueryDSL 실행")
    public void secondSolution() {
        // Given
        Member savedMember = memberGenerator.savedMember();

        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        Member selectedMember = jpaQueryFactory
                .select(QMember.member)
                .from(QMember.member)
                .where(QMember.member.no.eq(savedMember.getNo()))
                .fetchOne();

        // Then
        assertEquals(selectedMember.getNo(), savedMember.getNo());
    }
}