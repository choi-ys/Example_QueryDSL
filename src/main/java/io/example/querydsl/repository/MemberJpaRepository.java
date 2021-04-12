package io.example.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.example.querydsl.domain.Member;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static io.example.querydsl.domain.QMember.member;

/**
 * @author : choi-ys
 * @date : 2021/04/12 6:55 오후
 * @Content : 순수 Jpa와 QueryDsl을 이용한 Repository 구성
 */
@Repository
@Transactional(readOnly = true)
public class MemberJpaRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;

    public MemberJpaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Transactional
    public Member save(Member member){
        this.entityManager.persist(member);
        return member;
    }

    public Optional<Member> findById(long memberNo){
        return Optional.ofNullable(this.entityManager.find(Member.class, memberNo));
    }

    @Transactional
    public void remove(Member member){
        this.entityManager.remove(member);
    }

    public List<Member> findByMemberAge(int age){
        return this.entityManager.createQuery("select m from Member as m" +
                " where m.age = :age")
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByMemberAgeToQueryDsl(int age){
        return this.jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.age.eq(age))
                .fetch();
    }
}