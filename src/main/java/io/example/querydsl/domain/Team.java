package io.example.querydsl.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

/**
 * @author : choi-ys
 * @date : 2021-04-09 오후 3:32
 * @Content : 팀을 표현하는 Entity
 */
@Entity @Table(name = "team_tb")
@SequenceGenerator(
        name = "TEAM_ENTITY_SEQ_GENERATOR",
        sequenceName = "TEAM_ENTITY_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter @NoArgsConstructor(access = PROTECTED)
public class Team {

    @Id @GeneratedValue(generator = "TEAM_ENTITY_SEQ_GENERATOR")
    @Column(name = "team_no")
    private Long no;

    @Column(name = "team_name", nullable = false, length = 20)
    private String name;


    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/04/09 3:35 오후
    // * --------------------------------------------------------------

    @Builder
    public Team(String name) {
        this.name = name;
    }
}
