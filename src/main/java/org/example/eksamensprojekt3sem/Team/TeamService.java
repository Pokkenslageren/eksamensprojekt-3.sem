package org.example.eksamensprojekt3sem.Team;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<TeamModel> getAllTeams() {
        return teamRepository.findAll();
    }
}
