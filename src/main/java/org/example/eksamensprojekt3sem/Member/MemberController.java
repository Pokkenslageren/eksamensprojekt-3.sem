package org.example.eksamensprojekt3sem.Member;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public List getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberModel> getMemberById(@PathVariable long id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/members/add")
    public MemberModel createMember(@Valid @RequestBody MemberModel member) {
        return memberService.addMember(member);
    }

    @PutMapping("/members/update/{id}")
    public ResponseEntity<MemberModel> updateMember(@PathVariable long id, @RequestBody MemberModel memberDetails) {
        return memberService.updateMember(id, memberDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/members/delete/{id}")
    public ResponseEntity<MemberModel> deleteMember(@PathVariable long id) {
        if (memberService.deleteMember(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
