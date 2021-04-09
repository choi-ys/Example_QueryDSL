package io.example.querydsl.generator;

import io.example.querydsl.chO1_hello_query_dsl.SpringDataJpaRepository;
import io.example.querydsl.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : choi-ys
 * @date : 2021-04-09 오후 6:18
 * @Content : 회원 Test case 수행에 필요한 Member Entity 생성
 */
public class MemberGenerator {

    @Autowired
    SpringDataJpaRepository springDataJpaRepository;

    public Member savedMember(){
        String memberName = "최용석";
        int age = 31;
        Member member = Member.builder()
                .name(memberName)
                .age(age)
                .build();
        return this.springDataJpaRepository.save(member);
    }
}