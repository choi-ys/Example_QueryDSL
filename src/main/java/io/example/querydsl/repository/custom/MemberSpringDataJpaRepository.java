package io.example.querydsl.repository.custom;

import io.example.querydsl.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : choi-ys
 * @date : 2021/04/13 9:48 오전
 * @Content : Spring Data Jpa와 QueryDSL로 구성된 Repository
 *  - Spring Data Jpa를 이용한 Repository는 interface이므로, 개발자가 구현한 개발 코드는
 *  별도의 Repository로 작성하여 해당 Repository에 상속하여 의존관계를 분리한다.
 */
public interface MemberSpringDataJpaRepository extends JpaRepository<Member, Long>, MemberQueryRepository {
}