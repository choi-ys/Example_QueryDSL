package io.example.querydsl.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
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

    @QueryProjection // QueryDsl의 @QueryProjection를 이용한 Dto Projection조회 시 생성자에 선언
    public MemberDto(long memberNo, String memberName, int memberAge) {
        this.memberNo = memberNo;
        this.memberName = memberName;
        this.memberAge = memberAge;
    }
}
