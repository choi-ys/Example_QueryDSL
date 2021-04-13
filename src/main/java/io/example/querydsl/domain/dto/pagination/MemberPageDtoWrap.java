package io.example.querydsl.domain.dto.pagination;

import io.example.querydsl.domain.dto.MemberTeamDto;
import io.example.querydsl.domain.dto.pagination.common.Page;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

/**
 * @author : choi-ys
 * @date : 2021/04/08 3:24 오후
 * @Content : MemberTeamDto Paging 반환을 위한 DTO
 */
@Getter @NoArgsConstructor(access = PROTECTED)
public class MemberPageDtoWrap extends Page {
    private List<MemberTeamDto> memberDtoList = new ArrayList<>();

    public MemberPageDtoWrap(org.springframework.data.domain.Page page) {
        super(page);
        this.memberDtoList = page.getContent();
    }
}