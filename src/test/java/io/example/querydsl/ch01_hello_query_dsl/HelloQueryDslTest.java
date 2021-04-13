package io.example.querydsl.ch01_hello_query_dsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.QMember;
import io.example.querydsl.generator.MemberGenerator;
import io.example.querydsl.repository.MemberRepository;
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

/**
 * @author : choi-ys
 * @date : 2021-04-09 오후 6:18
 * @Content : QueryDSL을 이용한 조회 예제
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@DisplayName("[ch01]Hello Query DSL")
@Import(MemberGenerator.class)
class HelloQueryDslTest {

    @Resource
    MemberGenerator memberGenerator;

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("findMemberById:JPQL")
    public void findMemberByIdToJPQL() {
        // Given
        Member savedMember = memberGenerator.savedMember();

        entityManager.flush();
        entityManager.clear();

        // When
        Member selectedMember = (Member) entityManager.createQuery("select m from Member as m" +
                " where m.no = :memberNo")
                .setParameter("memberNo", savedMember.getNo())
                .getSingleResult();

        // Then
        assertEquals(selectedMember.getNo(), savedMember.getNo());
    }

    @Test
    @DisplayName("findMemberById:SpringDataJPA:QueryMethod")
    public void findMemberByIdToQueryMethod() {
        // Given
        Member savedMember = memberGenerator.savedMember();

        entityManager.flush();
        entityManager.clear();

        // When
        Member selectedMember = memberRepository.findMemberToQueryMethodByNo(savedMember.getNo());

        // Then
        assertEquals(selectedMember.getNo(), savedMember.getNo());
    }

    @Test
    @DisplayName("findMemberById:SpringDataJPA:QueryAnnotation")
    public void findMemberByIdToQueryAnnotation() {
        // Given
        Member savedMember = memberGenerator.savedMember();

        entityManager.flush();
        entityManager.clear();

        // When
        Member selectedMember = memberRepository.findMemberToQueryAnnotationByNo(savedMember.getNo());

        // Then
        assertEquals(selectedMember.getNo(), savedMember.getNo());
    }

    @Test
    @DisplayName("findMemberById:QueryDSL")
    public void findMemberByIdToQueryDSL() {
        // Given
        Member savedMember = memberGenerator.savedMember();

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QMember member = QMember.member;

        entityManager.flush();
        entityManager.clear();

        // When
        Member selectedMember = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.no.eq(savedMember.getNo()))
                .fetchOne();

        // Then
        assertEquals(selectedMember.getNo(), savedMember.getNo());
    }
}