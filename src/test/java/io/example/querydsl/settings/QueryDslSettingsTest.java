package io.example.querydsl.settings;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.ch01_hello_query_dsl.HelloQueryDsl;
import io.example.querydsl.chO1_hello_query_dsl.QHelloQueryDsl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@DisplayName("Settings:QueryDsl")
public class QueryDslSettingsTest {

    @Autowired
    EntityManager entityManager;

    /**
     * QueryDSL을 이용한 쿼리 생성 및 실행
     *  - 1. Gradle Task -> other -> compileQueryDsl 수행
     *    - @Entity로 명시된 객체를 이용하여 QType 객체 생성
     *  - 2. 쿼리를 생성하기 위한 JPAQueryFactory 생성
     *  - 3. 쿼리 대상인 QType 객체 생성
     *  - 4. 생성한 JPAQueryFactory와 QType 객체를 이용하여 쿼리 작성 및 실행
     *  - 5. 반환 결과 Entity Class 검증
     */
    @Test
    @DisplayName("hello queryDsl")
    public void helloQueryDsl () {
        // Given
        String name = "최용석";
        HelloQueryDsl helloQueryDsl = HelloQueryDsl.createHelloQueryDsl(name);
        entityManager.persist(helloQueryDsl);

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QHelloQueryDsl qHelloQueryDsl = new QHelloQueryDsl("hello");

        // When
        HelloQueryDsl queryResult = jpaQueryFactory
                .selectFrom(qHelloQueryDsl)
                .fetchOne();

        // Then
        assertEquals(queryResult, helloQueryDsl);
        assertEquals(queryResult.getId(), helloQueryDsl.getId());
        assertEquals(queryResult.getName(), helloQueryDsl.getName());
    }
}