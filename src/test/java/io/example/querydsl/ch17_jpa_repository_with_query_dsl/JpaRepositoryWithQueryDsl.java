package io.example.querydsl.ch17_jpa_repository_with_query_dsl;

import io.example.querydsl.config.BaseTest;
import io.example.querydsl.domain.Member;
import io.example.querydsl.generator.MemberGenerator;
import io.example.querydsl.repository.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author : choi-ys
 * @date : 2021/04/12 6:54 오후
 * @Content : 순수 Jpa와 QueryDsl로 구성된 Repository Test
 */
public class JpaRepositoryWithQueryDsl extends BaseTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("save")
//    @Rollback(value = false)
    public void save(){
        // Given
        Member member = MemberGenerator.getMember();

        // When
        Member savedMember = this.memberJpaRepository.save(member);

        // Then
        assertEquals(savedMember, member);
    }

    @Test
    @DisplayName("findById")
//    @Rollback(value = false)
    public void findById(){
        // Given
        Member savedMember = memberGenerator.savedMember();

        // When
        Optional<Member> optionalMember = memberJpaRepository.findById(savedMember.getNo());
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

        // When
        memberJpaRepository.remove(savedMember);

        // Then
        Optional<Member> optionalMember = memberJpaRepository.findById(savedMember.getNo());

        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            Member selectedMember = optionalMember.orElseThrow();
            assertEquals(selectedMember, null);
        });
        assertEquals(noSuchElementException.getMessage(), "No value present");
    }

    @Test
    @DisplayName("findByMemberAge")
//    @Rollback(value = false)
    public void findByMemberAge(){
        // Given
        memberGenerator.savedMemberWithParam("최용석", 31);
        memberGenerator.savedMemberWithParam("이성욱", 31);
        memberGenerator.savedMemberWithParam("박재현", 29);
        int searchMemberAge = 31;

        // When
        List<Member> memberList = memberJpaRepository.findByMemberAge(searchMemberAge);

        // Then
        assertThat(memberList).extracting("age")
                .doesNotContain(29)
                .containsExactly(31, 31);
    }

    @Test
    @DisplayName("findByMemberAge to QueryDsl")
//    @Rollback(value = false)
    public void findByMemberAgeToQueryDsl(){
        // Given
        memberGenerator.savedMemberWithParam("최용석", 31);
        memberGenerator.savedMemberWithParam("이성욱", 31);
        memberGenerator.savedMemberWithParam("박재현", 29);
        int searchMemberAge = 31;

        // When
        List<Member> memberList = memberJpaRepository.findByMemberAgeToQueryDsl(searchMemberAge);

        // Then
        assertThat(memberList).extracting("age")
                .doesNotContain(29)
                .containsExactly(31, 31);
    }
}