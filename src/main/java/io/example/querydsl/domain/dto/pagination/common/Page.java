package io.example.querydsl.domain.dto.pagination.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

/**
 * @author : choi-ys
 * @date : 2021/04/08 3:20 오후
 * @Content : Spring Data Jpa의 Page<T> 반환 타입을 가공
 */
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Page<T> extends Slice {
    private int totalPageCount;
    private long totalElementCount;

    public Page(org.springframework.data.domain.Page page) {
        super(page);
        this.totalPageCount = page.getTotalPages();
        this.totalElementCount = page.getTotalElements();
    }
}