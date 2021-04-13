package io.example.querydsl.repository.pagination;

import io.example.querydsl.domain.dto.MemberTeamDto;
import io.example.querydsl.domain.search.MemberSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/**
 * @author : choi-ys
 * @date : 2021/04/13 1:20 오후
 * @Content : QueryDsl을 이용한 Pagination(page/slice) 정의 부
 */
public interface MemberPaginationRepository {
    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition memberSearchCondition, Pageable pageable);
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition memberSearchCondition, Pageable pageable);
    Page<MemberTeamDto> searchPageComplexCountQueryOptimization(MemberSearchCondition memberSearchCondition, Pageable pageable);

    Slice<MemberTeamDto> searchSliceSimple(MemberSearchCondition memberSearchCondition, Pageable pageable);
}