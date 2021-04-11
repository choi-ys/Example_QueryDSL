package io.example.querydsl.ch12_query_dsl_query_projection_annotation;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.config.BaseTest;
import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.dto.MemberDto;
import io.example.querydsl.domain.dto.QMemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.example.querydsl.domain.QMember.member;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("QueryDsl의 @QueryProjection을 이용한 Dto Projection 결과 반환")
public class QueryProjection extends BaseTest {

    @Test
    @DisplayName("QueryProjection Annotation을 이용한 Dto Projection 반환")
    public void findMemberDtoByQueryProjectionAnnotation() {
        // Given
        Member savedMember = memberGenerator.savedMemberWithParam("최용석", 31);
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        MemberDto memberDto = jpaQueryFactory
                .select(new QMemberDto(member.no, member.name, member.age))
                .from(member)
                .where(member.no.eq(savedMember.getNo()))
                .fetchOne();

        // Then
        assertEquals(memberDto.getMemberNo(), savedMember.getNo());
        assertEquals(memberDto.getMemberName(), savedMember.getName());
        assertEquals(memberDto.getMemberAge(), savedMember.getAge());
    }
}
