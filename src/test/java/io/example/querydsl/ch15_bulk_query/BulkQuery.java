package io.example.querydsl.ch15_bulk_query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.config.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.example.querydsl.domain.QMember.member;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author : choi-ys
 * @date : 2021/04/12 4:13 오후
 * @Content : QueryDSL을 이용한 Bulk update/delete 쿼리
 */
@DisplayName("QueryDSL을 이용한 Bulk Update/Delete")
public class BulkQuery extends BaseTest {

    @BeforeEach
    public void setUp(){
        memberGenerator.savedMemberWithParam("최용석", 30);
        memberGenerator.savedMemberWithParam("이성욱", 31);
        memberGenerator.savedMemberWithParam("박재현", 32);
        memberGenerator.savedMemberWithParam("권성준", 33);
        memberGenerator.savedMemberWithParam("기호창", 34);
    }

    /**
     * 주의사항 :
     *   - JPQL 배치와 마찬가지로, 영속성 컨텍스트에 있는 엔티티와의 Dirty Checking을 진행하지 않고
     *     SQL이 DB에 직접 수행되기 때문에 배치 쿼리를 실행 후 영속성 컨텍스트를 초기화 하는 것이 안전하다.
     */
    @Test
    @DisplayName("bulk Update")
    public void bulkUpdate(){
        // Given
        jpaQueryFactory = new JPAQueryFactory(entityManager);

        // When
        long updatedCount = jpaQueryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();

        // When : bulk 연산 후 반드시 SQL 쓰기 지연 저장소에 저장된 쿼리 수행 후 영속성 컨텍스트를 초기화 하여
        //        DB와 영속성 컨텍스트에 저장된 Entity간의 데이터 차이로 인해 발생할 수 있는 오류를 방지
        entityManager.flush();
        entityManager.clear();

        // Then
        assertEquals(updatedCount, 5);
    }
}