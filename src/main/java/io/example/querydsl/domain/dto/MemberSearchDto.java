package io.example.querydsl.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : choi-ys
 * @date : 2021/04/12 12:06 오후
 * @Content : 검색 기간 요청 객체
 */
@Getter
@AllArgsConstructor
public class MemberSearchDto {
    private SearchDateType searchDateType;
    private PeriodSearchDto periodSearchDto;
}
