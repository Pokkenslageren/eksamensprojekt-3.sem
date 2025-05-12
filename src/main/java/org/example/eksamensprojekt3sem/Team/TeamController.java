package org.example.eksamensprojekt3sem.Team;

import jakarta.validation.Valid;
import org.example.eksamensprojekt3sem.Member.MemberModel;
import org.example.eksamensprojekt3sem.Member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("")
public class TeamController {

    private final TeamService teamService;
    private final MemberService memberService;
    private final TeamRepository teamRepository;

    public TeamController(TeamService teamService, MemberService memberService, TeamRepository teamRepository) {
        this.teamService = teamService;
        this.memberService = memberService;
        this.teamRepository = teamRepository;
    }

    @GetMapping("/teams")
    public List<TeamModel> getAllTeams() {
        return teamService.getAllTeams();
    }

    @PostMapping("/teams/add")
    public MemberModel addMemberToTeam(@Valid @RequestBody MemberModel member) {
        return teamService.addMember(member);
    }

    @PutMapping("/teams/update")
    public ResponseEntity<TeamModel> updateTeam(@PathVariable long id, @Valid @RequestBody TeamModel teamDetails) {
        return teamService.updateTeam(id, teamDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/teams/delete/{id}")
    public ResponseEntity<TeamModel> deleteTeam(@PathVariable long id) {
        if (teamService.deleteTeam(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
