package io.example.querydsl.ch19_spring_data_jpa_with_query_dsl;

import io.example.querydsl.config.BaseTest;
import io.example.querydsl.domain.Member;
import io.example.querydsl.domain.Team;
import io.example.querydsl.domain.dto.MemberTeamDto;
import io.example.querydsl.domain.search.MemberSearchCondition;
import io.example.querydsl.generator.MemberGenerator;
import io.example.querydsl.repository.custom.MemberSpringDataJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author : choi-ys
 * @date : 2021/04/13 9:46 오전
 * @Content : Spring Data Jpa와 QueryDSL로 구성된 Repository Test
 */
public class SpringDataJpaRepositoryWithQueryDsl extends BaseTest {

    @Autowired
    MemberSpringDataJpaRepository memberSpringDataJpaRepository;

    @Test
    @DisplayName("save")
//    @Rollback(value = false)
    public void save(){
        // Given
        Member member = MemberGenerator.getMember();

        // When
        Member savedMember = memberSpringDataJpaRepository.save(member);

        // Then
        assertEquals(savedMember, member);
    }

    @Test
    @DisplayName("findById")
//    @Rollback(value = false)
    public void findById(){
        // Given
        Member savedMember = memberGenerator.savedMember();

        entityManager.flush();
        entityManager.clear();

        // When
        Optional<Member> optionalMember = memberSpringDataJpaRepository.findById(savedMember.getNo());
        Member selectedMember = optionalMember.orElseThrow();

        // Then
        assertEquals(selectedMember.getNo(), savedMember.getNo());
    }

    @Test
    @DisplayName("remove")
//    @Rollback(value = false)
    public void remove(){
        // Given
        Member savedMember = memberGenerator.savedMember();
        entityManager.flush();
        entityManager.clear();

        // When
        memberSpringDataJpaRepository.delete(savedMember);

        // Then
        Optional<Member> optionalMember = memberSpringDataJpaRepository.findById(savedMember.getNo());

        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            Member member = optionalMember.orElseThrow();
            assertEquals(member, null);
        });
        assertEquals(noSuchElementException.getMessage(), "No value present");
    }

    @Test
    @DisplayName("findMemberTeamDto By QueryDsl")
//    @Rollback(value = false)
    public void findMemberTeamDtoByQueryDsl(){
        // Given
        Team savedTeam = teamGenerator.savedTeam();
        memberGenerator.savedMemberWithTeam("최용석", 31, savedTeam);
        memberGenerator.savedMemberWithTeam("이성욱", 31, savedTeam);
        memberGenerator.savedMemberWithTeam("박재현", 29, savedTeam);
        memberGenerator.savedMemberWithTeam("이하은", 29, savedTeam);

        // When
        MemberSearchCondition memberSearchCondition = new MemberSearchCondition();
        memberSearchCondition.setMemberName("박재현");
        memberSearchCondition.setTeamName("CoreDevTeam");
        memberSearchCondition.setAgeGoe(20);
        memberSearchCondition.setAgeLoe(30);

        List<MemberTeamDto> memberTeamDtoList = memberSpringDataJpaRepository.searchMemberTeam(memberSearchCondition);

        // Then
        assertThat(memberTeamDtoList)
                .extracting("memberName").containsExactly("박재현");
    }
}