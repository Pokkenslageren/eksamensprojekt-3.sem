package org.example.eksamensprojekt3sem.Team;

import jakarta.validation.Valid;
import org.example.eksamensprojekt3sem.Member.Member;
import org.example.eksamensprojekt3sem.Member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    public TeamService(TeamRepository teamRepository, MemberRepository memberRepository) {
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<Member> getMembers(){
        return memberRepository.findAll();
    }

    public Member addMember(Member member){
        return memberRepository.save(member);
    }

    public Optional<Team> updateTeam(long id, @Valid Team teamDetails) {
        if (id <= 0){
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return teamRepository.findById(id).map(team1 -> {
            team1.setName(teamDetails.getName());
            team1.setDescription(teamDetails.getDescription());
            team1.setActive(teamDetails.isActive());
            return teamRepository.save(team1);
        });
    }

    public boolean deleteTeam(long id) {
        if (id <= 0){
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        if(teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
