package io.example.querydsl.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.generator.MemberGenerator;
import io.example.querydsl.generator.TeamGenerator;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@Disabled
@Import({MemberGenerator.class, TeamGenerator.class})
public class BaseTest {

    @Resource
    protected MemberGenerator memberGenerator;

    @Resource
    protected TeamGenerator teamGenerator;

    @Autowired
    protected EntityManager entityManager;
    protected JPAQueryFactory jpaQueryFactory;
}