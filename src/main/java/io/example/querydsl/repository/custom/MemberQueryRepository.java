package io.example.querydsl.repository.custom;

import io.example.querydsl.domain.dto.MemberTeamDto;
import io.example.querydsl.domain.search.MemberSearchCondition;

import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/04/13 9:55 오전
 * @Content : Spring Data Jpa Repository와 QueryDSL을 같이 사용하기 위한 사용자 정의 Repository
 *  - 1. 사용자 정의 Interface 작성 : {@link MemberQueryRepository}
 *  - 2. 사용자 정의 Interface 구현 : {@link MemberSpringDataJpaRepositoryImpl}
 *       - 명명규칙 : {상속할 Spring Data Repository} + Impl
 *  - 3. Spring Data Repository에 사용자 정의 Interface 상속
 */
public interface MemberQueryRepository {

    List<MemberTeamDto> searchMemberTeam(MemberSearchCondition memberSearchCondition);
}
