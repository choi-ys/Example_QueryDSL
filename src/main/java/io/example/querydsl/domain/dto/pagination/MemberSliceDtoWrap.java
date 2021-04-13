package io.example.querydsl.domain.dto.pagination;

import io.example.querydsl.domain.dto.MemberTeamDto;
import io.example.querydsl.domain.dto.pagination.common.Slice;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

/**
 * @author : choi-ys
 * @date : 2021/04/08 4:38 오후
 * @Content : MemberTeamDto Slice 반환을 위한 DTO
 */
@Getter @NoArgsConstructor(access = PROTECTED)
public class MemberSliceDtoWrap extends Slice {
    private List<MemberTeamDto> memberDtoList = new ArrayList<>();

    public MemberSliceDtoWrap(org.springframework.data.domain.Slice slice) {
        super(slice);
        this.memberDtoList = slice.getContent();
    }
}