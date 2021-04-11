package io.example.querydsl.generator;

import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.Team;
import io.example.querydsl.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : choi-ys
 * @date : 2021-04-09 오후 6:18
 * @Content : 회원 Test case 수행에 필요한 Member Entity 생성
 */
public class MemberGenerator {

    @Autowired
    MemberRepository memberRepository;

    public Member savedMember(){
        String memberName = "최용석";
        int age = 31;
        Member member = this.memberBuild(memberName, age, null);
        return this.memberRepository.save(member);
    }

    public Member savedMemberWithParam(String memberName, int age){
        Member member = memberBuild(memberName, age, null);
        return this.memberRepository.save(member);
    }

    public Member savedMemberWithTeam(String memberName, int age, Team team){
        Member member = memberBuild(memberName, age, team);
        return this.memberRepository.save(member);
    }

    private Member memberBuild(String memberName, int age, Team team) {
        Member member = Member.builder()
                .name(memberName)
                .age(age)
                .team(team)
                .build();
        return member;
    }
}