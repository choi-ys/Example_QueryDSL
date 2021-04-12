package io.example.querydsl.domain.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @author : choi-ys
 * @date : 2021/04/12 12:21 오후
 * @Content : 엔티티 등록일/수정일 이력을 관리하기 위한 MetaData 설정 객체
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass // 해당 class의 속성만 Entity에 상속(JPA의 상속)하기 위해 명시
@Getter
public class BaseTimeEntity {

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
