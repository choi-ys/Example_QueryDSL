package io.example.querydsl.domain.dto.pagination.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import static lombok.AccessLevel.PROTECTED;

/**
 * @author : choi-ys
 * @date : 2021/04/08 4:22 오후
 * @Content : Spring Data Jpa의 Slice<T> 반환 타입을 가공
 */
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Slice<T> {
    private int currentPage;
    private int currentElementCount;
    private int perPageNumber;

    private boolean firstPage;
    private boolean lastPage;
    private boolean hasNextPage;
    private boolean hasPrevious;

    public Slice(Page<T> page) {
        this.currentPage = page.getNumber()+1;
        this.currentElementCount = page.getNumberOfElements();
        this.perPageNumber = page.getSize();
        this.firstPage = page.isFirst();
        this.lastPage = page.isLast();
        this.hasNextPage = page.hasNext();
        this.hasPrevious = page.hasPrevious();
    }

    public Slice(org.springframework.data.domain.Slice<T> slice) {
        this.currentPage = slice.getNumber()+1;
        this.currentElementCount = slice.getNumberOfElements();
        this.perPageNumber = slice.getSize();
        this.firstPage = slice.isFirst();
        this.lastPage = slice.isLast();
        this.hasNextPage = slice.hasNext();
        this.hasPrevious = slice.hasPrevious();
    }
}