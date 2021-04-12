package io.example.querydsl.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author : choi-ys
 * @date : 2021/04/12 12:27 오후
 * @Content : 검색 기간
 */
@Getter
@AllArgsConstructor
public class PeriodSearchDto {
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}