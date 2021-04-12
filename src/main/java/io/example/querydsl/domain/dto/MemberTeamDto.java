package io.example.querydsl.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

/**
 * @author : choi-ys
 * @date : 2021/04/12 7:50 오후
 * @Content : 조회 조건의 응답 객체
 */
@Getter
public class MemberTeamDto {
    private Long memberId;
    private String memberName;
    private int memberAge;
    private Long teamId;
    private String teamName;

    @QueryProjection
    public MemberTeamDto(Long memberId, String memberName, int memberAge, Long teamId, String teamName) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberAge = memberAge;
        this.teamId = teamId;
        this.teamName = teamName;
    }
}