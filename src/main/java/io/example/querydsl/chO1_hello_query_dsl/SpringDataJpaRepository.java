package io.example.querydsl.chO1_hello_query_dsl;

import io.example.querydsl.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author : choi-ys
 * @date : 2021-04-09 오후 6:18
 * @Content : Spring Data JPA를 이용한 Team Entity와 DB연동 처리 Repository
 */
public interface SpringDataJpaRepository extends JpaRepository<Member, Long> {

    Member findMemberToQueryMethodByNo(long memberNo);

    @Query(value = "select m from Member as m" +
            " where m.no = :memberNo")
    Member findMemberToQueryAnnotationByNo(@Param("memberNo") long memberNo);
}
