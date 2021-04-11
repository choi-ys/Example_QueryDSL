package io.example.querydsl.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {

    private long memberNo;

    private String memberName;

    private int memberAge;

    public MemberDto(long memberNo, String memberName, int memberAge) {
        this.memberNo = memberNo;
        this.memberName = memberName;
        this.memberAge = memberAge;
    }
}
