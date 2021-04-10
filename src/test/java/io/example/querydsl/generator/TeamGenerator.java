package io.example.querydsl.generator;

import io.example.querydsl.domain.Team;
import io.example.querydsl.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TeamGenerator {

    @Autowired
    TeamRepository teamRepository;

    public Team savedTeam(){
        String teamName = "CoreDevTeam";
        Team team = Team.builder()
                .name(teamName)
                .build();
        return teamRepository.save(team);
    }

}
