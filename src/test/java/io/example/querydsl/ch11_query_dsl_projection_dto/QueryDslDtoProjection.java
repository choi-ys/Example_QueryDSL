package io.example.querydsl.ch11_query_dsl_projection_dto;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.config.BaseTest;
import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.example.querydsl.domain.QMember.member;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("QueryDsl의 Dto Projection 결과 반환")
public class QueryDslDtoProjection extends BaseTest {

    @Test
    @DisplayName("순수 JPA의 DTO 조회")
    public void findMemberByDtoToJpql() {
        // Given
        Member savedMember = memberGenerator.savedMemberWithParam("최용석", 31);
        String searchMemberName = "최용석";
        entityManager.flush();
        entityManager.clear();

        // When
        Object searchResult = entityManager.createQuery("select new io.example.querydsl.domain.dto.MemberDto(m.no, m.name, m.age)" +
                " from Member as m" +
                " where m.name = :searchMemberName")
                .setParameter("searchMemberName", searchMemberName)
                .getSingleResult();

        // Then
        MemberDto memberDto = (MemberDto) searchResult;
        assertEquals(memberDto.getMemberNo(), savedMember.getNo());
        assertEquals(memberDto.getMemberName(), searchMemberName);
        assertEquals(memberDto.getMemberAge(), savedMember.getAge());
    }

    /**
     * QueryDsl의 조회 결과값을 *Dto 클래스에 선언된 Setter 메소드를 이용하여 주입
     * 주의사항 : 조회 결과를 반환받을 Dto 클래스는 반드시 Setter를 구현해야 한다.
     */
    @Test
    @DisplayName("Setter를 이용한 QueryDsl의 DTO 조회")
    public void findMemberDtoBySetter() {
        // Given
        Member savedMember = memberGenerator.savedMemberWithParam("최용석", 31);
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        MemberDto memberDto = jpaQueryFactory
                .select(Projections.bean(MemberDto.class,
                        member.no.as("memberNo"),
                        member.name.as("memberName"),
                        member.age.as("memberAge")
                ))
                .from(member)
                .where(member.no.eq(savedMember.getNo()))
                .fetchOne();

        // Then
        assertEquals(memberDto.getMemberNo(), savedMember.getNo());
        assertEquals(memberDto.getMemberName(), savedMember.getName());
        assertEquals(memberDto.getMemberAge(), savedMember.getAge());
    }

    /**
     * Public 기본 생성자 필수
     */
    @Test
    @DisplayName("Fields를 이용한 QueryDsl의 DTO 조회")
    public void findMemberDtoByFields() {
        // Given
        Member savedMember = memberGenerator.savedMemberWithParam("최용석", 31);
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        MemberDto memberDto = jpaQueryFactory
                .select(Projections.fields(MemberDto.class,
                        member.no.as("memberNo"),
                        member.name.as("memberName"),
                        member.age.as("memberAge")
                ))
                .from(member)
                .where(member.no.eq(savedMember.getNo()))
                .fetchOne();

        // Then
        assertEquals(memberDto.getMemberNo(), savedMember.getNo());
        assertEquals(memberDto.getMemberName(), savedMember.getName());
        assertEquals(memberDto.getMemberAge(), savedMember.getAge());
    }

    /**
     * 조회하는 각 필드를 인자로 하는 생성자 필수
     */
    @Test
    @DisplayName("Constructor를 이용한 QueryDsl의 DTO 조회")
    public void findMemberDtoByConstructor() {
        // Given
        Member savedMember = memberGenerator.savedMemberWithParam("최용석", 31);
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        MemberDto memberDto = jpaQueryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.no.as("memberNo"),
                        member.name.as("memberName"),
                        member.age.as("memberAge")
                ))
                .from(member)
                .where(member.no.eq(savedMember.getNo()))
                .fetchOne();

        // Then
        assertEquals(memberDto.getMemberNo(), savedMember.getNo());
        assertEquals(memberDto.getMemberName(), savedMember.getName());
        assertEquals(memberDto.getMemberAge(), savedMember.getAge());
    }
}