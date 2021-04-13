package io.example.querydsl.repository.pagination;

import io.example.querydsl.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : choi-ys
 * @date : 2021/04/13 12:42 오후
 * @Content : QeuryDls을 이용한 Pagination
 */
public interface MemberSearchRepository extends JpaRepository<Member, Long>, MemberPaginationRepository {
}