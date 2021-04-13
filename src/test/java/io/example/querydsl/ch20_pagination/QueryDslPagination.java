package io.example.querydsl.ch20_pagination;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.querydsl.config.BaseTest;
import io.example.querydsl.domain.Team;
import io.example.querydsl.domain.dto.MemberTeamDto;
import io.example.querydsl.domain.dto.pagination.MemberPageDtoWrap;
import io.example.querydsl.domain.dto.pagination.MemberSliceDtoWrap;
import io.example.querydsl.domain.search.MemberSearchCondition;
import io.example.querydsl.repository.pagination.MemberSearchRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

/**
 * @author : choi-ys
 * @date : 2021/04/13 1:51 오후
 * @Content : QueryDsl의 Pagination Test
 */
@DisplayName("QueryDSL의 Pagination")
public class QueryDslPagination extends BaseTest {

    @Autowired
    MemberSearchRepository memberSearchRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Pagination : simple")
//    @Rollback(value = false)
    public void searchPageSimple() throws JsonProcessingException {
        // Given
        Team savedTeam = teamGenerator.savedTeam();
        memberGenerator.savedMemberWithTeam("이성욱", 31, savedTeam);
        memberGenerator.savedMemberWithTeam("박재현", 29, savedTeam);
        memberGenerator.savedMemberWithTeam("기호창", 31, savedTeam);
        memberGenerator.savedMemberWithTeam("최용석", 31, savedTeam);

        MemberSearchCondition memberSearchCondition = new MemberSearchCondition();
        memberSearchCondition.setTeamName("CoreDevTeam");
        memberSearchCondition.setAgeGoe(20);
        memberSearchCondition.setAgeLoe(35);

        int page = 0;
        int perPageNum = 2;
        PageRequest pageRequest = PageRequest.of(page, perPageNum);

        // When
        Page<MemberTeamDto> memberTeamDtoList = memberSearchRepository.searchPageSimple(memberSearchCondition, pageRequest);

        // Then
        System.out.println("================================================================");
        System.out.println("getTotalPages : " + memberTeamDtoList.getTotalPages());
        System.out.println("getTotalElements : " + memberTeamDtoList.getTotalElements());
        System.out.println("getNumber : " + memberTeamDtoList.getNumber());
        System.out.println("getNumberOfElements : " + memberTeamDtoList.getNumberOfElements());
        System.out.println("getSize : " + memberTeamDtoList.getSize());
        System.out.println("isFirst : " + memberTeamDtoList.isFirst());
        System.out.println("isLast : " + memberTeamDtoList.isLast());
        System.out.println("hasNext : " + memberTeamDtoList.hasNext());
        System.out.println("hasPrevious : " + memberTeamDtoList.hasPrevious());
        System.out.println("================================================================");

        MemberPageDtoWrap memberPagingDtoWrap = new MemberPageDtoWrap(memberTeamDtoList);
        while (memberPagingDtoWrap.isHasNextPage()){
            page = page+1;
            PageRequest nextRequest = PageRequest.of(page, perPageNum);
            memberTeamDtoList = memberSearchRepository.searchPageSimple(memberSearchCondition, nextRequest);
            memberPagingDtoWrap = new MemberPageDtoWrap(memberTeamDtoList);
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(memberPagingDtoWrap));
        }
    }

    @Test
    @DisplayName("Pagination : complex")
