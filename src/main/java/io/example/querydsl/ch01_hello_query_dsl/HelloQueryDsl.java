package io.example.querydsl.ch01_hello_query_dsl;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import static lombok.AccessLevel.PROTECTED;

@Entity
@SequenceGenerator(
        name = "HELLO_QUERY_DSL_ENTITY_SEQ_GENERATOR",
        sequenceName = "HELLO_QUERY_DSL_ENTITY_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@NoArgsConstructor(access = PROTECTED)
public class HelloQueryDsl {

    @Id @GeneratedValue(generator = "HELLO_QUERY_DSL_ENTITY_SEQ_GENERATOR")
    private Long id;

    private String name;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/04/05 7:57 오후
    // * --------------------------------------------------------------

    protected HelloQueryDsl(String name) {
        this.name = name;
    }

    public static HelloQueryDsl createHelloQueryDsl(String name){
        return new HelloQueryDsl(name);
    }
}