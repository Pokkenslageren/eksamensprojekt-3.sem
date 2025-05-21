package org.example.eksamensprojekt3sem.Team;

import jakarta.validation.Valid;
import org.example.eksamensprojekt3sem.Member.Member;
import org.example.eksamensprojekt3sem.Member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("fodboldklub/teams")
public class TeamController {

    private final TeamService teamService;
    private final MemberService memberService;
    private final TeamRepository teamRepository;

    public TeamController(TeamService teamService, MemberService memberService, TeamRepository teamRepository) {
        this.teamService = teamService;
        this.memberService = memberService;
        this.teamRepository = teamRepository;
    }

    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @PostMapping
    public Member addMemberToTeam(@Valid @RequestBody Member member) {
        return teamService.addMember(member);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable long id, @Valid @RequestBody Team teamDetails) {
        return teamService.updateTeam(id, teamDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Team> deleteTeam(@PathVariable long id) {
        if (teamService.deleteTeam(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
