package io.example.querydsl.domain.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : choi-ys
 * @date : 2021/04/12 7:54 오후
 * @Content : 조회 조건 요청 객체
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberSearchCondition {
    private String memberName;
    private String teamName;
    private Integer ageGoe;
    private Integer ageLoe;
}