//    @Rollback(value = false)
    public void searchPageComplex() throws JsonProcessingException {
        // Given
        Team savedTeam = teamGenerator.savedTeam();
        memberGenerator.savedMemberWithTeam("이성욱", 31, savedTeam);
        memberGenerator.savedMemberWithTeam("박재현", 29, savedTeam);
        memberGenerator.savedMemberWithTeam("기호창", 31, savedTeam);
        memberGenerator.savedMemberWithTeam("최용석", 31, savedTeam);

        MemberSearchCondition memberSearchCondition = new MemberSearchCondition();
        memberSearchCondition.setTeamName("CoreDevTeam");
        memberSearchCondition.setAgeGoe(20);
        memberSearchCondition.setAgeLoe(35);

        int page = 0;
        int perPageNum = 2;
        PageRequest pageRequest = PageRequest.of(page, perPageNum);

        // When
        Page<MemberTeamDto> memberTeamDtoList = memberSearchRepository.searchPageComplex(memberSearchCondition, pageRequest);

        // Then
        System.out.println("================================================================");
        System.out.println("getTotalPages : " + memberTeamDtoList.getTotalPages());
        System.out.println("getTotalElements : " + memberTeamDtoList.getTotalElements());
        System.out.println("getNumber : " + memberTeamDtoList.getNumber());
        System.out.println("getNumberOfElements : " + memberTeamDtoList.getNumberOfElements());
        System.out.println("getSize : " + memberTeamDtoList.getSize());
        System.out.println("isFirst : " + memberTeamDtoList.isFirst());
        System.out.println("isLast : " + memberTeamDtoList.isLast());
        System.out.println("hasNext : " + memberTeamDtoList.hasNext());
        System.out.println("hasPrevious : " + memberTeamDtoList.hasPrevious());
        System.out.println("================================================================");

        MemberPageDtoWrap memberPagingDtoWrap = new MemberPageDtoWrap(memberTeamDtoList);
        while (memberPagingDtoWrap.isHasNextPage()){
            page = page+1;
            PageRequest nextRequest = PageRequest.of(page, perPageNum);
            memberTeamDtoList = memberSearchRepository.searchPageComplex(memberSearchCondition, nextRequest);
            memberPagingDtoWrap = new MemberPageDtoWrap(memberTeamDtoList);
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(memberPagingDtoWrap));
        }
    }

    @Test
    @DisplayName("Pagination : count query optimization")
    //    @Rollback(value = false)
    public void searchPageComplexCountQueryOptimization() throws JsonProcessingException {
        // Given
        Team savedTeam = teamGenerator.savedTeam();
        memberGenerator.savedMemberWithTeam("이성욱", 31, savedTeam);
        memberGenerator.savedMemberWithTeam("박재현", 29, savedTeam);
        memberGenerator.savedMemberWithTeam("기호창", 31, savedTeam);
        memberGenerator.savedMemberWithTeam("최용석", 31, savedTeam);
        memberGenerator.savedMemberWithTeam("이하은", 29, savedTeam);

        MemberSearchCondition memberSearchCondition = new MemberSearchCondition();
        memberSearchCondition.setTeamName("CoreDevTeam");
        memberSearchCondition.setAgeGoe(20);
        memberSearchCondition.setAgeLoe(35);

        int page = 2;
        int perPageNum = 2;
        PageRequest pageRequest = PageRequest.of(page, perPageNum);

        // When
        Page<MemberTeamDto> memberTeamDtoList = memberSearchRepository.searchPageComplexCountQueryOptimization(memberSearchCondition, pageRequest);

        // Then
        System.out.println("================================================================");
        System.out.println("getTotalPages : " + memberTeamDtoList.getTotalPages());
        System.out.println("getTotalElements : " + memberTeamDtoList.getTotalElements());
        System.out.println("getNumber : " + memberTeamDtoList.getNumber());
        System.out.println("getNumberOfElements : " + memberTeamDtoList.getNumberOfElements());
        System.out.println("getSize : " + memberTeamDtoList.getSize());
        System.out.println("isFirst : " + memberTeamDtoList.isFirst());
        System.out.println("isLast : " + memberTeamDtoList.isLast());
        System.out.println("hasNext : " + memberTeamDtoList.hasNext());
        System.out.println("hasPrevious : " + memberTeamDtoList.hasPrevious());
        System.out.println("================================================================");

        MemberPageDtoWrap memberPagingDtoWrap = new MemberPageDtoWrap(memberTeamDtoList);
        while (memberPagingDtoWrap.isHasNextPage()){
            page = page+1;
            PageRequest nextRequest = PageRequest.of(page, perPageNum);
            memberTeamDtoList = memberSearchRepository.searchPageComplexCountQueryOptimization(memberSearchCondition, nextRequest);
            memberPagingDtoWrap = new MemberPageDtoWrap(memberTeamDtoList);
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(memberPagingDtoWrap));
        }
    }

    @Test
    @DisplayName("slice")
    //    @Rollback(value = false)
    public void searchSliceSimple() throws JsonProcessingException {
        // Given
        Team savedTeam = teamGenerator.savedTeam();
        memberGenerator.savedMemberWithTeam("이성욱", 31, savedTeam);
        memberGenerator.savedMemberWithTeam("박재현", 29, savedTeam);
        memberGenerator.savedMemberWithTeam("기호창", 31, savedTeam);
        memberGenerator.savedMemberWithTeam("최용석", 31, savedTeam);
        memberGenerator.savedMemberWithTeam("이하은", 29, savedTeam);

        MemberSearchCondition memberSearchCondition = new MemberSearchCondition();
        memberSearchCondition.setTeamName("CoreDevTeam");
        memberSearchCondition.setAgeGoe(20);
        memberSearchCondition.setAgeLoe(35);

        int page = 0;
        int perPageNum = 2;
        PageRequest pageRequest = PageRequest.of(page, perPageNum);

        // When
        Slice<MemberTeamDto> memberTeamDtoList = memberSearchRepository.searchSliceSimple(memberSearchCondition, pageRequest);

        // Then
        System.out.println("================================================================");
        System.out.println("getNumber : " + memberTeamDtoList.getNumber());
        System.out.println("getNumberOfElements : " + memberTeamDtoList.getNumberOfElements());
        System.out.println("getSize : " + memberTeamDtoList.getSize());
        System.out.println("isFirst : " + memberTeamDtoList.isFirst());
        System.out.println("isLast : " + memberTeamDtoList.isLast());
        System.out.println("hasNext : " + memberTeamDtoList.hasNext());
        System.out.println("hasPrevious : " + memberTeamDtoList.hasPrevious());
        System.out.println("================================================================");

        MemberSliceDtoWrap memberSliceDtoWrap = new MemberSliceDtoWrap(memberTeamDtoList);
        while (memberSliceDtoWrap.isHasNextPage()){
            page = page+1;
            PageRequest nextRequest = PageRequest.of(page, perPageNum);
            memberTeamDtoList = memberSearchRepository.searchSliceSimple(memberSearchCondition, nextRequest);
            memberSliceDtoWrap = new MemberSliceDtoWrap(memberTeamDtoList);
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(memberSliceDtoWrap));
        }
    }
}