package io.example.querydsl.ch13_dynamic_query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.config.BaseTest;
import io.example.querydsl.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static io.example.querydsl.domain.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : choi-ys
 * @date : 2021/04/12 10:51 오전
 * @Content :
 */
@DisplayName("QueryDsl의 BooleanBuilder를 이용한 동적 쿼리 생성")
public class DynamicQueryByBooleanBuilder extends BaseTest {

    @Test
    @DisplayName("BooleanBuilder with All Param")
//    @Rollback(value = false)
    public void booleanBuilderByAllParam(){
        // Given
        memberGenerator.savedMemberWithParam("최용석", 31);
        memberGenerator.savedMemberWithParam("이성욱", 31);

        jpaQueryFactory = new JPAQueryFactory(entityManager);
        String searchMemberName = "최용석";
        Integer searchMemberAge = 31;

        //When
        List<Member> searchMemberResult = searchMember(searchMemberName, searchMemberAge);

        assertThat(searchMemberResult)
                .extracting("name")
                .contains(searchMemberName);
    }

    @Test
    @DisplayName("BooleanBuilder with Null Param")
//    @Rollback(value = false)
    public void booleanBuilderWithNullParm(){
        // Given
        memberGenerator.savedMemberWithParam("최용석", 31);
        memberGenerator.savedMemberWithParam("이성욱", 31);

        jpaQueryFactory = new JPAQueryFactory(entityManager);
        String searchMemberName = null;
        Integer searchMemberAge = 31;

        //When
        List<Member> searchMemberResult = searchMember(searchMemberName, searchMemberAge);

        assertThat(searchMemberResult)
                .extracting("name")
                .contains("최용석", "이성욱");
    }

    private List<Member> searchMember(String searchMemberName, Integer searchMemberAge) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(searchMemberName != null){
            booleanBuilder.and(member.name.eq(searchMemberName));
        }

        if(searchMemberAge != null) {
            booleanBuilder.and(member.age.eq(searchMemberAge));
        }

        return jpaQueryFactory
                .select(member)
                .from(member)
                .where(booleanBuilder)
                .fetch();
    }
}